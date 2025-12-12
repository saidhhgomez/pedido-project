package com.restaurant.service;

import java.util.List;
import java.util.stream.Collectors;

import com.restaurant.dao.CatalogoComidaDAO;
import com.restaurant.model.CatalogoComida;import com.restaurant.model.StorageFile;
import com.restaurant.util.BackblazeUtil;
import com.restaurant.config.BackblazeConfig;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.text.SimpleDateFormat;

public class CatalogoComidaService {

    private CatalogoComidaDAO dao = new CatalogoComidaDAO();
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg", "webp");
    
    private CatalogoComida transformarAUrlPublica(CatalogoComida plato) {
        String clave = plato.getImagenPlatoUrl();
        if (clave != null && !clave.isEmpty()) {
            final String BASE_URL = BackblazeConfig.getPublicFileEndpoint(); 
            
            String urlCompleta = BASE_URL + clave;
            
            plato.setImagenPlatoUrl(urlCompleta);
        }
        return plato;
    }
    
    public List<CatalogoComida> listar() {
        List<CatalogoComida> listaConKeys = dao.obtenerTodos();
        
        return listaConKeys.stream()
                           .map(this::transformarAUrlPublica)
                           .collect(Collectors.toList());
    }
    
    private String generateUniqueFileName(String originalFileName, String itemName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1).toLowerCase();
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return itemName.replaceAll("\\s", "_").toLowerCase() + "_" + uniqueId + "_" + timestamp + "." + extension;
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

    public int agregarPlatoCompleto(
            CatalogoComida plato, 
            File imagenTempFile, 
            FormDataContentDisposition imagenFileDetail, 
            int idAdmin) throws Exception {

    	if (plato == null || idAdmin <= 0 ||
    	        plato.getNombre() == null || plato.getNombre().trim().isEmpty() || 
    	        plato.getCategoria() == null || plato.getCategoria().trim().isEmpty() || 
    	        plato.getPrecio() == null || plato.getStock() == null) {
    	        
    	        throw new Exception("Datos del plato (nombre, categoría, precio, stock) o ID de administrador incompletos/nulos.");
    	    }
    	    
    	if (plato.getPrecio() <= 0 || plato.getStock() < 0) {
    		throw new Exception("Precio debe ser positivo y Stock no puede ser negativo.");
    	}
        
        String imagenOriginalName = imagenFileDetail.getFileName();
        String imagenExtension = imagenOriginalName.substring(imagenOriginalName.lastIndexOf('.') + 1).toLowerCase();
        
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(imagenExtension)) {
            throw new Exception("Tipo de archivo de imagen no soportado.");
        }
        
        String imagenUniqueName = generateUniqueFileName(imagenOriginalName, plato.getNombre());
        String imagenB2KeyCompleta = "";
        String imagenKeyPath = "imagen_catalogo/" + plato.getCategoria().replaceAll("\\s", "_").toLowerCase(); 
        
        try {
            imagenB2KeyCompleta = BackblazeUtil.uploadFile(imagenKeyPath + "/" + imagenUniqueName, imagenTempFile);
        } catch (Exception e) {
            throw new Exception("Fallo al subir la Imagen del Plato a Backblaze B2: " + e.getMessage());
        }
        
        plato.setImagenPlatoUrl(imagenB2KeyCompleta);
        
        StorageFile imagenStorageFile = prepareStorageFile(
            imagenB2KeyCompleta, 
            imagenUniqueName, 
            imagenFileDetail, 
            imagenTempFile, 
            "CatalogoComida", 
            idAdmin
        );
        
        List<StorageFile> storageFiles = Arrays.asList(imagenStorageFile);

	    try {
	        return dao.agregarPlatoCompleto(plato, storageFiles);

	    } catch (Exception e) {
            System.out.println("Error fatal en CatalogoComidaService durante la transacción: " + e.getMessage());
            try { BackblazeUtil.deleteFile(imagenB2KeyCompleta); } catch (Exception ex) {
                 System.err.println("ADVERTENCIA: No se pudo limpiar el archivo huérfano de B2: " + ex.getMessage());
            }
	        throw e;
	    }
    }
    
    public boolean actualizar(int id, CatalogoComida d) {
        return dao.actualizar(id, d );
    }
    public boolean eliminar(int id) {
        return dao.eliminar(id);
    }
}
