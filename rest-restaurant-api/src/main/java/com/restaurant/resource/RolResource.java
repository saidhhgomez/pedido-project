package com.restaurant.resource;

import com.restaurant.model.Rol;
import com.restaurant.service.RolService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RolResource {

    private final RolService service = new RolService();

    @GET
    public Response listarRoles() {
        List<HashMap<String, Object>> lista = service.listarRoles();
        return Response.ok(lista).build();
    }

    @GET
    @Path("/activos")
    public Response listarActivos() {
        List<HashMap<String, Object>> lista = service.listarActivos();
        return Response.ok(lista).build();
    }

    @GET
    @Path("/{id}")
    public Response obtenerRol(@PathParam("id") int id) {
        HashMap<String, Object> rol = service.obtenerRolPorId(id);
        if (rol == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Rol no encontrado\"}").build();
        }
        return Response.ok(rol).build();
    }

    @POST
    public Response crearRol(Rol rol) {
        HashMap<String, Object> resp = new HashMap<>();
        try {
            boolean ok = service.crearRol(rol);
            if (ok) {
                resp.put("status", "success");
                resp.put("mensaje", "Rol creado correctamente");
                return Response.ok(resp).build();
            }
        } catch (IllegalArgumentException e) {
            resp.put("status", "error");
            resp.put("mensaje", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
        }
        resp.put("status", "error");
        resp.put("mensaje", "No se pudo crear el rol");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizarRol(@PathParam("id") int id, Rol rol) {
        HashMap<String, Object> resp = new HashMap<>();
        rol.setIdRol(id);
        try {
            boolean ok = service.actualizarRol(rol);
            if (ok) {
                resp.put("status", "success");
                resp.put("mensaje", "Rol actualizado correctamente");
                return Response.ok(resp).build();
            }
        } catch (IllegalArgumentException e) {
            resp.put("status", "error");
            resp.put("mensaje", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
        }
        resp.put("status", "error");
        resp.put("mensaje", "No se pudo actualizar el rol");
        return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
    }

    @PUT
    @Path("/{id}/estado")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cambiarEstado(@PathParam("id") int id, HashMap<String, String> body) {
        String nuevoEstado = body.get("estado");
        HashMap<String, Object> resp = new HashMap<>();

        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            resp.put("status", "error");
            resp.put("mensaje", "El estado es obligatorio");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
        }

        boolean exito = service.cambiarEstado(id, nuevoEstado);
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
    public Response eliminarRol(@PathParam("id") int id) {
        boolean exito = service.cambiarEstado(id, "inactivo");
        HashMap<String, Object> resp = new HashMap<>();

        if (exito) {
            resp.put("status", "success");
            resp.put("mensaje", "Rol cambiado a inactivo");
            return Response.ok(resp).build();
        } else {
            resp.put("status", "error");
            resp.put("mensaje", "No se pudo cambiar el estado del rol");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
        }
    }
}
