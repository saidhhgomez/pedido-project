package com.restaurant.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.restaurant.model.EmpleadoCompletoRequest;
import com.restaurant.service.EmpleadoService;

@Path("/empleado")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpleadoResource {
	
	private final EmpleadoService empleadoService = new EmpleadoService();

    @POST
    @Path("/registrar")
    public Response registrarEmpleado(EmpleadoCompletoRequest request) {
        try {
            int idGenerado = empleadoService.registrarEmpleadoCompleto(request);

            if (idGenerado > 0) {
                String json = String.format(
                    "{\"mensaje\": \"Empleado registrado exitosamente\", \"idGenerado\": %d}", 
                    idGenerado
                );
                return Response.status(Response.Status.CREATED).entity(json).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"No se pudo registrar el empleado\"}")
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            String error = String.format("{\"error\": \"Error interno: %s\"}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(error)
                    .build();
        }
    }
}
