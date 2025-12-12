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
import java.io.IOException;

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
    public Response registrarEmpleadoCompleto(
            @QueryParam("idAdmin") int idAdmin, 
            @FormDataParam("data") String requestJsonString,
            @FormDataParam("pdfFirmado") InputStream pdfInputStream,
            @FormDataParam("pdfFirmado") FormDataContentDisposition pdfFileDetail,
            @FormDataParam("imagenEmpleado") InputStream imagenInputStream,
            @FormDataParam("imagenEmpleado") FormDataContentDisposition imagenFileDetail
    ) {
        
        File pdfTempFile = null;
        File imagenTempFile = null;
        EmpleadoCompletoRequest request = null; 

        try {
            if (requestJsonString == null || requestJsonString.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"Datos JSON del empleado obligatorios.\"}").build();
            }
            request = objectMapper.readValue(requestJsonString, EmpleadoCompletoRequest.class);

            if (pdfInputStream == null || pdfFileDetail == null || pdfFileDetail.getFileName().isEmpty() || !pdfFileDetail.getFileName().toLowerCase().endsWith(".pdf")) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"El archivo PDF firmado es obligatorio y debe ser un PDF válido.\"}").build();
            }
            if (pdfFileDetail.getSize() > MAX_FILE_SIZE_BYTES) {
                 return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"El PDF excede el tamaño máximo permitido de 10 MB.\"}").build();
            }
            
            if (imagenInputStream == null || imagenFileDetail == null || imagenFileDetail.getFileName().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"La imagen del empleado es obligatoria.\"}").build();
            }
            if (imagenFileDetail.getSize() > MAX_FILE_SIZE_BYTES) {
                 return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"La Imagen excede el tamaño máximo permitido de 10 MB.\"}").build();
            }
            
            pdfTempFile = File.createTempFile("pdf_contrato_", "_" + pdfFileDetail.getFileName());
            copyStreamToFile(pdfInputStream, pdfTempFile);
            
            imagenTempFile = File.createTempFile("img_empleado_", "_" + imagenFileDetail.getFileName());
            copyStreamToFile(imagenInputStream, imagenTempFile);

            int idGenerado = empleadoService.registrarEmpleadoCompleto(
                    request, 
                    pdfTempFile, 
                    pdfFileDetail,
                    imagenTempFile,
                    imagenFileDetail,
                    idAdmin
            );

            if (idGenerado > 0) {
                String json = String.format("{\"mensaje\": \"Empleado y Cliente registrados exitosamente\", \"idEmpleado\": %d}", idGenerado);
                return Response.status(Response.Status.CREATED).entity(json).build();
            } else {
                 return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"No se pudo registrar el empleado completo. Verifique logs.\"}")
                                .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            String error = String.format("{\"error\": \"Error interno en el servidor: %s\"}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        } finally {
            if (pdfTempFile != null && pdfTempFile.exists()) {
                pdfTempFile.delete();
            }
            if (imagenTempFile != null && imagenTempFile.exists()) {
                imagenTempFile.delete();
            }
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
            @FormDataParam("pdfFirmado") FormDataContentDisposition pdfFileDetail,
            @FormDataParam("imagenEmpleado") InputStream imagenInputStream,
            @FormDataParam("imagenEmpleado") FormDataContentDisposition imagenFileDetail
    ) {
        
        File pdfTempFile = null;
        File imagenTempFile = null;
        EmpleadoExisteCompletoRequest request = null; 

        try {
            if (requestJsonString == null || requestJsonString.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"Datos JSON del empleado obligatorios.\"}").build();
            }
            if (idAdmin <= 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"El ID del administrador (idAdmin) es obligatorio y debe ser mayor a cero.\"}").build();
            }
            
            request = objectMapper.readValue(requestJsonString, EmpleadoExisteCompletoRequest.class);
            
            if (pdfInputStream == null || pdfFileDetail == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"El archivo PDF firmado es obligatorio.\"}").build();
            }
            String pdfFileName = pdfFileDetail.getFileName();
            if (pdfFileName == null || pdfFileName.isEmpty() || !pdfFileName.toLowerCase().endsWith(".pdf")) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"El archivo debe ser de tipo PDF válido.\"}").build();
            }
            long pdfFileSize = pdfFileDetail.getSize();
            if (pdfFileSize > MAX_FILE_SIZE_BYTES) {
                 String errorMessage = String.format("El PDF excede el tamaño máximo permitido de 10 MB. Tamaño actual: %.2f MB", 
                                                    pdfFileSize / (1024.0 * 1024.0));
                 return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"" + errorMessage + "\"}").build();
            }
            
            if (imagenInputStream == null || imagenFileDetail == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"La imagen del empleado es obligatoria.\"}").build();
            }
            String imagenFileName = imagenFileDetail.getFileName();
            if (imagenFileName == null || imagenFileName.isEmpty()) {
                 return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"La imagen del empleado debe tener un nombre de archivo.\"}").build();
            }
            long imagenFileSize = imagenFileDetail.getSize();
            if (imagenFileSize > MAX_FILE_SIZE_BYTES) {
                 String errorMessage = String.format("La Imagen excede el tamaño máximo permitido de 10 MB. Tamaño actual: %.2f MB", 
                                                    imagenFileSize / (1024.0 * 1024.0));
                 return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"" + errorMessage + "\"}").build();
            }
            
            pdfTempFile = File.createTempFile("pdf_contrato_", "_" + pdfFileName);
            copyStreamToFile(pdfInputStream, pdfTempFile);
            
            imagenTempFile = File.createTempFile("img_empleado_", "_" + imagenFileName);
            copyStreamToFile(imagenInputStream, imagenTempFile);

            int idEmpleadoGenerado = empleadoService.registrarEmpleadoExisteCompleto(
                    request, 
                    pdfTempFile, 
                    pdfFileDetail,
                    imagenTempFile,
                    imagenFileDetail,
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
            if (pdfTempFile != null && pdfTempFile.exists()) {
                pdfTempFile.delete();
            }
            if (imagenTempFile != null && imagenTempFile.exists()) {
                imagenTempFile.delete();
            }
        }
    }
}
