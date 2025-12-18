package com.restaurant.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.model.ClienteCompletoRequest;
import com.restaurant.service.ClienteService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
<<<<<<< HEAD
=======
import java.util.HashMap;
>>>>>>> develop

@Path("/cliente")
@Produces(MediaType.APPLICATION_JSON)
public class ClienteResource {
    
    private final ClienteService service = new ClienteService();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024;
    
    private void copyStreamToFile(InputStream in, File file) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
    
    @POST
    @Path("/registrar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response registrarCliente(
            @FormDataParam("data") String requestJsonString,
            @FormDataParam("imagenCliente") InputStream imagenInputStream,
            @FormDataParam("imagenCliente") FormDataContentDisposition imagenFileDetail
    ) {
        
        File imagenTempFile = null;
        ClienteCompletoRequest request = null; 

        try {
            if (requestJsonString == null || requestJsonString.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"Datos JSON del cliente obligatorios.\"}").build();
            }
            request = objectMapper.readValue(requestJsonString, ClienteCompletoRequest.class);

            if (imagenInputStream == null || imagenFileDetail == null || imagenFileDetail.getFileName().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"La imagen del cliente es obligatoria.\"}").build();
            }
            if (imagenFileDetail.getSize() > MAX_FILE_SIZE_BYTES) {
                 return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"La Imagen excede el tamaño máximo permitido de 10 MB.\"}").build();
            }
            
            imagenTempFile = File.createTempFile("img_cliente_", "_" + imagenFileDetail.getFileName());
            copyStreamToFile(imagenInputStream, imagenTempFile);

            int idGenerado = service.registrarClienteCompleto(
                    request, 
                    imagenTempFile,
                    imagenFileDetail
            );

            if (idGenerado > 0) {
                String json = String.format("{\"mensaje\": \"Cliente registrado exitosamente\", \"idGenerado\": %d}", idGenerado);
                return Response.status(Response.Status.CREATED).entity(json).build();
            } else {
                 return Response.status(Response.Status.BAD_REQUEST)
                         .entity("{\"error\": \"No se pudo registrar el cliente. Verifique logs.\"}")
                         .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            String error = String.format("{\"error\": \"Error interno: %s\"}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        } finally {
            if (imagenTempFile != null && imagenTempFile.exists()) {
                imagenTempFile.delete();
            }
        }
    }
<<<<<<< HEAD
=======
    
    @GET
    @Path("/perfil/{idCliente}")
    public Response getPerfil(@PathParam("idCliente") int idCliente) {
        try {
            HashMap<String, Object> perfil = service.obtenerPerfilCompleto(idCliente);
            return Response.ok(perfil).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
>>>>>>> develop
}