package com.restaurant.resource;

import com.restaurant.model.Sucursal;
import com.restaurant.service.SucursalService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

@Path("/sucursales")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SucursalResource {

    private final SucursalService service = new SucursalService();

    @GET
    public Response listar() {
        List<HashMap<String, Object>> lista = service.listarSucursales();
        return Response.ok(lista).build();
    }

    @GET
    @Path("/{id}")
    public Response obtener(@PathParam("id") int id) {
        HashMap<String, Object> suc = service.obtenerSucursalPorId(id);
        if (suc == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Sucursal no encontrada\"}").build();
        }
        return Response.ok(suc).build();
    }

    @POST
    public Response crear(Sucursal s) {
        boolean exito = service.crearSucursal(s);
        HashMap<String, Object> resp = new HashMap<>();
        if (exito) {
            resp.put("status", "success");
            resp.put("mensaje", "Sucursal creada correctamente");
            return Response.ok(resp).build();
        } else {
            resp.put("status", "error");
            resp.put("mensaje", "No se pudo crear la sucursal");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, Sucursal s) {
        s.setIdSucursal(id);
        boolean exito = service.actualizarSucursal(s);
        HashMap<String, Object> resp = new HashMap<>();
        if (exito) {
            resp.put("status", "success");
            resp.put("mensaje", "Sucursal actualizada correctamente");
            return Response.ok(resp).build();
        } else {
            resp.put("status", "error");
            resp.put("mensaje", "No se pudo actualizar la sucursal");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        boolean exito = service.eliminarSucursal(id);
        HashMap<String, Object> resp = new HashMap<>();
        if (exito) {
            resp.put("status", "success");
            resp.put("mensaje", "Sucursal eliminada lógicamente");
            return Response.ok(resp).build();
        } else {
            resp.put("status", "error");
            resp.put("mensaje", "No se pudo eliminar la sucursal");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
        }
    }
}

