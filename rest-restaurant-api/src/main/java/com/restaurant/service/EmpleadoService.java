package com.restaurant.service;

import java.util.HashMap;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.restaurant.dao.RegistroDAO;
import com.restaurant.model.Empleado;
import com.restaurant.model.EmpleadoCompletoRequest;
import com.restaurant.model.EmpleadoExisteCompletoRequest;
import com.restaurant.model.Cliente;
import com.restaurant.model.Contrato;
import com.restaurant.model.Credenciales;
import com.restaurant.model.Persona;
import com.restaurant.util.BackblazeUtil;
import com.restaurant.config.BackblazeConfig;
import com.restaurant.model.StorageFile;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EmpleadoService {
	private final RegistroDAO empleadoDAO = new RegistroDAO();
	
	private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg", "webp");
    
    private String generateUniqueFileName(String originalFileName, String numDocumento) {
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1).toLowerCase();
        
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        
        return numDocumento + "_" + uniqueId + "_" + timestamp + "." + extension;
    }

    public HashMap<String, Object> buscarPorDni(String dni) {

        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI es obligatorio");
        }

        if (dni.length() < 8 || dni.length() > 15) {
            throw new IllegalArgumentException("El DNI debe tener entre 8 y 15 caracteres");
        }

        HashMap<String, Object> data = empleadoDAO.buscarPorDni(dni);

        if (data == null) {
            throw new RuntimeException("No se encontró ninguna persona con ese DNI");
        }

        return data;
    }

    public int registrarEmpleadoCompleto(
            EmpleadoCompletoRequest request, 
            File pdfTempFile, 
            FormDataContentDisposition pdfFileDetail, 
            File imagenTempFile, 
            FormDataContentDisposition imagenFileDetail,
            int idAdmin) throws Exception {

        Credenciales cred = request.getCredenciales();
        Persona persona = request.getPersona();
        Empleado empleado = request.getEmpleado();
        Contrato contrato = request.getContrato();
        
        if (cred == null || persona == null || empleado == null || contrato == null ||
            cred.getUsuario() == null || cred.getContrasena() == null ||
            cred.getUsuario().isEmpty() || cred.getContrasena().isEmpty() ||
            (persona.getCorreo() != null && !persona.getCorreo().contains("@")) ||
            contrato.getIdSucursal() <= 0 || contrato.getIdTipoContrato() <= 0 ||
            contrato.getIdRol() <= 0 || contrato.getSalario() == null ||
            contrato.getFechaInicio() == null) {
            
            throw new Exception("Datos de registro o contrato incompletos/inválidos.");
        }
        
        String numDocumento = persona.getNumDocumento();
        if (numDocumento == null || numDocumento.isEmpty()) {
            throw new Exception("El número de documento (numDocumento) de la Persona es obligatorio para las Keys de B2.");
        }
        
        cred.setContrasena(BCrypt.hashpw(cred.getContrasena(), BCrypt.gensalt()));

        List<String> b2KeysToRollback = new ArrayList<>();
        
        String imagenOriginalName = imagenFileDetail.getFileName();
        String imagenExtension = imagenOriginalName.substring(imagenOriginalName.lastIndexOf('.') + 1).toLowerCase();
        
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(imagenExtension)) {
            throw new Exception("Tipo de archivo de imagen no soportado.");
        }
        
        String imagenUniqueName = generateUniqueFileName(imagenOriginalName, numDocumento);
        
        String imagenEmpleadoKey = "";
        String imagenClienteKey = "";

        String imagenEmpleadoPath = "imagen_empleado/" + numDocumento; 
        try {
            imagenEmpleadoKey = BackblazeUtil.uploadFile(imagenEmpleadoPath + "/" + imagenUniqueName, imagenTempFile);
            b2KeysToRollback.add(imagenEmpleadoKey);
        } catch (Exception e) {
            throw new Exception("Fallo al subir la Imagen de Empleado a Backblaze B2: " + e.getMessage());
        }
        
        String imagenClientePath = "imagen_cliente/" + numDocumento;
        try {
            imagenClienteKey = BackblazeUtil.uploadFile(imagenClientePath + "/" + imagenUniqueName, imagenTempFile);
            b2KeysToRollback.add(imagenClienteKey);
        } catch (Exception e) {
            BackblazeUtil.deleteFile(imagenEmpleadoKey); 
            throw new Exception("Fallo al subir la Imagen de Cliente a Backblaze B2: " + e.getMessage());
        }
       
        String pdfOriginalFileName = pdfFileDetail.getFileName();
        String pdfUniqueName = generateUniqueFileName(pdfOriginalFileName, numDocumento);
        String pdfKeyPath = "contratos/firmados/" + numDocumento; 
        String pdfB2KeyCompleta = "";

        try {
            pdfB2KeyCompleta = BackblazeUtil.uploadFile(pdfKeyPath + "/" + pdfUniqueName, pdfTempFile);
            b2KeysToRollback.add(pdfB2KeyCompleta);
        } catch (Exception e) {
            BackblazeUtil.deleteFile(imagenEmpleadoKey);
            BackblazeUtil.deleteFile(imagenClienteKey);
            throw new Exception("Fallo al subir el Contrato PDF a Backblaze B2: " + e.getMessage());
        }
        
        Cliente cliente = new Cliente();
        cliente.setImagenCliente_url(imagenClienteKey);
        
        empleado.setImagenConductor_url(imagenEmpleadoKey); 
        
        contrato.setPdfFirmadoKey(pdfB2KeyCompleta); 
        
        StorageFile pdfStorageFile = prepareStorageFile(pdfB2KeyCompleta, pdfUniqueName, pdfFileDetail, pdfTempFile, "Contrato", idAdmin);
        StorageFile imagenEmpleadoStorageFile = prepareStorageFile(imagenEmpleadoKey, imagenUniqueName, imagenFileDetail, imagenTempFile, "Empleado", idAdmin);
        
        StorageFile imagenClienteStorageFile = prepareStorageFile(imagenClienteKey, imagenUniqueName, imagenFileDetail, imagenTempFile, "Cliente", idAdmin); 
        
        List<StorageFile> storageFiles = Arrays.asList(pdfStorageFile, imagenEmpleadoStorageFile, imagenClienteStorageFile);
        
        try {
             return empleadoDAO.registrarEmpleadoCompleto(
                    cred, 
                    persona, 
                    empleado, 
                    contrato, 
                    cliente,
                    storageFiles
             );

        } catch (Exception e) {
            System.out.println("Error fatal en EmpleadoService durante la transacción: " + e.getMessage());
            for (String key : b2KeysToRollback) {
                try { BackblazeUtil.deleteFile(key); } catch (Exception ex) {}
            }
            throw e; 
        }
    }
	
    public int registrarEmpleadoExisteCompleto(
            EmpleadoExisteCompletoRequest request, 
            File pdfTempFile, 
            FormDataContentDisposition pdfFileDetail,
            File imagenTempFile,
            FormDataContentDisposition imagenFileDetail,
            int idAdmin) throws Exception {

        if (request == null || pdfTempFile == null || imagenTempFile == null || idAdmin <= 0) {
             throw new Exception("Datos incompletos o ID de administrador inválido.");
        }
        
        String numDocumento = request.getPersona().getNumDocumento();
        if (numDocumento == null || numDocumento.isEmpty()) {
            throw new Exception("El número de documento (numDocumento) de la Persona es obligatorio para las Keys de B2.");
        }
        
        List<String> b2KeysToRollback = new ArrayList<>();
        
        String imagenOriginalName = imagenFileDetail.getFileName();
        String imagenExtension = imagenOriginalName.substring(imagenOriginalName.lastIndexOf('.') + 1).toLowerCase();
        
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(imagenExtension)) {
            throw new Exception("Tipo de archivo de imagen no soportado. Tipos permitidos: " + String.join(", ", ALLOWED_IMAGE_EXTENSIONS));
        }
        
        String imagenUniqueName = generateUniqueFileName(imagenOriginalName, numDocumento);
        
        String imagenKeyPath = "imagen_empleado/" + numDocumento;
        String imagenB2KeyCompleta = "";
        
        try {
            imagenB2KeyCompleta = BackblazeUtil.uploadFile(imagenKeyPath + "/" + imagenUniqueName, imagenTempFile);
            b2KeysToRollback.add(imagenB2KeyCompleta);
            
        } catch (Exception e) {
            throw new Exception("Fallo al subir la Imagen del Empleado a Backblaze B2: " + e.getMessage());
        }
        
        String pdfOriginalFileName = pdfFileDetail.getFileName();
        String pdfKeyPath = "contratos/firmados/" + numDocumento; 
        String pdfB2KeyCompleta = "";
        
        String pdfUniqueName = generateUniqueFileName(pdfOriginalFileName, numDocumento);

        try {
            pdfB2KeyCompleta = BackblazeUtil.uploadFile(pdfKeyPath + "/" + pdfUniqueName, pdfTempFile);
            b2KeysToRollback.add(pdfB2KeyCompleta);
            
        } catch (Exception e) {
            BackblazeUtil.deleteFile(imagenB2KeyCompleta);
            throw new Exception("Fallo al subir el Contrato PDF a Backblaze B2: " + e.getMessage());
        }
        
        Empleado empleado = request.getEmpleado();
        empleado.setImagenConductor_url(imagenB2KeyCompleta); 
        
        Contrato contrato = request.getContrato();
        contrato.setPdfFirmadoKey(pdfB2KeyCompleta); 
        
        StorageFile pdfStorageFile = prepareStorageFile(
            pdfB2KeyCompleta, 
            pdfUniqueName, 
            pdfFileDetail, 
            pdfTempFile, 
            "Contrato", 
            idAdmin
        );
        
        StorageFile imagenStorageFile = prepareStorageFile(
            imagenB2KeyCompleta, 
            imagenUniqueName, 
            imagenFileDetail, 
            imagenTempFile, 
            "Empleado", 
            idAdmin
        );
        
        List<StorageFile> storageFiles = Arrays.asList(pdfStorageFile, imagenStorageFile);

        int idEmpleadoGenerado = 0;
        try {
            idEmpleadoGenerado = empleadoDAO.registrarEmpleadoExisteCompleto(
                    empleado,
                    contrato,
                    request.getPersona().getIdPersona(),
                    storageFiles
            );

            if (idEmpleadoGenerado <= 0) {
                 throw new Exception("El DAO no pudo registrar Empleado/Contrato, la transacción falló.");
            }
            
            return idEmpleadoGenerado;

        } catch (Exception e) {
            System.out.println("Error fatal en EmpleadoService durante la transacción: " + e.getMessage());
            
            for (String key : b2KeysToRollback) {
                try {
                    BackblazeUtil.deleteFile(key);
                    System.out.println("Archivo huérfano eliminado de B2: " + key);
                } catch (Exception ex) {
                    System.err.println("ADVERTENCIA: No se pudo limpiar el archivo huérfano de B2: " + ex.getMessage());
                }
            }
            
            throw e; 
        }
    }
    
    private StorageFile prepareStorageFile(String b2Key, String uniqueName, FormDataContentDisposition fileDetail, File tempFile, String relatedTable, int uploadedBy) {
        StorageFile storageFile = new StorageFile();
        storageFile.setBucket(BackblazeConfig.getBucketName());
        storageFile.setObjectKey(b2Key);
        storageFile.setFilename(uniqueName);
        storageFile.setContentType(fileDetail.getType());
        storageFile.setSize(tempFile.length());
        storageFile.setRelatedTable(relatedTable);
        storageFile.setUploadedBy(uploadedBy);
        return storageFile;
    }
}