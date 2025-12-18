package com.restaurant.resource;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.restaurant.model.CatalogoComida;
import com.restaurant.service.CatalogoComidaService;

@Path("/catalogo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CatalogoComidaResource {

    private CatalogoComidaService service = new CatalogoComidaService();
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
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CatalogoComida> listarCatalogo() {
        return service.listar();
    }
    
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response agregar(
            @QueryParam("idAdmin") int idAdmin,
            @FormDataParam("data") String requestJsonString,
            @FormDataParam("imagenPlato") InputStream imagenInputStream, 
            @FormDataParam("imagenPlato") FormDataContentDisposition imagenFileDetail
    ) {
        
        File imagenTempFile = null;
        CatalogoComida catalogoComida = null; 

        try {
            if (requestJsonString == null || requestJsonString.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"Datos JSON del catálogo obligatorios.\"}").build();
            }
            catalogoComida = objectMapper.readValue(requestJsonString, CatalogoComida.class);

            if (idAdmin <= 0) {
                 return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"ID de administrador inválido.\"}").build();
            }
            if (imagenInputStream == null || imagenFileDetail == null || imagenFileDetail.getFileName().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"La imagen del plato es obligatoria.\"}").build();
            }
            if (imagenFileDetail.getSize() > MAX_FILE_SIZE_BYTES) {
                 return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"La Imagen excede el tamaño máximo permitido de 10 MB.\"}").build();
            }
            
            imagenTempFile = File.createTempFile("img_plato_", "_" + imagenFileDetail.getFileName());
            copyStreamToFile(imagenInputStream, imagenTempFile);

            int idGenerado = service.agregarPlatoCompleto(
                    catalogoComida, 
                    imagenTempFile,
                    imagenFileDetail,
                    idAdmin
            );

            if (idGenerado > 0) {
                String json = String.format("{\"mensaje\": \"Plato registrado exitosamente\", \"idPlato\": %d}", idGenerado);
                return Response.status(Response.Status.CREATED).entity(json).build();
            } else {
                 return Response.status(Response.Status.BAD_REQUEST)
                         .entity("{\"error\": \"No se pudo registrar el plato. Verifique logs.\"}")
                         .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            String error = String.format("{\"error\": \"Error interno en el servidor: %s\"}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        } finally {
            if (imagenTempFile != null && imagenTempFile.exists()) {
                imagenTempFile.delete();
            }
        }
    }
    
    @PUT
    @Path("/{idCatalogo}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizar(@PathParam("idCatalogo") int id, CatalogoComida comida) {
        boolean ok = service.actualizar(id, comida );
        if (ok) {
            return Response.ok("{\"mensaje\":\"CatalogoComida actualizado\"}").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"El plato no encontrado o no se pudo actualizar\"}").build();
        }
    }
    @DELETE
    @Path("/{idCatalogo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(@PathParam("idCatalogo") int id) {
    	
                             
        boolean ok = service.eliminar(id);
        if (ok) {
            return Response.ok("{\"mensaje\":\"Plato eliminado correctamente\"}").build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("{\"error\":\"No se encontró el plato\"}").build();
    }
}
