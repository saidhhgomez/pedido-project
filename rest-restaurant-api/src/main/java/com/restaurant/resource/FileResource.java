package com.restaurant.resource;

import com.restaurant.util.BackblazeUtil;
import com.restaurant.config.BackblazeConfig; // Importar para obtener el prefijo/folder

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;

@Path("/files")
@Produces(MediaType.APPLICATION_JSON)
public class FileResource {

    // ----------------------------------------
    // 1. LISTAR ARCHIVOS (GET /files/list?prefix=...)
    // ----------------------------------------
    @GET
    @Path("/list")
    public Response listFiles(@DefaultValue("") @QueryParam("prefix") String prefix) {
        try {
            // Llama a listFiles con el prefijo dado.
            // Si el prefijo es "", lista todo el bucket (gracias a la lógica de BackblazeUtil).
            return Response.ok(BackblazeUtil.listFiles(prefix)).build();
        } catch (Exception e) {
            // Uso de System.err.println o un logger es recomendado en servidores
            System.err.println("Error al listar archivos: " + e.getMessage());
            return Response.serverError().entity("Error al listar archivos: " + e.getMessage()).build();
        }
    }

    // ----------------------------------------
    // 2. SUBIR ARCHIVO (POST /files/upload)
    // ----------------------------------------
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @DefaultValue("") @FormDataParam("keyPath") String keyPath // Opcional: para definir una subcarpeta específica
    ) {
        if (uploadedInputStream == null || fileDetail == null || fileDetail.getFileName() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Debes enviar un archivo con un nombre válido.\"}")
                    .build();
        }
        
        File tempFile = null;
        try {
            // Paso 1: Guardar el InputStream temporalmente en el disco (necesario para el SDK de AWS/S3)
            String originalFileName = fileDetail.getFileName();
            tempFile = File.createTempFile("upload_", "_" + originalFileName);
            
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int read;
                while ((read = uploadedInputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            }
            
            // Paso 2: Definir la Key final que incluye la carpeta base y el nombre de archivo
            // La keyPath permite guardar en una subcarpeta (ej: 'facturas/').
            String finalKeyName = keyPath.isEmpty() 
                                ? originalFileName 
                                : keyPath + "/" + originalFileName;
            
            // Paso 3: Subir el archivo temporal a Backblaze B2 (BackblazeUtil añade el prefijo 'contratos/')
            BackblazeUtil.uploadFile(finalKeyName, tempFile);

            // Devolver la Key completa (incluyendo la carpeta base 'contratos/')
            String fullB2Key = BackblazeConfig.getFolder() + finalKeyName;

            return Response.ok("{\"mensaje\": \"Archivo subido exitosamente\", \"key\": \"" + fullB2Key + "\"}").build();
            
        } catch (Exception e) {
            System.err.println("Error al subir archivo a Backblaze: " + e.getMessage());
            return Response.serverError().entity("{\"error\": \"Error al subir archivo: " + e.getMessage() + "\"}").build();
        } finally {
            // Paso 4: MUY IMPORTANTE, limpiar el archivo temporal del disco
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    // ----------------------------------------
    // 3. DESCARGAR ARCHIVO (GET /files/download?key=...)
    // ----------------------------------------
    @GET
    @Path("/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@QueryParam("key") String key) {
        if (key == null || key.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Debe especificar la 'key' del archivo a descargar.")
                    .build();
        }
        
        File file = null;
        try {
            // BackblazeUtil descarga el archivo en un archivo temporal local
            file = BackblazeUtil.downloadFile(key);
            
            // Devolver el archivo al cliente
            Response.ResponseBuilder response = Response.ok((Object) file);
            
            // Esto fuerza al navegador a descargarlo con el nombre original del archivo temporal
            response.header("Content-Disposition", "attachment; filename=\"" + file.getName().substring(file.getName().indexOf("_") + 1) + "\"");
            
            return response.build();
            
        } catch (RuntimeException e) {
            // Capturar la RuntimeException lanzada por downloadFile si falla
            System.err.println("Error al descargar archivo: " + e.getMessage());
            // Nota: Aquí podrías añadir una comprobación de fileExists antes de descargar si fuera necesario.
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Archivo no encontrado o error de descarga: " + key).build();
        } finally {
            // MUY IMPORTANTE: Limpiar el archivo temporal del servidor después de enviarlo.
            // Nota: En entornos de producción, el manejo de archivos temporales debe ser robusto.
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }

    // ----------------------------------------
    // 4. ELIMINAR ARCHIVO (DELETE /files/delete?key=...)
    // ----------------------------------------
    @DELETE
    @Path("/delete")
    public Response deleteFile(@QueryParam("key") String key) {
        if (key == null || key.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Debe especificar la 'key' del archivo a eliminar.")
                    .build();
        }
        try {
            BackblazeUtil.deleteFile(key);
            return Response.ok("{\"mensaje\": \"Archivo eliminado (o marcado para eliminación): " + key + "\"}").build();
        } catch (Exception e) {
            System.err.println("Error al eliminar archivo: " + e.getMessage());
            return Response.serverError().entity("{\"error\": \"Error al eliminar archivo: " + e.getMessage() + "\"}").build();
        }
    }

    // ----------------------------------------
    // 5. VERIFICAR EXISTENCIA (GET /files/exists?key=...)
    // ----------------------------------------
    @GET
    @Path("/exists")
    public Response fileExists(@QueryParam("key") String key) {
        if (key == null || key.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Debe especificar la 'key' del archivo a verificar.\"}")
                    .build();
        }
        try {
            boolean exists = BackblazeUtil.fileExists(key);
            return Response.ok("{\"exists\": " + exists + "}").build();
        } catch (Exception e) {
            System.err.println("Error al verificar archivo: " + e.getMessage());
            return Response.serverError().entity("{\"error\": \"Error al verificar archivo: " + e.getMessage() + "\"}").build();
        }
    }
}