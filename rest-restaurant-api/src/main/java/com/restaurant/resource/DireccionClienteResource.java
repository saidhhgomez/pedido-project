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
        Map<String, Object> resp = new HashMap<>();

        boolean ok = service.registrar(d);

        resp.put("status", ok ? "ok" : "error");
        resp.put("mensaje", ok ? "Dirección registrada correctamente" : "No se pudo registrar la dirección");

        return Response.ok(resp).build();
    }

    @DELETE
    @Path("/eliminar/{id}")
    public Response eliminar(@PathParam("id") int id) {
        Map<String, Object> resp = new HashMap<>();

        boolean ok = service.eliminar(id);

        resp.put("status", ok ? "ok" : "error");
        resp.put("mensaje", ok ? "Dirección eliminada" : "No se pudo eliminar");

        return Response.ok(resp).build();
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

        List<DireccionCliente> lista = service.listarPorCliente(idCliente);

        resp.put("status", "ok");
        resp.put("data", lista);

        return Response.ok(resp).build();
    }
}