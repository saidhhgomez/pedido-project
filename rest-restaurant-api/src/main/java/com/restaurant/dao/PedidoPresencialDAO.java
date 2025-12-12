package com.restaurant.dao;

import com.restaurant.config.DBConnection;
import com.restaurant.model.PedidoPresencial;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoPresencialDAO {

    // Registrar nuevo pedido presencial (fecha y hora automáticas con NOW())
    public boolean registrar(PedidoPresencial pedido) {

        String sql = "INSERT INTO pedido (idEmpleado, idMesa, idFormaPago, estado, fecha, hora) "
                   + "VALUES (?, ?, ?, ?, DATE(NOW()), TIME(NOW()))";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, pedido.getIdEmpleado());
            ps.setObject(2, pedido.getIdMesa());
            ps.setObject(3, pedido.getIdFormaPago());

            // Estado por defecto
            if (pedido.getEstado() == null || pedido.getEstado().trim().isEmpty()) {
                ps.setString(4, "PENDIENTE");
            } else {
                ps.setString(4, pedido.getEstado());
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Listar todos los pedidos presenciales (idCliente IS NULL)
    public List<PedidoPresencial> listar() {
        List<PedidoPresencial> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE idCliente IS NULL ORDER BY fecha DESC, hora DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PedidoPresencial p = mapResultSetToPedido(rs);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Listar pedidos por empleado
    public List<PedidoPresencial> listarPorEmpleado(int idEmpleado) {
        List<PedidoPresencial> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE idCliente IS NULL AND idEmpleado = ? "
                   + "ORDER BY fecha DESC, hora DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEmpleado);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PedidoPresencial p = mapResultSetToPedido(rs);
                    lista.add(p);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Obtener pedido por ID
    public PedidoPresencial obtenerPorId(int idPedido) {
        String sql = "SELECT * FROM pedido WHERE idPedido = ? AND idCliente IS NULL";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPedido(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Actualizar solo estado
    public boolean actualizarEstado(int idPedido, String nuevoEstado) {
        String sql = "UPDATE pedido SET estado = ? WHERE idPedido = ? AND idCliente IS NULL";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cambiar mesa
    public boolean cambiarMesa(int idPedido, Integer idMesaNueva) {
        String sql = "UPDATE pedido SET idMesa = ? WHERE idPedido = ? AND idCliente IS NULL";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (idMesaNueva != null) {
                ps.setObject(1, idMesaNueva);
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }

            ps.setInt(2, idPedido);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar todos los datos del pedido (excepto fecha/hora)
    public boolean actualizarPedido(PedidoPresencial pedido) {
        String sql = "UPDATE pedido SET idEmpleado = ?, idMesa = ?, idFormaPago = ?, estado = ? "
                   + "WHERE idPedido = ? AND idCliente IS NULL";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, pedido.getIdEmpleado());
            ps.setObject(2, pedido.getIdMesa());
            ps.setObject(3, pedido.getIdFormaPago());
            ps.setString(4, pedido.getEstado());
            ps.setInt(5, pedido.getIdPedido());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mapear ResultSet → Modelo
    private PedidoPresencial mapResultSetToPedido(ResultSet rs) throws SQLException {
        PedidoPresencial p = new PedidoPresencial();

        p.setIdPedido(rs.getInt("idPedido"));
        p.setIdEmpleado((Integer) rs.getObject("idEmpleado"));
        p.setIdMesa((Integer) rs.getObject("idMesa"));
        p.setIdFormaPago((Integer) rs.getObject("idFormaPago"));
        p.setFecha(rs.getString("fecha"));
        p.setHora(rs.getString("hora"));
        p.setEstado(rs.getString("estado"));

        return p;
    }

 // Cambia de 'pedido_presencial' a 'pedido' si es lo correcto
    public boolean cambiarFormaPago(int idPedido, Integer idFormaPagoNueva) {
        String sql = "UPDATE pedido SET idFormaPago = ? WHERE idPedido = ?";  // Cambia 'pedido_presencial' por 'pedido'

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFormaPagoNueva);
            ps.setInt(2, idPedido);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}

