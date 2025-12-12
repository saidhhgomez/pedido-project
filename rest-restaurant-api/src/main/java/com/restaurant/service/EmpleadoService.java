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

public class EmpleadoService {
	private final RegistroDAO empleadoDAO = new RegistroDAO();

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
            FormDataContentDisposition fileDetail,
            int idAdmin
    ) throws Exception {

        if (request == null || pdfTempFile == null || fileDetail == null) {
             throw new Exception("Datos de empleado o archivo PDF incompletos.");
        }
        if (idAdmin <= 0) {
            throw new Exception("El ID del administrador que registra es inválido.");
        }
        
        String numDocumento = request.getPersona().getNumDocumento();
        if (numDocumento == null || numDocumento.isEmpty()) {
            throw new Exception("El número de documento (numDocumento) de la Persona es obligatorio para la Key de B2.");
        }
        
        String originalFileName = fileDetail.getFileName();
        String keyPath = "firmados/" + numDocumento; 
        String b2KeyCompleta = "";
        
        try {
            b2KeyCompleta = BackblazeUtil.uploadFile(keyPath + "/" + originalFileName, pdfTempFile);
            
        } catch (Exception e) {
            throw new Exception("Fallo al subir el Contrato PDF a Backblaze B2: " + e.getMessage());
        }
        
        Contrato contrato = request.getContrato();
        contrato.setPdfFirmadoKey(b2KeyCompleta); 
        
        StorageFile storageFileMetadata = new StorageFile();
        storageFileMetadata.setBucket(BackblazeConfig.getBucketName());
        storageFileMetadata.setObjectKey(b2KeyCompleta);
        storageFileMetadata.setFilename(originalFileName);
        storageFileMetadata.setContentType(fileDetail.getType());
        storageFileMetadata.setSize(pdfTempFile.length());
        storageFileMetadata.setUploadedBy(idAdmin);
        
        int idEmpleadoGenerado = 0;
        try {
            idEmpleadoGenerado = empleadoDAO.registrarEmpleadoExisteCompleto(
                    request.getEmpleado(),
                    contrato,
                    request.getPersona().getIdPersona(),
                    storageFileMetadata 
            );

            if (idEmpleadoGenerado <= 0) {
                 throw new Exception("El DAO no pudo registrar Empleado/Contrato, la transacción falló.");
            }
            
            return idEmpleadoGenerado;

        } catch (Exception e) {
            System.out.println("Error fatal en EmpleadoService durante la transacción: " + e.getMessage());
            
            try {
                BackblazeUtil.deleteFile(b2KeyCompleta);
                System.out.println("Archivo huérfano eliminado de B2: " + b2KeyCompleta);
            } catch (Exception ex) {
                System.err.println("ADVERTENCIA: No se pudo limpiar el archivo huérfano de B2: " + ex.getMessage());
            }
            
            throw e; 
        }
    }
}
