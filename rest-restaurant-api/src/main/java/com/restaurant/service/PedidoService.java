package com.restaurant.service;

<<<<<<< HEAD
import com.restaurant.model.Pedido;

public interface PedidoService {
    int crearPedido(Pedido pedido);
=======
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.restaurant.dao.PedidoDAO;

public class PedidoService {
    private PedidoDAO pedidoDAO = new PedidoDAO();

    public int procesarPedidoOnline(HashMap<String, Object> request) throws Exception {
        if (!request.containsKey("idSucursal") || (int) request.get("idSucursal") <= 0) {
            throw new Exception("Error: Debe seleccionar una sucursal válida para procesar su pedido online.");
        }

        List<HashMap<String, Object>> detallesRaw = (List<HashMap<String, Object>>) request.get("detalles");
        if (detallesRaw == null || detallesRaw.isEmpty()) {
            throw new Exception("El pedido debe contener al menos un plato.");
        }

        List<HashMap<String, Object>> detallesProcesados = new ArrayList<>();

        for (HashMap<String, Object> det : detallesRaw) {
            int idProd = (int) det.get("idCatalogo");
            int cant = (int) det.get("cantidad");

            if (cant <= 0) throw new Exception("La cantidad debe ser mayor a 0.");
            if (cant > 10) throw new Exception("Máximo 10 unidades por plato (ID: " + idProd + ")");

            HashMap<String, Object> platoDB = pedidoDAO.obtenerInfoPlato(idProd);
            if (platoDB == null) throw new Exception("El plato ID " + idProd + " no existe.");
            
            int stockActual = (int) platoDB.get("stock");
            if (stockActual < cant) throw new Exception("Stock insuficiente para: " + platoDB.get("nombre"));

            BigDecimal precio = (BigDecimal) platoDB.get("precio");
            BigDecimal base = precio.multiply(new BigDecimal(cant));
            BigDecimal igv = base.multiply(new BigDecimal("0.18"));
            BigDecimal subtotal = base.add(igv);

            det.put("precioUnitario", precio);
            det.put("igv", igv);
            det.put("subtotal", subtotal);
            detallesProcesados.add(det);
        }

        request.put("idMesa", 1);
        return pedidoDAO.insertarPedidoCompleto(request, detallesProcesados);
    }
    
    public int procesarPedidoPresencial(HashMap<String, Object> request) throws Exception {
        int idMesa = (int) request.get("idMesa");
        int idSucursal = (int) request.get("idSucursal");

        if (!pedidoDAO.validarMesaEnSucursal(idMesa, idSucursal)) {
            throw new Exception("Error de seguridad: La mesa " + idMesa + " no pertenece a la sucursal " + idSucursal);
        }

        String estadoMesa = pedidoDAO.obtenerEstadoMesa(idMesa);
        if (estadoMesa == null) throw new Exception("La mesa con ID " + idMesa + " no existe.");
        if (!estadoMesa.equalsIgnoreCase("disponible") && !estadoMesa.equalsIgnoreCase("activo")) {
            throw new Exception("La mesa " + idMesa + " no está disponible (Estado: " + estadoMesa + ").");
        }

        if (!request.containsKey("idCliente")) request.put("idCliente", 1);
        if (!request.containsKey("idDireccion")) request.put("idDireccion", 1);

        List<HashMap<String, Object>> detallesRaw = (List<HashMap<String, Object>>) request.get("detalles");
        List<HashMap<String, Object>> detallesProcesados = new ArrayList<>();

        for (HashMap<String, Object> det : detallesRaw) {
            int idProd = (int) det.get("idCatalogo");
            int cant = (int) det.get("cantidad");

            if (cant > 10) throw new Exception("Límite de 10 unidades excedido en plato ID: " + idProd);

            HashMap<String, Object> platoDB = pedidoDAO.obtenerInfoPlato(idProd);
            if (platoDB == null) throw new Exception("Plato ID " + idProd + " no encontrado.");
            
            int stockActual = (int) platoDB.get("stock");
            if (stockActual < cant) throw new Exception("No hay stock para: " + platoDB.get("nombre"));

            BigDecimal precio = (BigDecimal) platoDB.get("precio");
            BigDecimal base = precio.multiply(new BigDecimal(cant));
            BigDecimal igv = base.multiply(new BigDecimal("0.18"));
            BigDecimal subtotal = base.add(igv);

            det.put("precioUnitario", precio);
            det.put("igv", igv);
            det.put("subtotal", subtotal);
            detallesProcesados.add(det);
        }

        return pedidoDAO.insertarPedidoPresencial(request, detallesProcesados);
    }
    
    public void agregarPlatosAPedido(int idPedido, List<HashMap<String, Object>> nuevosPlatos) throws Exception {
        String estado = pedidoDAO.obtenerEstadoPedido(idPedido);
        if (estado == null) throw new Exception("El pedido no existe.");
        if (!estado.equals("pendiente")) {
            throw new Exception("No se pueden agregar platos a un pedido " + estado);
        }

        List<HashMap<String, Object>> procesados = new ArrayList<>();

        for (HashMap<String, Object> p : nuevosPlatos) {
            int idPlato = (int) p.get("idCatalogo");
            int cant = (int) p.get("cantidad");

            if (cant > 10) throw new Exception("Máximo 10 unidades por plato.");

            HashMap<String, Object> platoDB = pedidoDAO.obtenerInfoPlato(idPlato);
            if (platoDB == null) throw new Exception("Plato ID " + idPlato + " no existe.");
            
            int stockActual = (int) platoDB.get("stock");
            if (stockActual < cant) throw new Exception("Stock insuficiente para: " + platoDB.get("nombre"));

            BigDecimal precio = (BigDecimal) platoDB.get("precio");
            BigDecimal base = precio.multiply(new BigDecimal(cant));
            BigDecimal igv = base.multiply(new BigDecimal("0.18"));
            BigDecimal subtotal = base.add(igv);

            p.put("precioUnitario", precio);
            p.put("igv", igv);
            p.put("subtotal", subtotal);
            procesados.add(p);
        }

        pedidoDAO.insertarMasDetalles(idPedido, procesados);
    }
    
    public void marcarMesaParaLimpieza(int idPedido, int idMesa) throws Exception {
        pedidoDAO.finalizarPedido(idPedido);
        pedidoDAO.cambiarEstadoMesa(idMesa, "liberando");
    }

    public void habilitarMesa(int idMesa) throws Exception {
        String estadoActual = pedidoDAO.obtenerEstadoMesa(idMesa);
        if (!"liberando".equalsIgnoreCase(estadoActual)) {
            throw new Exception("La mesa no está en proceso de limpieza.");
        }
        pedidoDAO.cambiarEstadoMesa(idMesa, "disponible");
    }
    
    public HashMap<String, Object> obtenerPedidoPorMesa(int idMesa) throws Exception {
        HashMap<String, Object> pedido = pedidoDAO.obtenerPedidoActivoPorMesa(idMesa);
        if (pedido == null) {
            throw new Exception("No hay un pedido pendiente para la mesa ID: " + idMesa);
        }
        return pedido;
    }
    
    public void procesarCambioMesa(HashMap<String, Object> request) throws Exception {
        int idPedido = (int) request.get("idPedido");
        int idMesaOrigen = (int) request.get("idMesaOrigen");
        int idMesaDestino = (int) request.get("idMesaDestino");

        String estadoPedido = pedidoDAO.obtenerEstadoPedido(idPedido);
        if (estadoPedido == null) throw new Exception("El pedido " + idPedido + " no existe.");
        if (!estadoPedido.equals("pendiente")) {
            throw new Exception("Solo se pueden cambiar mesas de pedidos pendientes.");
        }

        String estadoMesaDestino = pedidoDAO.obtenerEstadoMesa(idMesaDestino);
        if (estadoMesaDestino == null) throw new Exception("La mesa destino no existe.");
        if (!estadoMesaDestino.equals("disponible")) {
            throw new Exception("La mesa destino está " + estadoMesaDestino + ". Elija una disponible.");
        }

        pedidoDAO.ejecutarCambioMesa(idPedido, idMesaOrigen, idMesaDestino);
    }
    
    public void cancelarPedidoTotal(int idPedido) throws Exception {
        HashMap<String, Object> info = pedidoDAO.obtenerInfoBasicaPedido(idPedido);
        
        if (info == null) {
            throw new Exception("El pedido con ID " + idPedido + " no existe.");
        }

        String estadoActual = (String) info.get("estado");
        Integer idMesa = (Integer) info.get("idMesa");

        if ("finalizado".equals(estadoActual) || "pagado".equals(estadoActual)) {
            throw new Exception("No se puede cancelar un pedido que ya ha sido finalizado o pagado.");
        }
        
        if ("cancelado".equals(estadoActual)) {
            throw new Exception("El pedido ya se encuentra cancelado.");
        }

        pedidoDAO.cancelarPedidoCompleto(idPedido, idMesa);
    }
    
    public void cancelarPlatoEspecifico(int idPedido, int idDetalle) throws Exception {
        HashMap<String, Object> info = pedidoDAO.obtenerInfoBasicaPedido(idPedido);

        if (info == null) {
            throw new Exception("El pedido no existe.");
        }

        String estado = (String) info.get("estado");

        if (!"pendiente".equalsIgnoreCase(estado)) {
            throw new Exception("No se puede eliminar el plato. El pedido está: " + estado);
        }

        pedidoDAO.eliminarPlatoDetalle(idDetalle);
    }
    
    public List<HashMap<String, Object>> listarHistorialEmpleado(int idEmpleado) throws Exception {
        List<HashMap<String, Object>> lista = pedidoDAO.obtenerHistorialPorEmpleado(idEmpleado);
        if (lista.isEmpty()) {
            throw new Exception("El empleado no tiene pedidos registrados.");
        }
        return lista;
    }
    
    public List<HashMap<String, Object>> listarHistorialMesa(int idMesa) throws Exception {
        List<HashMap<String, Object>> historial = pedidoDAO.obtenerHistorialPorMesa(idMesa);
        if (historial.isEmpty()) {
            throw new Exception("No hay registros de consumo para la mesa ID: " + idMesa);
        }
        return historial;
    }
    
    public List<HashMap<String, Object>> listarPedidosOnlineActivos(int idSucursal) throws Exception {
        List<HashMap<String, Object>> lista = pedidoDAO.obtenerPedidosOnlineActivosPorSucursal(idSucursal);
        
        if (lista.isEmpty()) {
            throw new Exception("No hay pedidos online pendientes o en camino para esta sucursal.");
        }
        
        return lista;
    }
    
    public void procesarAsignacionRepartidor(int idPedido, int idEmpleado) throws Exception {
        HashMap<String, Object> pedido = pedidoDAO.obtenerInfoBasicaPedido(idPedido);
        
        if (pedido == null) throw new Exception("El pedido no existe.");
        
        int idSucursalPedido = (int) pedido.get("idSucursal");
        String estadoActual = (String) pedido.get("estado");
        int idMesa = (int) pedido.get("idMesa");

        if (idMesa != 1) {
            throw new Exception("Solo se puede asignar repartidor a pedidos Online.");
        }

        if (!estadoActual.equalsIgnoreCase("pendiente")) {
            throw new Exception("El pedido no se puede asignar porque está en estado: " + estadoActual);
        }

        if (!pedidoDAO.esRepartidorDeSucursal(idEmpleado, idSucursalPedido)) {
            throw new Exception("El empleado no es un repartidor activo en la sucursal del pedido (Sucursal ID: " + idSucursalPedido + ")");
        }

        pedidoDAO.asignarRepartidorYEstado(idPedido, idEmpleado);
    }
    
    public void finalizarPedidoOnline(int idPedido) throws Exception {
        HashMap<String, Object> pedido = pedidoDAO.obtenerInfoBasicaPedido(idPedido);
        
        if (pedido == null) {
            throw new Exception("El pedido con ID " + idPedido + " no existe.");
        }
        
        if (!pedido.get("idMesa").toString().equals("1")) {
            throw new Exception("Este método solo es para pedidos Online.");
        }

        String estado = (String) pedido.get("estado");
        if (estado.equalsIgnoreCase("finalizado")) {
            throw new Exception("El pedido ya fue entregado y finalizado anteriormente.");
        }
        if (!estado.equalsIgnoreCase("en camino")) {
            throw new Exception("No se puede finalizar un pedido que no ha sido enviado (Estado actual: " + estado + ").");
        }

        pedidoDAO.finalizarPedido(idPedido);
    }
    
    public List<HashMap<String, Object>> obtenerHistorialSucursal(int idSucursal, String desde, String hasta) throws Exception {
        List<HashMap<String, Object>> lista = pedidoDAO.listarHistorialSucursal(idSucursal, desde, hasta);
        
        if (lista.isEmpty()) {
            throw new Exception("No se encontraron pedidos en el historial.");
        }
        return lista;
    }

    public List<HashMap<String, Object>> obtenerHistorialRepartidor(int idEmpleado) throws Exception {
        List<HashMap<String, Object>> lista = pedidoDAO.listarHistorialRepartidor(idEmpleado);
        
        if (lista.isEmpty()) {
            throw new Exception("El repartidor aún no tiene pedidos finalizados en su historial.");
        }
        return lista;
    }
    
    public List<HashMap<String, Object>> obtenerPendientesCocina(int idSucursal) throws Exception {
        return pedidoDAO.listarPlatosPendientesCocina(idSucursal);
    }

    public void tomarPlatoParaPreparar(int idDetallePedido, int idEmpleado) throws Exception {
        int idPedido = pedidoDAO.obtenerIdPedidoDeDetalle(idDetallePedido);
        if (idPedido == 0) throw new Exception("Plato no encontrado.");

        HashMap<String, Object> info = pedidoDAO.obtenerInfoBasicaPedido(idPedido);
        int idSucursalPedido = (int) info.get("idSucursal");
        String estado = (String) info.get("estado");

        if (estado.equalsIgnoreCase("finalizado") || estado.equalsIgnoreCase("cancelado")) {
            throw new Exception("No puedes tomar un plato de un pedido " + estado);
        }

        if (!pedidoDAO.esCocineroDeSucursal(idEmpleado, idSucursalPedido)) {
            throw new Exception("Error de seguridad: No eres cocinero activo en esta sucursal.");
        }

        pedidoDAO.asignarCocineroADetalle(idDetallePedido, idEmpleado);
    }
    
    public List<HashMap<String, Object>> obtenerHistorialCocinero(int idEmpleado) throws Exception {
        List<HashMap<String, Object>> lista = pedidoDAO.listarHistorialPlatosCocinero(idEmpleado);
        
        if (lista.isEmpty()) {
            throw new Exception("El cocinero aún no tiene platos registrados en su historial.");
        }
        return lista;
    }
    
    public List<HashMap<String, Object>> obtenerHistorialCliente(int idCliente) throws Exception {
        List<HashMap<String, Object>> historial = pedidoDAO.listarPedidosPorCliente(idCliente);
        if (historial.isEmpty()) {
            throw new Exception("Aún no has realizado ningún pedido.");
        }
        return historial;
    }
>>>>>>> develop
}