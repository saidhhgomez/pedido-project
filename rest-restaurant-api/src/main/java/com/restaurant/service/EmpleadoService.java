package com.restaurant.service;

import java.util.HashMap;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.restaurant.dao.RegistroDAO;
import com.restaurant.model.Empleado;
import com.restaurant.model.EmpleadoCompletoRequest;
import com.restaurant.model.EmpleadoExisteCompletoRequest;
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

    public int registrarEmpleadoCompleto(EmpleadoCompletoRequest request) {
        try {
            Credenciales cred = request.getCredenciales();
            Persona persona = request.getPersona();
            Empleado empleado = request.getEmpleado();
            Contrato contrato = request.getContrato();

            if (cred == null || persona == null || empleado == null || contrato == null) {
                System.out.println("Error: Algún objeto está nulo. cred: " + cred + 
                                   ", persona: " + persona + 
                                   ", empleado: " + empleado +
                                   ", contrato: " + contrato);
                return 0;
            }

            if (cred.getUsuario() == null || cred.getContrasena() == null ||
                cred.getUsuario().isEmpty() || cred.getContrasena().isEmpty()) {
                System.out.println("Error: Usuario o contraseña vacíos.");
                return 0;
            }

            String passwordEncriptada = BCrypt.hashpw(cred.getContrasena(), BCrypt.gensalt());
            cred.setContrasena(passwordEncriptada);

            if (persona.getCorreo() != null && !persona.getCorreo().contains("@")) {
                System.out.println("Error: Correo no válido.");
                return 0;
            }

            if (contrato.getIdSucursal() <= 0 ||
                contrato.getIdTipoContrato() <= 0 ||
                contrato.getIdRol() <= 0 ||
                contrato.getSalario() == null ||
                contrato.getFechaInicio() == null ||
                contrato.getFechaFin() == null) {

                System.out.println("Error: Datos de contrato incompletos.");
                return 0;
            }
            
            return empleadoDAO.registrarEmpleadoCompleto(cred, persona, empleado, contrato);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
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
        
        StorageFile pdfStorageFile = new StorageFile();
        pdfStorageFile.setBucket(BackblazeConfig.getBucketName());
        pdfStorageFile.setObjectKey(pdfB2KeyCompleta);
        pdfStorageFile.setFilename(pdfUniqueName);
        pdfStorageFile.setContentType(pdfFileDetail.getType());
        pdfStorageFile.setSize(pdfTempFile.length());
        pdfStorageFile.setUploadedBy(idAdmin);
        
        StorageFile imagenStorageFile = new StorageFile();
        imagenStorageFile.setBucket(BackblazeConfig.getBucketName());
        imagenStorageFile.setObjectKey(imagenB2KeyCompleta);
        imagenStorageFile.setFilename(imagenUniqueName);
        imagenStorageFile.setContentType(imagenFileDetail.getType());
        imagenStorageFile.setSize(imagenTempFile.length());
        imagenStorageFile.setUploadedBy(idAdmin);
        
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
}
