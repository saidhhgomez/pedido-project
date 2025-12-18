package com.restaurant.resource;

import com.restaurant.model.Mesa;
import com.restaurant.service.MesaService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

@Path("/mesas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MesaResource {
    private final MesaService mesaService = new MesaService();

    @GET
    @Path("/mesero/{idSucursal}")
    public Response listarParaMesero(@PathParam("idSucursal") int idSucursal) {
        try {
            List<HashMap<String, Object>> mesas = mesaService.listarParaMesero(idSucursal);
            return Response.ok(mesas).build();
        } catch (Exception e) {
            // Devolvemos 404 (Not Found) con el mensaje de "No hay mesas"
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\": \"" + e.getMessage() + "\"}")
                           .build();
        }
    }

    @GET
    @Path("/admin/{idSucursal}")
    public Response listarParaAdmin(@PathParam("idSucursal") int idSucursal) {
        try {
            List<HashMap<String, Object>> mesas = mesaService.listarParaAdmin(idSucursal);
            return Response.ok(mesas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\": \"" + e.getMessage() + "\"}")
                           .build();
        }
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