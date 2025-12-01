package com.restaurant.dao;

import com.restaurant.config.DBConnection;
import com.restaurant.model.DetallePedido;

import java.sql.*;

public class DetallePedidoDAO {

    private static final String INSERT =
        "INSERT INTO detalle_pedido (idPedido, idProducto, cantidad, precioUnitario, subtotal, igv, total) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    public boolean agregarDetalle(DetallePedido detalle) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {

            ps.setInt(1, detalle.getIdPedido());
            ps.setInt(2, detalle.getIdProducto());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getPrecioUnitario());
            ps.setDouble(5, detalle.getSubtotal());
            ps.setDouble(6, detalle.getIgv());
            ps.setDouble(7, detalle.getTotal());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


