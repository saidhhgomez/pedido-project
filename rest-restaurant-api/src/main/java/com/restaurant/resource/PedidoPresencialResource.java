package com.restaurant.resource;

import com.restaurant.model.PedidoPresencial;
import com.restaurant.service.PedidoPresencialService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/pedido-presencial")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoPresencialResource {

    private PedidoPresencialService service = new PedidoPresencialService();

    // ============================
    // 1. Registrar pedido presencial
    // ============================
    @POST
    @Path("/registrar")
    public Response registrar(PedidoPresencial pedido) {

        if (pedido == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Datos incompletos\"}")
                    .build();
        }

        boolean registrado = service.registrar(pedido);

        if (registrado) {
            return Response.ok("{\"mensaje\":\"Pedido registrado correctamente\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"No se pudo registrar el pedido\"}")
                    .build();
        }
    }

    // ============================
    // 2. Listar todos
    // ============================
    @GET
    @Path("/listar")
    public Response listar() {
        List<PedidoPresencial> lista = service.listar();
        return Response.ok(lista).build();
    }

    // ============================
    // 3. Listar por empleado
    // ============================
    @GET
    @Path("/empleado/{idEmpleado}")
    public Response listarPorEmpleado(@PathParam("idEmpleado") int idEmpleado) {
        List<PedidoPresencial> lista = service.listarPorEmpleado(idEmpleado);
        return Response.ok(lista).build();
    }

    // ============================
    // 4. Obtener por ID
    // ============================
    @GET
    @Path("/{idPedido}")
    public Response obtener(@PathParam("idPedido") int idPedido) {

        PedidoPresencial pedido = service.obtenerPorId(idPedido);

        if (pedido != null) {
            return Response.ok(pedido).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Pedido no encontrado\"}")
                    .build();
        }
    }

    // ============================
    // 5. Actualizar SOLO estado
    // ============================
    @PUT
    @Path("/estado/{idPedido}")
    public Response actualizarEstado(
            @PathParam("idPedido") int idPedido,
            PedidoPresencial pedido
    ) {

        if (pedido.getEstado() == null || pedido.getEstado().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Estado inválido\"}")
                    .build();
        }

        boolean actualizado = service.actualizarEstado(idPedido, pedido.getEstado());

        if (actualizado) {
            return Response.ok("{\"mensaje\":\"Estado actualizado correctamente\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"No se pudo actualizar el estado\"}")
                    .build();
        }
    }

    // ============================
    // 6. Cambiar mesa
    // ============================
    @PUT
    @Path("/cambiar-mesa/{idPedido}")
    public Response cambiarMesa(
            @PathParam("idPedido") int idPedido,
            PedidoPresencial pedido
    ) {

        boolean cambiado = service.cambiarMesa(idPedido, pedido.getIdMesa());

        if (cambiado) {
            return Response.ok("{\"mensaje\":\"Mesa cambiada correctamente\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"No se pudo cambiar la mesa\"}")
                    .build();
        }
    }

    // ============================
    // 7. Actualizar un pedido COMPLETO
    // ============================
    @PUT
    @Path("/actualizar/{idPedido}")
    public Response actualizarPedido(
            @PathParam("idPedido") int idPedido,
            PedidoPresencial pedido
    ) {

        if (pedido == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Datos inválidos\"}")
                    .build();
        }

        boolean actualizado = service.actualizarPedido(idPedido, pedido);

        if (actualizado) {
            return Response.ok("{\"mensaje\":\"Pedido actualizado correctamente\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"No se pudo actualizar el pedido\"}")
                    .build();}
        }
     // ============================
     // Cambiar forma de pago (JSON)
     // ============================
     @PUT
     @Path("/cambiar-forma-pago/{idPedido}")
     public Response cambiarFormaPago(
             @PathParam("idPedido") int idPedido,
             PedidoPresencial pedido
     ) {

         boolean cambiado = service.cambiarFormaPago(idPedido, pedido.getIdFormaPago());

         if (cambiado) {
             return Response.ok("{\"mensaje\":\"Forma de pago cambiada correctamente\"}").build();
         } else {
             return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                     .entity("{\"error\":\"No se pudo cambiar la forma de pago\"}")
                     .build();
         }
     }
    }

  