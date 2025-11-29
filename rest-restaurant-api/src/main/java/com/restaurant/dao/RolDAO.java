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
        String sql = "INSERT INTO Roles (nombre, descripcion) VALUES (?, ?)";
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
                rol.put("estado", rs.getString("estadoRol"));
                return rol;
            }

        } catch (Exception e) {
            System.out.println("Error al obtener rol por ID: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean actualizarRol(Rol rol) {
        String sql = "UPDATE Roles SET nombre=?, descripcion=?, estadoRol=? WHERE idRol=?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, rol.getNombre());
            ps.setString(2, rol.getDescripcion());
            ps.setString(3, rol.getEstado());
            ps.setInt(4, rol.getIdRol());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al actualizar rol: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean eliminarRol(int idRol) {
        String sql = "DELETE FROM Roles WHERE idRol=?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idRol);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al eliminar rol: " + e.getMessage());
            return false;
        }
    }
}