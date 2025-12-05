package com.restaurant.dao;

import com.restaurant.model.TipoContrato;
import com.restaurant.config.DBConnection;

import java.sql.*;
import java.util.*;

public class TipoContratoDAO {

    public boolean crear(TipoContrato contrato) {
        String sql = "INSERT INTO tipocontrato (nombre, descripcion) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contrato.getNombre());
            stmt.setString(2, contrato.getDescripcion());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear tipo de contrato: " + e.getMessage());
            return false;
        }
    }

    public List<HashMap<String, Object>> listar() {
        List<HashMap<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipocontrato";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HashMap<String, Object> fila = new HashMap<>();
                fila.put("idTipoContrato", rs.getInt("idTipoContrato"));
                fila.put("nombre", rs.getString("nombre"));
                fila.put("descripcion", rs.getString("descripcion"));

                lista.add(fila);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar tipos de contrato: " + e.getMessage());
        }

        return lista;
    }

    public HashMap<String, Object> obtenerPorId(int id) {
        String sql = "SELECT * FROM tipocontrato WHERE idTipoContrato = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                HashMap<String, Object> fila = new HashMap<>();
                fila.put("idTipoContrato", rs.getInt("idTipoContrato"));
                fila.put("nombre", rs.getString("nombre"));
                fila.put("descripcion", rs.getString("descripcion"));

                return fila;
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener tipo de contrato: " + e.getMessage());
        }

        return null;
    }

    public boolean actualizar(TipoContrato contrato) {
        String sql = "UPDATE tipocontrato SET nombre = ?, descripcion = ? WHERE idTipoContrato = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contrato.getNombre());
            stmt.setString(2, contrato.getDescripcion());
            stmt.setInt(3, contrato.getIdTipoContrato());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar tipo de contrato: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM tipocontrato WHERE idTipoContrato = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar tipo de contrato: " + e.getMessage());
            return false;
        }
    }
}
