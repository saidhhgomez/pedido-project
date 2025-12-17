package com.restaurant.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.restaurant.dao.PedidoDAO;

public class PedidoService {
    private PedidoDAO pedidoDAO = new PedidoDAO();

    public int procesarPedidoOnline(HashMap<String, Object> request) throws Exception {
        List<HashMap<String, Object>> detallesRaw = (List<HashMap<String, Object>>) request.get("detalles");
        List<HashMap<String, Object>> detallesProcesados = new ArrayList<>();

        for (HashMap<String, Object> det : detallesRaw) {
            int idProd = (int) det.get("idCatalogo");
            int cant = (int) det.get("cantidad");

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

        String estadoMesa = pedidoDAO.obtenerEstadoMesa(idMesa);
        if (estadoMesa == null) throw new Exception("La mesa con ID " + idMesa + " no existe.");
        if (!estadoMesa.equalsIgnoreCase("disponible")) {
            throw new Exception("La mesa " + idMesa + " no está disponible (Estado actual: " + estadoMesa + ").");
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
}