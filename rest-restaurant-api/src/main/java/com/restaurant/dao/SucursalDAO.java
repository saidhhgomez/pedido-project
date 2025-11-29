package com.restaurant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.restaurant.config.DBConnection;
import com.restaurant.model.Sucursal;

public class SucursalDAO {
    // CREATE
    public boolean crearSucursal(Sucursal sucursal) {
        String sql = "INSERT INTO Sucursal (nombre, direccion, telefono) VALUES (?, ?, ?)";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, sucursal.getNombre());
            ps.setString(2, sucursal.getDireccion());
            ps.setString(3, sucursal.getTelefono());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al crear sucursal: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<HashMap<String, Object>> listarSucursales() {
        List<HashMap<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT * FROM Sucursal";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HashMap<String, Object> s = new HashMap<>();
                s.put("idSucursal", rs.getInt("idSucursal"));
                s.put("nombre", rs.getString("nombre"));
                s.put("direccion", rs.getString("direccion"));
                s.put("telefono", rs.getString("telefono"));
                s.put("estado", rs.getString("estadoSucursal"));
                lista.add(s);
            }

        } catch (Exception e) {
            System.out.println("Error al listar sucursales: " + e.getMessage());
        }
        return lista;
    }

    // READ BY ID
    public HashMap<String, Object> obtenerPorId(int idSucursal) {
        String sql = "SELECT * FROM Sucursal WHERE idSucursal=?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idSucursal);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                HashMap<String, Object> s = new HashMap<>();
                s.put("idSucursal", rs.getInt("idSucursal"));
                s.put("nombre", rs.getString("nombre"));
                s.put("direccion", rs.getString("direccion"));
                s.put("telefono", rs.getString("telefono"));
                s.put("estado", rs.getString("estadoSucursal"));
                return s;
            }

        } catch (Exception e) {
            System.out.println("Error al obtener sucursal por ID: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean actualizarSucursal(Sucursal sucursal) {
        String sql = "UPDATE Sucursal SET nombre=?, direccion=?, telefono=?, estadoSucursal=? WHERE idSucursal=?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, sucursal.getNombre());
            ps.setString(2, sucursal.getDireccion());
            ps.setString(3, sucursal.getTelefono());
            ps.setString(4, sucursal.getEstado());
            ps.setInt(5, sucursal.getIdSucursal());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al actualizar sucursal: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean eliminarSucursal(int idSucursal) {
        String sql = "DELETE FROM Sucursal WHERE idSucursal=?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idSucursal);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al eliminar sucursal: " + e.getMessage());
            return false;
        }
    }
}
