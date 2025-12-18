package com.restaurant.resource;

import com.restaurant.model.TipoContrato;
import com.restaurant.service.TipoContratoService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

@Path("/tipocontrato")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TipoContratoResource {

    private final TipoContratoService service = new TipoContratoService();

    @GET
    public Response listar() {
        List<HashMap<String, Object>> lista = service.listar();
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
    public Response obtener(@PathParam("id") int id) {
        HashMap<String, Object> contrato = service.obtener(id);
        if (contrato == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Tipo de contrato no encontrado\"}")
                    .build();
        }
        return Response.ok(contrato).build();
    }

    @POST
    public Response crear(TipoContrato contrato) {
        HashMap<String, Object> resp = new HashMap<>();
        try {
            boolean ok = service.crear(contrato);
            if (ok) {
                resp.put("status", "success");
                resp.put("mensaje", "Tipo de contrato creado correctamente");
                return Response.ok(resp).build();
            }
        } catch (IllegalArgumentException e) {
            resp.put("status", "error");
            resp.put("mensaje", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
        }
        resp.put("status", "error");
        resp.put("mensaje", "No se pudo crear el tipo de contrato");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, TipoContrato contrato) {
        HashMap<String, Object> resp = new HashMap<>();
        contrato.setIdTipoContrato(id);
        try {
            boolean ok = service.actualizar(contrato);
            if (ok) {
                resp.put("status", "success");
                resp.put("mensaje", "Tipo de contrato actualizado correctamente");
                return Response.ok(resp).build();
            }
        } catch (IllegalArgumentException e) {
            resp.put("status", "error");
            resp.put("mensaje", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
        }
        resp.put("status", "error");
        resp.put("mensaje", "No se pudo actualizar el tipo de contrato");
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
    public Response eliminar(@PathParam("id") int id) {
        boolean exito = service.cambiarEstado(id, "inactivo");
        HashMap<String, Object> resp = new HashMap<>();

        if (exito) {
            resp.put("status", "success");
            resp.put("mensaje", "Tipo de contrato cambiado a inactivo");
            return Response.ok(resp).build();
        } else {
            resp.put("status", "error");
            resp.put("mensaje", "No se pudo cambiar el estado del tipo de contrato");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
        }
    }
}

