package com.restaurant.dao;

import com.restaurant.model.TipoContrato;
import com.restaurant.config.DBConnection;

import java.sql.*;
import java.util.*;

public class TipoContratoDAO {

    // CREATE
    public boolean crear(TipoContrato contrato) {
        // Validar que no exista nombre duplicado
        if (existeNombre(contrato.getNombre())) {
            throw new IllegalArgumentException("Ya existe un tipo de contrato con ese nombre");
        }

        String sql = "INSERT INTO TipoContrato (nombre, descripcion, estadoTipoContrato) VALUES (?, ?, 'activo')";
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

    // READ ALL
    public List<HashMap<String, Object>> listar() {
        List<HashMap<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT * FROM TipoContrato";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HashMap<String, Object> fila = new HashMap<>();
                fila.put("idTipoContrato", rs.getInt("idTipoContrato"));
                fila.put("nombre", rs.getString("nombre"));
                fila.put("descripcion", rs.getString("descripcion"));
                fila.put("estadoTipoContrato", rs.getString("estadoTipoContrato"));
                lista.add(fila);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar tipos de contrato: " + e.getMessage());
        }

        return lista;
    }

    // GET SOLO ACTIVOS
    public List<HashMap<String, Object>> listarActivos() {
        List<HashMap<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT * FROM TipoContrato WHERE estadoTipoContrato = 'activo'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HashMap<String, Object> fila = new HashMap<>();
                fila.put("idTipoContrato", rs.getInt("idTipoContrato"));
                fila.put("nombre", rs.getString("nombre"));
                fila.put("descripcion", rs.getString("descripcion"));
                fila.put("estadoTipoContrato", rs.getString("estadoTipoContrato"));
                lista.add(fila);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar tipos de contrato activos: " + e.getMessage());
        }

        return lista;
    }

    // READ BY ID
    public HashMap<String, Object> obtenerPorId(int id) {
        String sql = "SELECT * FROM TipoContrato WHERE idTipoContrato = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                HashMap<String, Object> fila = new HashMap<>();
                fila.put("idTipoContrato", rs.getInt("idTipoContrato"));
                fila.put("nombre", rs.getString("nombre"));
                fila.put("descripcion", rs.getString("descripcion"));
                fila.put("estadoTipoContrato", rs.getString("estadoTipoContrato"));
                return fila;
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener tipo de contrato: " + e.getMessage());
        }

        return null;
    }

    // UPDATE
    public boolean actualizar(TipoContrato contrato) {
        // Validar duplicado
        if (existeNombreActualizar(contrato.getNombre(), contrato.getIdTipoContrato())) {
            throw new IllegalArgumentException("Ya existe un tipo de contrato con ese nombre");
        }

        String sql = "UPDATE TipoContrato SET nombre = ?, descripcion = ? WHERE idTipoContrato = ?";

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

    // DELETE LOGICO / CAMBIAR ESTADO
    public boolean cambiarEstado(int idTipoContrato, String nuevoEstado) {
        String sql = "UPDATE TipoContrato SET estadoTipoContrato = ? WHERE idTipoContrato = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idTipoContrato);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al cambiar estado TipoContrato: " + e.getMessage());
            return false;
        }
    }

    // VALIDACIONES DE DUPLICADO
    private boolean existeNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM TipoContrato WHERE nombre = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) { return false; }
        return false;
    }

    private boolean existeNombreActualizar(String nombre, int id) {
        String sql = "SELECT COUNT(*) FROM TipoContrato WHERE nombre = ? AND idTipoContrato != ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setInt(2, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) { return false; }
        return false;
    }
}


