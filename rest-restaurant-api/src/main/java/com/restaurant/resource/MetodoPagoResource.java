package com.restaurant.resource;

import java.util.List;

import com.restaurant.model.MetodoPago;
import com.restaurant.service.MetodoPagoService;
import com.restaurant.service.MetodoPagoServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/metodopago")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MetodoPagoResource {

    private MetodoPagoService service = new MetodoPagoServiceImpl();

   @GET
    public Response obtenerTodos() {
        List<MetodoPago> lista = service.obtenerTodos();
        return Response.ok(lista).build();
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        MetodoPago pago = service.obtenerPorId(id);

        if (pago != null) {
            return Response.ok(pago).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"mensaje\":\"Método de pago no encontrado\"}")
                    .build();
        }
    }

    @POST
    public Response agregar(MetodoPago pago) {

        boolean creado = service.agregar(pago);

        if (creado) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\":\"Método de pago agregado correctamente\"}")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\":\"Error al agregar método de pago\"}")
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, MetodoPago pago) {

        boolean actualizado = service.actualizar(id, pago);

        if (actualizado) {
            return Response.ok("{\"mensaje\":\"Método de pago actualizado correctamente\"}").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"mensaje\":\"Método de pago no encontrado o error al actualizar\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {

        boolean eliminado = service.eliminar(id);

        if (eliminado) {
            return Response.ok("{\"mensaje\":\"Método de pago eliminado correctamente\"}").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"mensaje\":\"Método de pago no encontrado o error al eliminar\"}")
                    .build();
        }
    }
}
