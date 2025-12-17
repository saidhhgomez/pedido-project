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
    @Path("/activas")
    public Response listarActivas() {
        List<HashMap<String, Object>> lista = service.listarSucursalesActivas();
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
        return Response.ok(exito).build();
    }
    
    @PUT
    @Path("/{id}/estado")
    public Response cambiarEstado(@PathParam("id") int id, HashMap<String, String> body) {

        String nuevoEstado = body.get("estado");

        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            HashMap<String, Object> resp = new HashMap<>();
            resp.put("status", "error");
            resp.put("mensaje", "El estado es obligatorio");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
        }

        boolean exito = service.cambiarEstado(id, nuevoEstado);

        HashMap<String, Object> resp = new HashMap<>();
        if (exito) {
            resp.put("status", "success");
            resp.put("mensaje", "Estado actualizado correctamente");
            return Response.ok(resp).build();
        } else {
            resp.put("status", "error");
            resp.put("mensaje", "No se pudo actualizar el estado");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        boolean exito = service.eliminarLogico(id);

        HashMap<String, Object> resp = new HashMap<>();

        if (exito) {
            resp.put("status", "success");
            resp.put("mensaje", "Estado de la sucursal cambiado a inactivo");
            return Response.ok(resp).build();
        } else {
            resp.put("status", "error");
            resp.put("mensaje", "No se pudo cambiar el estado de la sucursal");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
        }
    }
    
}



