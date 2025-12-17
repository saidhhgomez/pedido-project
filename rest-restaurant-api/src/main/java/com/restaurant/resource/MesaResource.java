package com.restaurant.resource;

import com.restaurant.model.Mesa;
import com.restaurant.service.MesaService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

@Path("/mesas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MesaResource {
    private final MesaService mesaService = new MesaService();

    @GET
    @Path("/mesero")
    public Response listarParaMesero() {
        return Response.ok(mesaService.listarParaMesero()).build();
    }

    @GET
    @Path("/gestion")
    public Response listarParaAdmin() {
        return Response.ok(mesaService.listarParaAdmin()).build();
    }

    @POST
    public Response crear(Mesa mesa) {
        HashMap<String, Object> res = mesaService.crear(mesa);
        if ((boolean) res.get("success")) {
            return Response.status(Response.Status.CREATED).entity(res).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, Mesa mesa) {
        mesa.setIdMesa(id);
        HashMap<String, Object> res = mesaService.actualizar(mesa);
        if ((boolean) res.get("success")) {
            return Response.ok(res).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        boolean ok = mesaService.eliminarLogico(id);
        HashMap<String, Object> res = new HashMap<>();
        if (ok) {
            res.put("message", "Mesa desactivada correctamente");
            return Response.ok(res).build();
        }
        res.put("message", "No se pudo desactivar la mesa");
        return Response.status(Response.Status.NOT_FOUND).entity(res).build();
    }
}