package com.restaurant.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.restaurant.model.DireccionCliente;
import com.restaurant.service.DireccionClienteService;

@Path("/direccion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DireccionClienteResource {

    private final DireccionClienteService service = new DireccionClienteService();

    @POST
    @Path("/registrar")
    public Response registrar(DireccionCliente d) {
        try {
            service.registrar(d);
            return Response.status(Response.Status.CREATED)
                           .entity("{\"mensaje\": \"Dirección guardada con éxito\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @DELETE
    @Path("/eliminar/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            service.eliminar(id);
            return Response.ok("{\"mensaje\": \"Dirección eliminada correctamente (Lógico)\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtener(@PathParam("id") int id) {
        Map<String, Object> resp = new HashMap<>();

        DireccionCliente d = service.obtenerPorId(id);

        if (d != null) {
            resp.put("status", "ok");
            resp.put("data", d);
        } else {
            resp.put("status", "error");
            resp.put("mensaje", "No existe la dirección con ese ID");
        }

        return Response.ok(resp).build();
    }

    @GET
    @Path("/cliente/{idCliente}")
    public Response listarPorCliente(@PathParam("idCliente") int idCliente) {
        Map<String, Object> resp = new HashMap<>();
        try {
            List<DireccionCliente> lista = service.listarPorCliente(idCliente);
            
            resp.put("status", "ok");
            resp.put("count", lista.size());
            resp.put("data", lista);
            
            if (lista.isEmpty()) {
                resp.put("mensaje", "El cliente no tiene direcciones registradas.");
            }

            return Response.ok(resp).build();
        } catch (Exception e) {
            resp.put("status", "error");
            resp.put("mensaje", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
        }
    }
}