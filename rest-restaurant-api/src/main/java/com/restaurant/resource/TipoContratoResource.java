package com.restaurant.resource;

import com.restaurant.model.TipoContrato;
import com.restaurant.service.TipoContratoService;

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
        boolean ok = service.crear(contrato);

        HashMap<String, Object> resp = new HashMap<>();
        if (ok) {
            resp.put("mensaje", "Tipo de contrato creado correctamente");
            return Response.ok(resp).build();
        }

        resp.put("mensaje", "No se pudo crear el tipo de contrato");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, TipoContrato contrato) {
        contrato.setIdTipoContrato(id);

        boolean ok = service.actualizar(contrato);

        HashMap<String, Object> resp = new HashMap<>();
        if (ok) {
            resp.put("mensaje", "Tipo de contrato actualizado correctamente");
            return Response.ok(resp).build();
        }

        resp.put("mensaje", "No se pudo actualizar el tipo de contrato");
        return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        boolean ok = service.eliminar(id);

        HashMap<String, Object> resp = new HashMap<>();
        if (ok) {
            resp.put("mensaje", "Tipo de contrato eliminado correctamente");
            return Response.ok(resp).build();
        }

        resp.put("mensaje", "No se pudo eliminar el tipo de contrato");
        return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
    }
}
