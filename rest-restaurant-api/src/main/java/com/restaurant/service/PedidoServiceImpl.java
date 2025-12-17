package com.restaurant.service;

import com.restaurant.dao.PedidoDAO;
import com.restaurant.dao.DetallePedidoDAO;
import com.restaurant.model.DetallePedido;
import com.restaurant.model.Pedido;

public class PedidoServiceImpl implements PedidoService {

    private PedidoDAO pedidoDAO = new PedidoDAO();
    private DetallePedidoDAO detalleDAO = new DetallePedidoDAO();

    @Override
    public int crearPedido(Pedido pedido) {
        try {
            int idPedido = pedidoDAO.registrarPedido(pedido);

            if (idPedido == -1) return -1;

            for (DetallePedido d : pedido.getDetalles()) {
                d.setIdPedido(idPedido);
                detalleDAO.agregarDetalle(d);
            }

            return idPedido;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
