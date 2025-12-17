package com.restaurant.resource;

import com.restaurant.service.PedidoService;

import java.util.HashMap;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/pedido")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PedidoResource {

    private PedidoService service = new PedidoService();

    @POST
    @Path("/online")
    public Response registrarOnline(HashMap<String, Object> request) {
        try {
            int id = service.procesarPedidoOnline(request);
            return Response.status(201).entity("{\"mensaje\":\"Pedido exitoso\", \"id\":" + id + "}").build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
    
    @POST
    @Path("/presencial")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarPresencial(HashMap<String, Object> request) {
        try {
            int id = service.procesarPedidoPresencial(request);
            return Response.status(201).entity("{\"mensaje\":\"Pedido en mesa registrado\", \"idPedido\":" + id + "}").build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
    
    @PUT
    @Path("/{idPedido}/mesa/{idMesa}/liberar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response liberarMesa(@PathParam("idPedido") int idPedido, @PathParam("idMesa") int idMesa) {
        try {
            service.marcarMesaParaLimpieza(idPedido, idMesa);
            return Response.ok("{\"mensaje\": \"Mesa en proceso de limpieza (liberando)\"}").build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @PUT
    @Path("/mesa/{idMesa}/habilitar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response habilitarMesa(@PathParam("idMesa") int idMesa) {
        try {
            service.habilitarMesa(idMesa);
            return Response.ok("{\"mensaje\": \"Mesa habilitada y disponible\"}").build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
    
    @GET
    @Path("/activo/{idMesa}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPedidoActivo(@PathParam("idMesa") int idMesa) {
        try {
            HashMap<String, Object> pedido = service.obtenerPedidoPorMesa(idMesa);
            return Response.ok(pedido).build();
        } catch (Exception e) {
            return Response.status(404).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}


