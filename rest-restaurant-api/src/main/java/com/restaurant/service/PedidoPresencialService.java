package com.restaurant.service;

import com.restaurant.dao.PedidoPresencialDAO;
import com.restaurant.model.PedidoPresencial;
import java.util.List;

public class PedidoPresencialService {

    private PedidoPresencialDAO dao = new PedidoPresencialDAO();

    public boolean registrar(PedidoPresencial pedido) {
        if (pedido == null) return false;

        // Si no viene estado, se establece PENDIENTE
        if (pedido.getEstado() == null || pedido.getEstado().trim().isEmpty()) {
            pedido.setEstado("PENDIENTE");
        }

        return dao.registrar(pedido);
    }

    public List<PedidoPresencial> listar() {
        return dao.listar();
    }

    public List<PedidoPresencial> listarPorEmpleado(int idEmpleado) {
        return dao.listarPorEmpleado(idEmpleado);
    }

    public PedidoPresencial obtenerPorId(int idPedido) {
        return dao.obtenerPorId(idPedido);
    }

    public boolean actualizarEstado(int idPedido, String nuevoEstado) {
        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) return false;
        return dao.actualizarEstado(idPedido, nuevoEstado);
    }

    public boolean cambiarMesa(int idPedido, Integer idMesaNueva) {
        return dao.cambiarMesa(idPedido, idMesaNueva);
    }

    // ======================================================
    //  🔥 NUEVA FIRMA: ahora coincide con el Resource
    // ======================================================
    public boolean actualizarPedido(int idPedido, PedidoPresencial pedido) {
        if (pedido == null) return false;

        // Se fuerza el ID proveniente de la URL
        pedido.setIdPedido(idPedido);

        return dao.actualizarPedido(pedido);
    }
    
    public boolean cambiarFormaPago(int idPedido, Integer idFormaPagoNueva) {
        if (idFormaPagoNueva == null || idFormaPagoNueva <= 0) return false;
        return dao.cambiarFormaPago(idPedido, idFormaPagoNueva);
    }

}
