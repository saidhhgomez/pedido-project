package com.restaurant.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.math.BigDecimal;

import com.restaurant.config.DBConnection;

public class PedidoDAO {

    public HashMap<String, Object> obtenerInfoPlato(int idCatalogo) throws SQLException {
        String sql = "SELECT nombre, precio, stock FROM CatalogoComida WHERE idCatalogo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCatalogo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HashMap<String, Object> plato = new HashMap<>();
                    plato.put("nombre", rs.getString("nombre"));
                    plato.put("precio", rs.getBigDecimal("precio"));
                    plato.put("stock", rs.getInt("stock"));
                    return plato;
                }
            }
        }
        return null;
    }

    public int insertarPedidoCompleto(HashMap<String, Object> cabecera, List<HashMap<String, Object>> detalles) throws SQLException {
        String sqlPed = "INSERT INTO Pedido (idCliente, idDireccion, idMesa, idFormaPago, fecha, hora, estado) VALUES (?, ?, ?, ?, CURDATE(), CURTIME(), 'pendiente')";
        String sqlDet = "INSERT INTO DetallePedido (idPedido, idCatalogo, cantidad, precioUnitario, igv, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlStock = "UPDATE CatalogoComida SET stock = stock - ? WHERE idCatalogo = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int idPedido;
                try (PreparedStatement ps = conn.prepareStatement(sqlPed, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, (int) cabecera.get("idCliente"));
                    ps.setInt(2, (int) cabecera.get("idDireccion"));
                    ps.setInt(3, (int) cabecera.get("idMesa"));
                    ps.setInt(4, (int) cabecera.get("idFormaPago"));
                    ps.executeUpdate();
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) idPedido = rs.getInt(1);
                    else throw new SQLException("Error al generar ID de Pedido.");
                }

                try (PreparedStatement psD = conn.prepareStatement(sqlDet);
                     PreparedStatement psS = conn.prepareStatement(sqlStock)) {
                    for (HashMap<String, Object> d : detalles) {
                        psD.setInt(1, idPedido);
                        psD.setInt(2, (int) d.get("idCatalogo"));
                        psD.setInt(3, (int) d.get("cantidad"));
                        psD.setBigDecimal(4, (BigDecimal) d.get("precioUnitario"));
                        psD.setBigDecimal(5, (BigDecimal) d.get("igv"));
                        psD.setBigDecimal(6, (BigDecimal) d.get("subtotal"));
                        psD.addBatch();

                        psS.setInt(1, (int) d.get("cantidad"));
                        psS.setInt(2, (int) d.get("idCatalogo"));
                        psS.addBatch();
                    }
                    psD.executeBatch();
                    psS.executeBatch();
                }
                conn.commit();
                return idPedido;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
    
    public int insertarPedidoPresencial(HashMap<String, Object> cabecera, List<HashMap<String, Object>> detalles) throws SQLException {
        String sqlPed = "INSERT INTO Pedido (idCliente, idDireccion, idEmpleado, idMesa, idFormaPago, fecha, hora, estado) VALUES (?, ?, ?, ?, ?, CURDATE(), CURTIME(), 'pendiente')";
        String sqlDet = "INSERT INTO DetallePedido (idPedido, idCatalogo, cantidad, precioUnitario, igv, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlStock = "UPDATE CatalogoComida SET stock = stock - ? WHERE idCatalogo = ?";
        String sqlMesa = "UPDATE Mesa SET estado = 'ocupada' WHERE idMesa = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int idPedido;
                try (PreparedStatement ps = conn.prepareStatement(sqlPed, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, (int) cabecera.get("idCliente"));
                    ps.setInt(2, (int) cabecera.get("idDireccion"));
                    ps.setInt(3, (int) cabecera.get("idEmpleado"));
                    ps.setInt(4, (int) cabecera.get("idMesa"));
                    ps.setInt(5, (int) cabecera.get("idFormaPago"));
                    ps.executeUpdate();
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) idPedido = rs.getInt(1);
                    else throw new SQLException("Error al generar ID de Pedido Presencial.");
                }

                try (PreparedStatement psD = conn.prepareStatement(sqlDet);
                     PreparedStatement psS = conn.prepareStatement(sqlStock)) {
                    for (HashMap<String, Object> d : detalles) {
                        psD.setInt(1, idPedido);
                        psD.setInt(2, (int) d.get("idCatalogo"));
                        psD.setInt(3, (int) d.get("cantidad"));
                        psD.setBigDecimal(4, (BigDecimal) d.get("precioUnitario"));
                        psD.setBigDecimal(5, (BigDecimal) d.get("igv"));
                        psD.setBigDecimal(6, (BigDecimal) d.get("subtotal"));
                        psD.addBatch();

                        psS.setInt(1, (int) d.get("cantidad"));
                        psS.setInt(2, (int) d.get("idCatalogo"));
                        psS.addBatch();
                    }
                    psD.executeBatch();
                    psS.executeBatch();
                }

                try (PreparedStatement psM = conn.prepareStatement(sqlMesa)) {
                    psM.setInt(1, (int) cabecera.get("idMesa"));
                    psM.executeUpdate();
                }

                conn.commit();
                return idPedido;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public String obtenerEstadoMesa(int idMesa) throws SQLException {
        String sql = "SELECT estado FROM Mesa WHERE idMesa = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMesa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("estado");
            }
        }
        return null;
    }
    
    public void cambiarEstadoMesa(int idMesa, String nuevoEstado) throws SQLException {
        String sql = "UPDATE Mesa SET estado = ? WHERE idMesa = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idMesa);
            ps.executeUpdate();
        }
    }
    
    public void finalizarPedido(int idPedido) throws SQLException {
        String sql = "UPDATE Pedido SET estado = 'finalizado' WHERE idPedido = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ps.executeUpdate();
        }
    }
    
    public HashMap<String, Object> obtenerPedidoActivoPorMesa(int idMesa) throws SQLException {
        String sql = "SELECT p.idPedido, p.fecha, p.hora, p.estado, " +
                     "fp.nombre AS formaPago, " +
                     "CONCAT(per.nombres, ' ', per.apPaterno) AS nombreEmpleado " +
                     "FROM Pedido p " +
                     "JOIN FormaPago fp ON p.idFormaPago = fp.idFormaPago " +
                     "JOIN Empleado e ON p.idEmpleado = e.idEmpleado " +
                     "JOIN Persona per ON e.idPersona = per.idPersona " +
                     "WHERE p.idMesa = ? AND p.estado IN ('pendiente', 'finalizado') " +
                     "ORDER BY p.idPedido DESC LIMIT 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMesa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HashMap<String, Object> pedido = new HashMap<>();
                    pedido.put("idPedido", rs.getInt("idPedido"));
                    pedido.put("fecha", rs.getString("fecha"));
                    pedido.put("hora", rs.getString("hora"));
                    pedido.put("estado", rs.getString("estado"));
                    pedido.put("formaPago", rs.getString("formaPago"));
                    pedido.put("nombreEmpleado", rs.getString("nombreEmpleado"));
                    
                    pedido.put("detalles", obtenerDetallesPorPedido(rs.getInt("idPedido")));
                    return pedido;
                }
            }
        }
        return null;
    }

    private List<HashMap<String, Object>> obtenerDetallesPorPedido(int idPedido) throws SQLException {
        List<HashMap<String, Object>> detalles = new ArrayList<>();
        String sql = "SELECT dp.idDetalle, dp.idCatalogo, c.nombre AS nombrePlato, " +
                     "dp.cantidad, dp.precioUnitario, dp.subtotal " +
                     "FROM DetallePedido dp " +
                     "JOIN CatalogoComida c ON dp.idCatalogo = c.idCatalogo " +
                     "WHERE dp.idPedido = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HashMap<String, Object> det = new HashMap<>();
                    det.put("idDetalle", rs.getInt("idDetalle"));
                    det.put("idCatalogo", rs.getInt("idCatalogo"));
                    det.put("nombrePlato", rs.getString("nombrePlato"));
                    det.put("cantidad", rs.getInt("cantidad"));
                    det.put("precioUnitario", rs.getBigDecimal("precioUnitario"));
                    det.put("subtotal", rs.getBigDecimal("subtotal"));
                    detalles.add(det);
                }
            }
        }
        return detalles;
    }
}



