package com.restaurant.resource;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.restaurant.model.EmpleadoCompletoRequest;
import com.restaurant.model.EmpleadoExisteCompletoRequest;
import com.restaurant.service.EmpleadoService;

@Path("/empleado")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpleadoResource {
	
	private final EmpleadoService empleadoService = new EmpleadoService();

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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarEmpleadoExisteCompleto(EmpleadoExisteCompletoRequest request) {

        try {
            int result = empleadoService.registrarEmpleadoExisteCompleto(request);

            if (result > 0) {
                return Response.ok()
                        .entity("{\"message\":\"Empleado y contrato registrados correctamente\"}")
                        .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\":\"No se pudo registrar el empleado y contrato\"}")
                        .build();
            }

        } catch (Exception e) {
            System.out.println("Error en EmpleadoResource: " + e.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Error interno del servidor\"}")
                    .build();
        }
    }
}
