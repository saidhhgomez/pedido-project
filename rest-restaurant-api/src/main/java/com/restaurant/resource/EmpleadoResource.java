package com.restaurant.resource;

import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.restaurant.model.EmpleadoCompletoRequest;
import com.restaurant.model.EmpleadoExisteCompletoRequest;
import com.restaurant.service.EmpleadoService;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

@Path("/empleado")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpleadoResource {
	
	private final EmpleadoService empleadoService = new EmpleadoService();
	private final ObjectMapper objectMapper = new ObjectMapper();

    @POST
    @Path("/registrar")
    public Response registrarEmpleadoCompleto(EmpleadoCompletoRequest request) {
        try {
            int idGenerado = empleadoService.registrarEmpleadoCompleto(request);

            if (idGenerado > 0) {
                String json = String.format(
                    "{\"mensaje\": \"Empleado registrado exitosamente\", \"idEmpleado\": %d}",
                    idGenerado
                );

                return Response.status(Response.Status.CREATED)
                        .entity(json)
                        .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"No se pudo registrar el empleado completo\"}")
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            String error = String.format("{\"error\": \"Error interno en el servidor: %s\"}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(error)
                    .build();
        }
    }
    
    @GET
    @Path("/dni/{dni}")
    public Response buscarPorDni(@PathParam("dni") String dni) {

        try {
            HashMap<String, Object> persona = empleadoService.buscarPorDni(dni);
            return Response.ok(persona).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();

        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno del servidor\"}")
                    .build();
        }
    }
    
    @POST
    @Path("/contrato-existente")
    @Consumes(MediaType.MULTIPART_FORM_DATA) 
    public Response registrarEmpleadoExisteCompleto(
            @QueryParam("idAdmin") int idAdmin, 
            @FormDataParam("data") String requestJsonString,
            @FormDataParam("pdfFirmado") InputStream pdfInputStream,
            @FormDataParam("pdfFirmado") FormDataContentDisposition fileDetail
    ) {
        
        File tempFile = null;
        EmpleadoExisteCompletoRequest request = null; 

        try {
            if (requestJsonString == null || requestJsonString.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"Datos JSON del empleado obligatorios.\"}").build();
            }
            if (idAdmin <= 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"El ID del administrador (idAdmin) es obligatorio y debe ser mayor a cero.\"}").build();
            }
            
            request = objectMapper.readValue(requestJsonString, EmpleadoExisteCompletoRequest.class);
            
            String fileName = fileDetail.getFileName();
            if (pdfInputStream == null || fileDetail == null || fileName == null || fileName.isEmpty() || !fileName.toLowerCase().endsWith(".pdf")) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"El archivo PDF firmado es obligatorio y debe ser un PDF válido.\"}").build();
            }

            tempFile = File.createTempFile("pdf_contrato_", "_" + fileName);
            
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = pdfInputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            
            int idEmpleadoGenerado = empleadoService.registrarEmpleadoExisteCompleto(
                    request, 
                    tempFile, 
                    fileDetail,
                    idAdmin
            );

            if (idEmpleadoGenerado > 0) {
                 return Response.ok().entity("{\"message\":\"Empleado y contrato registrados correctamente. ID Empleado: " + idEmpleadoGenerado + "\"}").build();
            } else {
                 return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"No se pudo registrar el empleado y contrato. Verifique logs para detalles.\"}")
                                .build();
            }

        } catch (Exception e) {
            System.err.println("Error en EmpleadoResource: " + e.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Error interno del servidor durante el registro: " + e.getMessage() + "\"}").build();
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}
