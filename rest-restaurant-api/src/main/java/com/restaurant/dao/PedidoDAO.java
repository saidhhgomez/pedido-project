package com.restaurant.dao;

import java.sql.*;
import com.restaurant.model.Pedido;
import com.restaurant.config.DBConnection;

public class PedidoDAO {

    private static final String INSERT =
        "INSERT INTO pedido (idCliente, idDireccion, idEmpleado, idMesa, idFormaPago, fecha, hora, estado) " +
        "VALUES (?, ?, ?, ?, ?, CURRENT_DATE, CURRENT_TIME, ?)";

    public int registrarPedido(Pedido pedido) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            // 1) idCliente (OBLIGATORIO)
            ps.setInt(1, pedido.getIdCliente());

            // 2) idDireccion (OBLIGATORIO)
            ps.setInt(2, pedido.getIdDireccion());

            // 3) idEmpleado (opcional, puede ser null)
            if (pedido.getIdEmpleado() == null || pedido.getIdEmpleado() == 0) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, pedido.getIdEmpleado());
            }

            // 4) idMesa (opcional, puede ser null para compras online)
            if (pedido.getIdMesa() == 0) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, pedido.getIdMesa());
            }

            // 5) idFormaPago (OBLIGATORIO)
            ps.setInt(5, pedido.getIdFormaPago());

            // 6) estado
            ps.setString(6, pedido.getEstado());

            // Ejecutar INSERT
            int filas = ps.executeUpdate();

            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // devolvemos idPedido generado
                }
            }

            return -1;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}



