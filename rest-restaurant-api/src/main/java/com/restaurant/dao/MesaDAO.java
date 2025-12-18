package com.restaurant.dao;

import com.restaurant.config.DBConnection;
import com.restaurant.model.Mesa;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MesaDAO {

    public boolean crearMesa(Mesa mesa) {
        String sql = "INSERT INTO Mesa (idSucursal, numeroMesa, capacidad, ubicacion) VALUES (?, ?, ?, ?)";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, mesa.getIdSucursal());
            ps.setString(2, mesa.getNumeroMesa());
            ps.setInt(3, mesa.getCapacidad());
            ps.setString(4, mesa.getUbicacion());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<HashMap<String, Object>> listarMesas(int idSucursal, String estadoFiltro) {
        List<HashMap<String, Object>> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Mesa WHERE 1=1");

        if (idSucursal > 0) {
            sql.append(" AND idSucursal = ?");
        }

        if (estadoFiltro.equals("OPERATIVO")) {
            sql.append(" AND estado != 'inactivo'");
        } else if (!estadoFiltro.equals("TODOS")) {
            sql.append(" AND estado = ?");
        }

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (idSucursal > 0) {
                ps.setInt(paramIndex++, idSucursal);
            }
            
            if (!estadoFiltro.equals("TODOS") && !estadoFiltro.equals("OPERATIVO")) {
                ps.setString(paramIndex, estadoFiltro);
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HashMap<String, Object> m = new HashMap<>();
                m.put("idMesa", rs.getInt("idMesa"));
                m.put("idSucursal", rs.getInt("idSucursal"));
                m.put("numeroMesa", rs.getString("numeroMesa"));
                m.put("capacidad", rs.getInt("capacidad"));
                m.put("ubicacion", rs.getString("ubicacion"));
                m.put("estado", rs.getString("estado"));
                lista.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public boolean actualizarMesa(Mesa mesa) {
        String sql = "UPDATE Mesa SET numeroMesa=?, capacidad=?, ubicacion=?, estado=? WHERE idMesa=?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, mesa.getNumeroMesa());
            ps.setInt(2, mesa.getCapacidad());
            ps.setString(3, mesa.getUbicacion());
            ps.setString(4, mesa.getEstado());
            ps.setInt(5, mesa.getIdMesa());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    public boolean cambiarEstado(int idMesa, String nuevoEstado) {
        String sql = "UPDATE Mesa SET estado = ? WHERE idMesa = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idMesa);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    public boolean existeNumeroMesa(String numero, int idSucursal, int idMesaExcluir) {
        String sql = "SELECT COUNT(*) FROM Mesa WHERE numeroMesa = ? AND idSucursal = ? AND estado != 'inactivo' AND idMesa != ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, numero);
            ps.setInt(2, idSucursal);
            ps.setInt(3, idMesaExcluir);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}