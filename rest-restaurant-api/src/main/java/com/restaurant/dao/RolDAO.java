package com.restaurant.dao;

import com.restaurant.config.DBConnection;
import com.restaurant.model.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RolDAO {

    // CREATE
    public boolean crearRol(Rol rol) {
        if (existeNombre(rol.getNombre())) {
            throw new IllegalArgumentException("Ya existe un rol con ese nombre");
        }

        String sql = "INSERT INTO Roles (nombre, descripcion, estadoRol) VALUES (?, ?, 'activo')";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, rol.getNombre());
            ps.setString(2, rol.getDescripcion());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al crear rol: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<HashMap<String, Object>> listarRoles() {
        List<HashMap<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT * FROM Roles";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HashMap<String, Object> rol = new HashMap<>();
                rol.put("idRol", rs.getInt("idRol"));
                rol.put("nombre", rs.getString("nombre"));
                rol.put("descripcion", rs.getString("descripcion"));
                rol.put("estadoRol", rs.getString("estadoRol"));
                lista.add(rol);
            }

        } catch (Exception e) {
            System.out.println("Error al listar roles: " + e.getMessage());
        }
        return lista;
    }

    // GET SOLO ACTIVOS
    public List<HashMap<String, Object>> listarActivos() {
        List<HashMap<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT * FROM Roles WHERE estadoRol='activo'";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HashMap<String, Object> rol = new HashMap<>();
                rol.put("idRol", rs.getInt("idRol"));
                rol.put("nombre", rs.getString("nombre"));
                rol.put("descripcion", rs.getString("descripcion"));
                rol.put("estadoRol", rs.getString("estadoRol"));
                lista.add(rol);
            }

        } catch (Exception e) {
            System.out.println("Error al listar roles activos: " + e.getMessage());
        }
        return lista;
    }

    // READ BY ID
    public HashMap<String, Object> obtenerPorId(int idRol) {
        String sql = "SELECT * FROM Roles WHERE idRol=?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idRol);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                HashMap<String, Object> rol = new HashMap<>();
                rol.put("idRol", rs.getInt("idRol"));
                rol.put("nombre", rs.getString("nombre"));
                rol.put("descripcion", rs.getString("descripcion"));
                rol.put("estadoRol", rs.getString("estadoRol"));
                return rol;
            }

        } catch (Exception e) {
            System.out.println("Error al obtener rol por ID: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean actualizarRol(Rol rol) {
        if (existeNombreActualizar(rol.getNombre(), rol.getIdRol())) {
            throw new IllegalArgumentException("Ya existe un rol con ese nombre");
        }

        String sql = "UPDATE Roles SET nombre=?, descripcion=? WHERE idRol=?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, rol.getNombre());
            ps.setString(2, rol.getDescripcion());
            ps.setInt(3, rol.getIdRol());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al actualizar rol: " + e.getMessage());
            return false;
        }
    }

    // DELETE LOGICO / CAMBIAR ESTADO
    public boolean cambiarEstado(int idRol, String nuevoEstado) {
        String sql = "UPDATE Roles SET estadoRol=? WHERE idRol=?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idRol);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al cambiar estado del rol: " + e.getMessage());
            return false;
        }
    }

    // VALIDACIONES DUPLICADO
    private boolean existeNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM Roles WHERE nombre=?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (Exception e) { return false; }
        return false;
    }

    private boolean existeNombreActualizar(String nombre, int id) {
        String sql = "SELECT COUNT(*) FROM Roles WHERE nombre=? AND idRol<>?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setInt(2, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (Exception e) { return false; }
        return false;
    }
}
