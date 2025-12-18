package com.restaurant.resource;

import com.restaurant.model.MetodoPago;
import com.restaurant.service.MetodoPagoService;
import com.restaurant.service.MetodoPagoServiceImpl;

import java.util.HashMap;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Path("/metodopago")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MetodoPagoResource {

    private MetodoPagoService service = new MetodoPagoServiceImpl();

    // TODOS
    @GET
    public Response obtenerTodos() {
        return Response.ok(service.obtenerTodos()).build();
    }

    // SOLO ACTIVOS
    @GET
    @Path("/activos")
    public Response obtenerActivos() {
        return Response.ok(service.obtenerActivos()).build();
    }

    // POR ID
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        MetodoPago mp = service.obtenerPorId(id);

        if (mp == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"mensaje\":\"Método de pago no encontrado\"}")
                    .build();
        }
        return Response.ok(mp).build();
    }

    // CREAR
    @POST
    public Response agregar(MetodoPago pago) {
        String r = service.agregar(pago);

        if ("DUPLICADO".equals(r)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\":\"La forma de pago ya existe\"}")
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity("{\"mensaje\":\"Forma de pago registrada correctamente\"}")
                .build();
    }

    // ACTUALIZAR
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, MetodoPago pago) {

        pago.setIdFormaPago(id); // 🔥 ESTA LÍNEA FALTABA

        boolean actualizado = service.actualizar(id, pago);

        if (actualizado) {
            return Response.ok("{\"mensaje\":\"Método de pago actualizado correctamente\"}").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\":\"No se pudo actualizar el método de pago\"}")
                    .build();
        }
    }


    // BOTÓN ACTIVO / INACTIVO
    @PUT
    @Path("/{id}/estado")
    public Response cambiarEstado(@PathParam("id") int id, HashMap<String, String> body) {

        String estado = body.get("estado");

        if (estado == null || estado.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\":\"El estado es obligatorio\"}")
                    .build();
        }

        boolean ok = service.cambiarEstado(id, estado);

        if (ok) {
            return Response.ok("{\"mensaje\":\"Estado actualizado correctamente\"}").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\":\"No se pudo cambiar el estado\"}")
                    .build();
        }
    }


    // DELETE LÓGICO
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        return service.eliminarLogico(id)
                ? Response.ok("{\"mensaje\":\"Forma de pago inactivada\"}").build()
                : Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"mensaje\":\"No se pudo inactivar\"}")
                .build();
    }
}