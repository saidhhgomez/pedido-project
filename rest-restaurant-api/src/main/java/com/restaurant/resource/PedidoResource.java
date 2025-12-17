package com.restaurant.resource;

import com.restaurant.model.Pedido;
import com.restaurant.service.PedidoServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/pedido")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PedidoResource {

    private PedidoServiceImpl service = new PedidoServiceImpl();

    @POST
    public Response crearPedido(Pedido pedido) {

        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El pedido debe incluir al menos un detalle\"}")
                    .build();
        }

        int idPedido = service.crearPedido(pedido);

        if (idPedido > 0) {
            return Response.ok("{\"mensaje\":\"Pedido registrado correctamente\", \"idPedido\":" + idPedido + "}")
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al registrar el pedido\"}")
                    .build();
        }
    }
}


