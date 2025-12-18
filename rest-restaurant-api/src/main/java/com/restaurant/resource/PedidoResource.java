package com.restaurant.resource;

import com.restaurant.service.PedidoService;

import java.util.HashMap;
import java.util.List;

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
    public Response registrarPresencial(HashMap<String, Object> request) {
        try {
            int id = service.procesarPedidoPresencial(request);
            return Response.status(201).entity("{\"mensaje\":\"Pedido en mesa registrado\", \"idPedido\":" + id + "}").build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
    
    @POST
    @Path("/detalles/{idPedido}")
    public Response agregarDetalles(@PathParam("idPedido") int idPedido, List<HashMap<String, Object>> nuevosDetalles) {
        try {
            service.agregarPlatosAPedido(idPedido, nuevosDetalles);
            return Response.ok("{\"mensaje\": \"Platos agregados correctamente\"}").build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
    
    @PUT
    @Path("/cancelar/{idPedido}")
    public Response cancelarPedido(@PathParam("idPedido") int idPedido) {
        try {
            service.cancelarPedidoTotal(idPedido);
            return Response.ok("{\"mensaje\": \"Pedido cancelado con éxito y stock devuelto al catálogo.\"}").build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
    
    @PUT
    @Path("/{idPedido}/mesa/{idMesa}/liberar")
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
    public Response getPedidoActivo(@PathParam("idMesa") int idMesa) {
        try {
            HashMap<String, Object> pedido = service.obtenerPedidoPorMesa(idMesa);
            return Response.ok(pedido).build();
        } catch (Exception e) {
            return Response.status(404).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
    
    @PUT
    @Path("/cambio-mesa")
    public Response cambiarMesa(HashMap<String, Object> request) {
        try {
            service.procesarCambioMesa(request);
            return Response.ok("{\"mensaje\": \"Cambio de mesa realizado con éxito\"}").build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
    
    @DELETE
    @Path("/{idPedido}/detalle/{idDetalle}") 
    public Response eliminarDetalle(@PathParam("idPedido") int idPedido, @PathParam("idDetalle") int idDetalle) {
        try {
            service.cancelarPlatoEspecifico(idPedido, idDetalle);
            return Response.ok("{\"mensaje\": \"Plato eliminado y stock actualizado\"}").build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
    
    @GET
    @Path("/empleado/{idEmpleado}/historial")
    public Response getHistorial(@PathParam("idEmpleado") int idEmpleado) {
        try {
            List<HashMap<String, Object>> historial = service.listarHistorialEmpleado(idEmpleado);
            return Response.ok(historial).build();
        } catch (Exception e) {
            return Response.status(404).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
    
    @GET
    @Path("/mesa/{idMesa}/historial")
    public Response getHistorialMesa(@PathParam("idMesa") int idMesa) {
        try {
            List<HashMap<String, Object>> historial = service.listarHistorialMesa(idMesa);
            return Response.ok(historial).build();
        } catch (Exception e) {
            return Response.status(404).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
    
    @GET
    @Path("/online/activos/{idSucursal}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOnlineActivos(@PathParam("idSucursal") int idSucursal) {
        try {
            List<HashMap<String, Object>> lista = service.listarPedidosOnlineActivos(idSucursal);
            return Response.ok(lista).build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
    
    @PUT
    @Path("/{idPedido}/asignar-repartidor/{idEmpleado}")
    public Response asignarRepartidor(@PathParam("idPedido") int idPedido, 
                                     @PathParam("idEmpleado") int idEmpleado) {
        try {
            service.procesarAsignacionRepartidor(idPedido, idEmpleado);
            return Response.ok("{\"mensaje\": \"Pedido actualizado con éxito. El repartidor ya está en camino.\"}").build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
    
    @PUT
    @Path("/online/{idPedido}/finalizar")
    public Response finalizarPedido(@PathParam("idPedido") int idPedido) {
        try {
            service.finalizarPedidoOnline(idPedido);
            return Response.ok("{\"mensaje\": \"Pedido Online finalizado exitosamente.\"}").build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
    
    @GET
    @Path("/historial/sucursal/{idSucursal}")
    public Response getHistorialSucursal(
            @PathParam("idSucursal") int idSucursal,
            @QueryParam("desde") String desde,
            @QueryParam("hasta") String hasta) {
        try {
            List<HashMap<String, Object>> lista = service.obtenerHistorialSucursal(idSucursal, desde, hasta);
            return Response.ok(lista).build();
        } catch (Exception e) {
            return Response.status(404).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/historial/repartidor/{idEmpleado}")
    public Response getHistorialRepartidor(@PathParam("idEmpleado") int idEmpleado) {
        try {
            List<HashMap<String, Object>> lista = service.obtenerHistorialRepartidor(idEmpleado);
            return Response.ok(lista).build();
        } catch (Exception e) {
            return Response.status(404).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
    
    @GET
    @Path("/cocina/pendientes/{idSucursal}")
    public Response getPendientesCocina(@PathParam("idSucursal") int idSucursal) {
        try {
            return Response.ok(service.obtenerPendientesCocina(idSucursal)).build();
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/cocina/tomar-plato/{idDetallePedido}/{idEmpleado}")
    public Response asignarCocinero(@PathParam("idDetallePedido") int idDetallePedido, 
                                    @PathParam("idEmpleado") int idEmpleado) {
        try {
            service.tomarPlatoParaPreparar(idDetallePedido, idEmpleado);
            return Response.ok("{\"mensaje\": \"Plato tomado. ¡A cocinar!\"}").build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
    
    @GET
    @Path("/cocina/historial/{idEmpleado}")
    public Response getHistorialCocinero(@PathParam("idEmpleado") int idEmpleado) {
        try {
            List<HashMap<String, Object>> historial = service.obtenerHistorialCocinero(idEmpleado);
            return Response.ok(historial).build();
        } catch (Exception e) {
            return Response.status(404).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
    
    @GET
    @Path("/cliente/mis-pedidos/{idCliente}")
    public Response getPedidosCliente(@PathParam("idCliente") int idCliente) {
        try {
            return Response.ok(service.obtenerHistorialCliente(idCliente)).build();
        } catch (Exception e) {
            return Response.status(404).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
}