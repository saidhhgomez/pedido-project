package com.restaurant.dao;

import java.sql.*;
<<<<<<< HEAD
import com.restaurant.config.DBConnection;
import com.restaurant.model.Pedido;

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
=======
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
        String sqlPed = "INSERT INTO Pedido (idCliente, idDireccion, idMesa, idSucursal, idFormaPago, fecha, hora, estado) " +
                        "VALUES (?, ?, ?, ?, ?, CURDATE(), CURTIME(), 'pendiente')";
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
                    ps.setInt(4, (int) cabecera.get("idSucursal"));
                    ps.setInt(5, (int) cabecera.get("idFormaPago"));
                    ps.executeUpdate();
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) idPedido = rs.getInt(1);
                    else throw new SQLException("Error al generar ID de Pedido Online.");
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
        String sqlPed = "INSERT INTO Pedido (idCliente, idDireccion, idEmpleado, idMesa, idSucursal, idFormaPago, fecha, hora, estado) VALUES (?, ?, ?, ?, ?, ?, CURDATE(), CURTIME(), 'pendiente')";
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
                    ps.setInt(5, (int) cabecera.get("idSucursal")); // <--- NUEVO
                    ps.setInt(6, (int) cabecera.get("idFormaPago"));
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
    
    public void insertarMasDetalles(int idPedido, List<HashMap<String, Object>> nuevosDetalles) throws SQLException {
        String sqlDet = "INSERT INTO DetallePedido (idPedido, idCatalogo, cantidad, precioUnitario, igv, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlStock = "UPDATE CatalogoComida SET stock = stock - ? WHERE idCatalogo = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psD = conn.prepareStatement(sqlDet);
                 PreparedStatement psS = conn.prepareStatement(sqlStock)) {
                
                for (HashMap<String, Object> d : nuevosDetalles) {
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
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
    
    public void eliminarPlatoDetalle(int idDetalle) throws SQLException {
        String sqlInfo = "SELECT idCatalogo, cantidad FROM DetallePedido WHERE idDetalle = ?";
        String sqlStock = "UPDATE CatalogoComida SET stock = stock + ? WHERE idCatalogo = ?";
        String sqlDelete = "DELETE FROM DetallePedido WHERE idDetalle = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int idCatalogo = 0;
                int cantidad = 0;

                try (PreparedStatement ps1 = conn.prepareStatement(sqlInfo)) {
                    ps1.setInt(1, idDetalle);
                    ResultSet rs = ps1.executeQuery();
                    if (rs.next()) {
                        idCatalogo = rs.getInt("idCatalogo");
                        cantidad = rs.getInt("cantidad");
                    } else {
                        throw new SQLException("El detalle no existe.");
                    }
                }

                try (PreparedStatement ps2 = conn.prepareStatement(sqlStock)) {
                    ps2.setInt(1, cantidad);
                    ps2.setInt(2, idCatalogo);
                    ps2.executeUpdate();
                }

                try (PreparedStatement ps3 = conn.prepareStatement(sqlDelete)) {
                    ps3.setInt(1, idDetalle);
                    ps3.executeUpdate();
                }

                conn.commit();
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
    
    public boolean validarMesaEnSucursal(int idMesa, int idSucursal) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Mesa WHERE idMesa = ? AND idSucursal = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMesa);
            ps.setInt(2, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
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
    
    public void ejecutarCambioMesa(int idPedido, int idMesaOrigen, int idMesaDestino) throws SQLException {
        String sqlUpdatePedido = "UPDATE Pedido SET idMesa = ? WHERE idPedido = ?";
        String sqlLiberarOrigen = "UPDATE Mesa SET estado = 'liberando' WHERE idMesa = ?";
        String sqlOcuparDestino = "UPDATE Mesa SET estado = 'ocupada' WHERE idMesa = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps1 = conn.prepareStatement(sqlUpdatePedido)) {
                    ps1.setInt(1, idMesaDestino);
                    ps1.setInt(2, idPedido);
                    ps1.executeUpdate();
                }

                try (PreparedStatement ps2 = conn.prepareStatement(sqlLiberarOrigen)) {
                    ps2.setInt(1, idMesaOrigen);
                    ps2.executeUpdate();
                }

                try (PreparedStatement ps3 = conn.prepareStatement(sqlOcuparDestino)) {
                    ps3.setInt(1, idMesaDestino);
                    ps3.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public String obtenerEstadoPedido(int idPedido) throws SQLException {
        String sql = "SELECT estado FROM Pedido WHERE idPedido = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("estado");
            }
        }
        return null;
    }
    
    public void cancelarPedidoCompleto(int idPedido, Integer idMesa) throws SQLException {
        String sqlGetDetalles = "SELECT idCatalogo, cantidad FROM DetallePedido WHERE idPedido = ?";
        String sqlUpdateStock = "UPDATE CatalogoComida SET stock = stock + ? WHERE idCatalogo = ?";
        String sqlUpdatePedido = "UPDATE Pedido SET estado = 'cancelado' WHERE idPedido = ?";
        String sqlUpdateMesa = "UPDATE Mesa SET estado = 'disponible' WHERE idMesa = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                List<HashMap<String, Object>> detalles = new ArrayList<>();
                try (PreparedStatement ps1 = conn.prepareStatement(sqlGetDetalles)) {
                    ps1.setInt(1, idPedido);
                    ResultSet rs = ps1.executeQuery();
                    while (rs.next()) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("id", rs.getInt("idCatalogo"));
                        item.put("cant", rs.getInt("cantidad"));
                        detalles.add(item);
                    }
                }

                try (PreparedStatement ps2 = conn.prepareStatement(sqlUpdateStock)) {
                    for (HashMap<String, Object> item : detalles) {
                        ps2.setInt(1, (int) item.get("cant"));
                        ps2.setInt(2, (int) item.get("id"));
                        ps2.addBatch();
                    }
                    ps2.executeBatch();
                }

                try (PreparedStatement ps3 = conn.prepareStatement(sqlUpdatePedido)) {
                    ps3.setInt(1, idPedido);
                    ps3.executeUpdate();
                }

                if (idMesa != null) {
                    try (PreparedStatement ps4 = conn.prepareStatement(sqlUpdateMesa)) {
                        ps4.setInt(1, idMesa);
                        ps4.executeUpdate();
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
    
    public HashMap<String, Object> obtenerInfoBasicaPedido(int idPedido) throws SQLException {
        String sql = "SELECT estado, idMesa, idSucursal FROM Pedido WHERE idPedido = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HashMap<String, Object> info = new HashMap<>();
                    info.put("estado", rs.getString("estado"));
                    info.put("idMesa", rs.getInt("idMesa")); 
                    info.put("idSucursal", rs.getInt("idSucursal"));
                    return info;
                }
            }
        }
        return null;
    }
    
    public List<HashMap<String, Object>> obtenerHistorialPorEmpleado(int idEmpleado) throws SQLException {
        List<HashMap<String, Object>> historial = new ArrayList<>();
        String sql = "SELECT p.idPedido, p.fecha, p.hora, p.estado, p.idMesa, m.numeroMesa, fp.nombre AS formaPago " +
                     "FROM Pedido p " +
                     "LEFT JOIN Mesa m ON p.idMesa = m.idMesa " +
                     "JOIN FormaPago fp ON p.idFormaPago = fp.idFormaPago " +
                     "WHERE p.idEmpleado = ? " +
                     "ORDER BY p.fecha DESC, p.hora DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HashMap<String, Object> pedido = new HashMap<>();
                    int idPedido = rs.getInt("idPedido");
                    
                    pedido.put("idPedido", idPedido);
                    pedido.put("fecha", rs.getString("fecha"));
                    pedido.put("hora", rs.getString("hora"));
                    pedido.put("estado", rs.getString("estado"));
                    pedido.put("numeroMesa", rs.getString("numeroMesa"));
                    pedido.put("formaPago", rs.getString("formaPago"));
                    
                    pedido.put("detalles", obtenerDetallesPorPedido(idPedido));
                    
                    historial.add(pedido);
                }
            }
        }
        return historial;
    }
    
    public List<HashMap<String, Object>> obtenerHistorialPorMesa(int idMesa) throws SQLException {
        List<HashMap<String, Object>> historial = new ArrayList<>();
        String sql = "SELECT p.idPedido, p.fecha, p.hora, p.estado, " +
                     "fp.nombre AS formaPago, " +
                     "CONCAT(per.nombres, ' ', per.apPaterno) AS nombreEmpleado " +
                     "FROM Pedido p " +
                     "JOIN FormaPago fp ON p.idFormaPago = fp.idFormaPago " +
                     "JOIN Empleado e ON p.idEmpleado = e.idEmpleado " +
                     "JOIN Persona per ON e.idPersona = per.idPersona " +
                     "WHERE p.idMesa = ? " +
                     "ORDER BY p.fecha DESC, p.hora DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMesa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HashMap<String, Object> pedido = new HashMap<>();
                    int idPedido = rs.getInt("idPedido");
                    
                    pedido.put("idPedido", idPedido);
                    pedido.put("fecha", rs.getString("fecha"));
                    pedido.put("hora", rs.getString("hora"));
                    pedido.put("estado", rs.getString("estado"));
                    pedido.put("formaPago", rs.getString("formaPago"));
                    pedido.put("atendidoPor", rs.getString("nombreEmpleado"));
                    
                    pedido.put("detalles", obtenerDetallesPorPedido(idPedido));
                    
                    historial.add(pedido);
                }
            }
        }
        return historial;
    }
    
    public List<HashMap<String, Object>> obtenerPedidosOnlineActivosPorSucursal(int idSucursal) throws SQLException {
        List<HashMap<String, Object>> pedidos = new ArrayList<>();
        String sql = "SELECT p.idPedido, p.fecha, p.hora, p.estado, " +
                     "per.nombres, per.apPaterno, d.direccion AS direccionEnvio, d.referencia " +
                     "FROM Pedido p " +
                     "JOIN Persona per ON p.idCliente = per.idPersona " +
                     "JOIN DireccionCliente d ON p.idDireccion = d.idDireccion " +
                     "WHERE p.idMesa = 1 " + 
                     "AND p.idSucursal = ? " + 
                     "AND p.estado IN ('pendiente', 'en camino') " +
                     "ORDER BY p.idPedido ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HashMap<String, Object> p = new HashMap<>();
                    int idPedido = rs.getInt("idPedido");
                    
                    p.put("idPedido", idPedido);
                    p.put("fecha", rs.getString("fecha"));
                    p.put("hora", rs.getString("hora"));
                    p.put("estado", rs.getString("estado"));
                    p.put("cliente", rs.getString("nombres") + " " + rs.getString("apPaterno"));
                    p.put("direccion", rs.getString("direccionEnvio"));
                    p.put("referencia", rs.getString("referencia"));
                    
                    p.put("detalles", obtenerDetallesPorPedido(idPedido));
                    
                    pedidos.add(p);
                }
            }
        }
        return pedidos;
    }
    
    public boolean esRepartidorDeSucursal(int idEmpleado, int idSucursal) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Contrato c " +
                     "JOIN Roles r ON c.idRol = r.idRol " +
                     "WHERE c.idEmpleado = ? AND c.idSucursal = ? " +
                     "AND r.nombre = 'Repartidor' AND c.estadoContrato = 'activo'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ps.setInt(2, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void asignarRepartidorYEstado(int idPedido, int idEmpleado) throws SQLException {
        String sql = "UPDATE Pedido SET idEmpleado = ?, estado = 'en camino' WHERE idPedido = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ps.setInt(2, idPedido);
            ps.executeUpdate();
        }
    }
    
    //ONLINE
    public List<HashMap<String, Object>> listarHistorialSucursal(int idSucursal, String desde, String hasta) throws SQLException {
        List<HashMap<String, Object>> lista = new ArrayList<>();

        String fechaInicio = (desde == null || desde.isEmpty()) ? "2000-01-01" : desde;
        String fechaFin = (hasta == null || hasta.isEmpty()) ? "2099-12-31" : hasta;

        String sql = "SELECT p.idPedido, p.fecha, p.hora, p.estado, per.nombres, per.apPaterno, d.direccion " +
                     "FROM Pedido p " +
                     "JOIN Persona per ON p.idCliente = per.idPersona " +
                     "JOIN DireccionCliente d ON p.idDireccion = d.idDireccion " +
                     "WHERE p.idSucursal = ? AND p.idMesa = 1 " + 
                     "AND p.estado IN ('finalizado', 'cancelado') " +
                     "AND p.fecha BETWEEN ? AND ? " +
                     "ORDER BY p.fecha DESC, p.hora DESC LIMIT 100";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            ps.setString(2, fechaInicio);
            ps.setString(3, fechaFin);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HashMap<String, Object> m = new HashMap<>();
                    m.put("idPedido", rs.getInt("idPedido"));
                    m.put("fecha", rs.getString("fecha"));
                    m.put("hora", rs.getString("hora"));
                    m.put("estado", rs.getString("estado"));
                    m.put("cliente", rs.getString("nombres") + " " + rs.getString("apPaterno"));
                    m.put("direccion", rs.getString("direccion"));
                    lista.add(m);
                }
            }
        }
        return lista;
    }

    public List<HashMap<String, Object>> listarHistorialRepartidor(int idEmpleado) throws SQLException {
        List<HashMap<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT p.idPedido, p.fecha, p.hora, p.estado, d.direccion " +
                     "FROM Pedido p " +
                     "JOIN DireccionCliente d ON p.idDireccion = d.idDireccion " +
                     "WHERE p.idEmpleado = ? AND p.estado = 'finalizado' " +
                     "ORDER BY p.fecha DESC, p.hora DESC LIMIT 20";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HashMap<String, Object> m = new HashMap<>();
                    m.put("idPedido", rs.getInt("idPedido"));
                    m.put("fecha", rs.getString("fecha"));
                    m.put("hora", rs.getString("hora"));
                    m.put("direccion", rs.getString("direccion"));
                    lista.add(m);
                }
            }
        }
        return lista;
    }
    
    public List<HashMap<String, Object>> listarPlatosPendientesCocina(int idSucursal) throws SQLException {
        List<HashMap<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT dp.idDetalle, dp.idPedido, c.nombre AS plato, dp.cantidad, p.hora " +
                     "FROM DetallePedido dp " +
                     "JOIN Pedido p ON dp.idPedido = p.idPedido " +
                     "JOIN CatalogoComida c ON dp.idCatalogo = c.idCatalogo " +
                     "WHERE p.idSucursal = ? " +
                     "AND dp.idEmpleado IS NULL " +
                     "AND p.estado NOT IN ('finalizado', 'cancelado') " + 
                     "ORDER BY p.idPedido ASC, p.hora ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("idDetalle", rs.getInt("idDetalle"));
                    item.put("idPedido", rs.getInt("idPedido"));
                    item.put("plato", rs.getString("plato"));
                    item.put("cantidad", rs.getInt("cantidad"));
                    item.put("horaPedido", rs.getString("hora"));
                    lista.add(item);
                }
            }
        }
        return lista;
    }

    public int obtenerIdPedidoDeDetalle(int idDetallePedido) throws SQLException {
        String sql = "SELECT idPedido FROM DetallePedido WHERE idDetalle = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDetallePedido);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("idPedido") : 0;
            }
        }
    }

    public boolean esCocineroDeSucursal(int idEmpleado, int idSucursal) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Contrato c JOIN Roles r ON c.idRol = r.idRol " +
                     "WHERE c.idEmpleado = ? AND c.idSucursal = ? " +
                     "AND r.nombre = 'Cocinero' AND c.estadoContrato = 'activo'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado); ps.setInt(2, idSucursal);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() && rs.getInt(1) > 0; }
        }
    }

    public void asignarCocineroADetalle(int idDetallePedido, int idEmpleado) throws SQLException {
        String sql = "UPDATE DetallePedido SET idEmpleado = ? WHERE idDetalle = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ps.setInt(2, idDetallePedido);
            ps.executeUpdate();
        }
    }
    
    public List<HashMap<String, Object>> listarHistorialPlatosCocinero(int idEmpleado) throws SQLException {
        List<HashMap<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT dp.idDetallePedido, dp.idPedido, c.nombre AS plato, dp.cantidad, p.fecha, p.hora " +
                     "FROM DetallePedido dp " +
                     "JOIN CatalogoComida c ON dp.idCatalogo = c.idCatalogo " +
                     "JOIN Pedido p ON dp.idPedido = p.idPedido " +
                     "WHERE dp.idEmpleado = ? " +
                     "ORDER BY p.fecha DESC, p.hora DESC LIMIT 50";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("idDetallePedido", rs.getInt("idDetallePedido"));
                    item.put("idPedido", rs.getInt("idPedido"));
                    item.put("plato", rs.getString("plato"));
                    item.put("cantidad", rs.getInt("cantidad"));
                    item.put("fecha", rs.getString("fecha"));
                    item.put("hora", rs.getString("hora"));
                    lista.add(item);
                }
            }
        }
        return lista;
    }
    
    public List<HashMap<String, Object>> listarPedidosPorCliente(int idCliente) throws SQLException {
        List<HashMap<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT p.idPedido, p.fecha, p.hora, p.estado, p.idSucursal, " +
                     "d.direccion, fp.nombre AS formaPago " +
                     "FROM Pedido p " +
                     "JOIN DireccionCliente d ON p.idDireccion = d.idDireccion " +
                     "JOIN FormaPago fp ON p.idFormaPago = fp.idFormaPago " +
                     "WHERE p.idCliente = ? " +
                     "ORDER BY p.fecha DESC, p.hora DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HashMap<String, Object> p = new HashMap<>();
                    int idPedido = rs.getInt("idPedido");
                    p.put("idPedido", idPedido);
                    p.put("fecha", rs.getString("fecha"));
                    p.put("hora", rs.getString("hora"));
                    p.put("estado", rs.getString("estado"));
                    p.put("idSucursal", rs.getInt("idSucursal"));
                    p.put("direccion", rs.getString("direccion"));
                    p.put("formaPago", rs.getString("formaPago"));
                    
                    p.put("detalles", obtenerDetallesPorPedido(idPedido)); 
                    
                    lista.add(p);
                }
            }
        }
        return lista;
    }
>>>>>>> develop
}