package com.restaurant.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.restaurant.dao.RegistroDAO;
import com.restaurant.model.Cliente;
import com.restaurant.model.ClienteCompletoRequest;
import com.restaurant.model.Credenciales;
import com.restaurant.model.Persona;
import com.restaurant.model.StorageFile;
import com.restaurant.util.BackblazeUtil;
import com.restaurant.config.BackblazeConfig;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.text.SimpleDateFormat;

public class ClienteService {
	private final RegistroDAO clientDAO = new RegistroDAO();
	
	private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg", "webp");
    
    private String generateUniqueFileName(String originalFileName, String numDocumento) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1).toLowerCase();
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return numDocumento + "_" + uniqueId + "_" + timestamp + "." + extension;
    }
    
    private StorageFile prepareStorageFile(String b2Key, String uniqueName, FormDataContentDisposition fileDetail, File tempFile, String relatedTable, int uploadedBy) {
        StorageFile storageFile = new StorageFile();
        storageFile.setBucket(BackblazeConfig.getBucketName());
        storageFile.setObjectKey(b2Key);
        storageFile.setFilename(uniqueName);
        storageFile.setContentType(fileDetail.getType());
        storageFile.setSize(tempFile.length());
        storageFile.setRelatedTable(relatedTable);
        return storageFile;
    }

	public int registrarClienteCompleto(ClienteCompletoRequest request, File imagenTempFile, FormDataContentDisposition imagenFileDetail) throws Exception {
	    
	    Credenciales cred = request.getCredenciales();
	    Persona persona = request.getPersona();
	    Cliente cliente = request.getCliente();

	    if (cred == null || persona == null || cliente == null ||
            cred.getUsuario() == null || cred.getContrasena() == null ||
            cred.getUsuario().isEmpty() || cred.getContrasena().isEmpty() ||
            (persona.getCorreo() != null && !persona.getCorreo().contains("@"))) {
	        
	        throw new Exception("Datos de registro incompletos o inválidos.");
	    }
	    
	    if (clientDAO.existeUsuario(cred.getUsuario())) {
	        throw new Exception("El nombre de usuario '" + cred.getUsuario() + "' ya existe.");
	    }
	    if (clientDAO.existeDocumento(persona.getNumDocumento())) {
	        throw new Exception("El número de documento '" + persona.getNumDocumento() + "' ya está registrado.");
	    }
	    if (persona.getCorreo() != null && clientDAO.existeCorreo(persona.getCorreo())) {
	        throw new Exception("El correo '" + persona.getCorreo() + "' ya está registrado por otro usuario.");
	    }
        
        String numDocumento = persona.getNumDocumento();
        if (numDocumento == null || numDocumento.isEmpty()) {
            throw new Exception("El número de documento (numDocumento) de la Persona es obligatorio para las Keys de B2.");
        }

	    String passwordEncriptada = BCrypt.hashpw(cred.getContrasena(), BCrypt.gensalt());
	    cred.setContrasena(passwordEncriptada);
        
        String imagenOriginalName = imagenFileDetail.getFileName();
        String imagenExtension = imagenOriginalName.substring(imagenOriginalName.lastIndexOf('.') + 1).toLowerCase();
        
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(imagenExtension)) {
            throw new Exception("Tipo de archivo de imagen no soportado.");
        }
        
        String imagenUniqueName = generateUniqueFileName(imagenOriginalName, numDocumento);
        String imagenB2KeyCompleta = "";
        String imagenKeyPath = "imagen_cliente/" + numDocumento; 
        
        try {
            imagenB2KeyCompleta = BackblazeUtil.uploadFile(imagenKeyPath + "/" + imagenUniqueName, imagenTempFile);
        } catch (Exception e) {
            throw new Exception("Fallo al subir la Imagen de Cliente a Backblaze B2: " + e.getMessage());
        }
        
        cliente.setImagenCliente_url(imagenB2KeyCompleta);
        
        StorageFile imagenStorageFile = prepareStorageFile(
            imagenB2KeyCompleta, 
            imagenUniqueName, 
            imagenFileDetail, 
            imagenTempFile, 
            "Cliente", 
            0
        );
        
        List<StorageFile> storageFiles = Arrays.asList(imagenStorageFile);
	    try {
	        return clientDAO.registrarClienteCompleto(cred, persona, cliente, storageFiles);

	    } catch (Exception e) {
            System.out.println("Error fatal en ClienteService durante la transacción: " + e.getMessage());
            try { BackblazeUtil.deleteFile(imagenB2KeyCompleta); } catch (Exception ex) {
                 System.err.println("ADVERTENCIA: No se pudo limpiar el archivo huérfano de B2: " + ex.getMessage());
            }
	        throw e;
	    }
	}
	
	public HashMap<String, Object> obtenerPerfilCompleto(int idCliente) throws Exception {
        HashMap<String, Object> perfil = clientDAO.obtenerPerfil(idCliente);
        
        if (perfil == null) {
            throw new Exception("Cliente no encontrado.");
        }

        return transformarImagenPerfil(perfil);
    }

    private HashMap<String, Object> transformarImagenPerfil(HashMap<String, Object> perfil) {
        String clave = (String) perfil.get("imagenUrl");
        
        if (clave != null && !clave.isEmpty()) {
            final String BASE_URL = BackblazeConfig.getPublicFileEndpoint(); 
            perfil.put("imagenUrl", BASE_URL + clave);
        } else {
            perfil.put("imagenUrl", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png");
        }
        return perfil;
    }
}
