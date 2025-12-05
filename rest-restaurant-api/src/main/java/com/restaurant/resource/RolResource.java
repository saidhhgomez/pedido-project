package com.restaurant.resource;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.restaurant.model.Rol;
import com.restaurant.service.RolService;

@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RolResource {
    private final RolService rolService = new RolService();

    @GET
    public Response listarRoles() {
        List<HashMap<String, Object>> lista = rolService.listarRoles();
        return Response.ok(lista).build();
    }

    @GET
    @Path("/{id}")
    public Response obtenerRol(@PathParam("id") int id) {
        HashMap<String, Object> rol = rolService.obtenerRolPorId(id);
        if (rol == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Rol no encontrado\"}").build();
        }
        return Response.ok(rol).build();
    }

    @POST
    public Response crearRol(Rol rol) {
        boolean exito = rolService.crearRol(rol);

        if (exito) {
            HashMap<String, Object> resp = new HashMap<>();
            resp.put("status", "success");
            resp.put("mensaje", "Rol creado correctamente");
            return Response.ok(resp).build();
        } else {
            HashMap<String, Object> resp = new HashMap<>();
            resp.put("status", "error");
            resp.put("mensaje", "No se pudo crear el rol");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizarRol(@PathParam("id") int id, Rol rol) {
        rol.setIdRol(id);
        boolean actualizado = rolService.actualizarRol(rol);
        if (actualizado) {
            HashMap<String, Object> resp = new HashMap<>();
            resp.put("status", "success");
            resp.put("mensaje", "Rol actualizado correctamente");
            return Response.ok(resp).build();
        } else {
            HashMap<String, Object> resp = new HashMap<>();
            resp.put("status", "error");
            resp.put("mensaje", "No se pudo actualizar el rol");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarRol(@PathParam("id") int id) {
        boolean eliminado = rolService.eliminarRol(id);
        if (eliminado) {
            HashMap<String, Object> resp = new HashMap<>();
            resp.put("status", "success");
            resp.put("mensaje", "Rol eliminado correctamente");
            return Response.ok(resp).build();
        } else {
            HashMap<String, Object> resp = new HashMap<>();
            resp.put("status", "error");
            resp.put("mensaje", "No se pudo eliminar el rol");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
        }
    }
}
