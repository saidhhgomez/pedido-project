package com.restaurant.dao;

import com.restaurant.config.DBConnection;
import com.restaurant.model.DireccionCliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DireccionClienteDAO {

    public boolean existeDireccion(int idCliente, String direccion, String distrito) throws SQLException {
        String sql = "SELECT COUNT(*) FROM DireccionCliente WHERE idCliente = ? AND direccion = ? AND distrito = ? AND estado = 1";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ps.setString(2, direccion);
            ps.setString(3, distrito);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean registrarDireccion(DireccionCliente d) throws SQLException {
        String sql = "INSERT INTO DireccionCliente (idCliente, departamento, provincia, distrito, direccion, referencia) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, d.getIdCliente());
            ps.setString(2, d.getDepartamento());
            ps.setString(3, d.getProvincia());
            ps.setString(4, d.getDistrito());
            ps.setString(5, d.getDireccion());
            ps.setString(6, d.getReferencia());
            return ps.executeUpdate() > 0;
        }
    }

    // DELETE LÓGICO
    public boolean eliminarLogico(int idDireccion) throws SQLException {
        String sql = "UPDATE DireccionCliente SET estado = 0 WHERE idDireccion = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idDireccion);
            return ps.executeUpdate() > 0;
        }
    }

    // GET ID
    public DireccionCliente obtenerPorId(int idDireccion) {
        String sql = "SELECT * FROM DireccionCliente WHERE idDireccion=?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idDireccion);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                DireccionCliente d = new DireccionCliente();
                d.setIdDireccion(rs.getInt("idDireccion"));
                d.setIdCliente(rs.getInt("idCliente"));
                d.setDepartamento(rs.getString("departamento"));
                d.setProvincia(rs.getString("provincia"));
                d.setDistrito(rs.getString("distrito"));
                d.setDireccion(rs.getString("direccion"));
                d.setReferencia(rs.getString("referencia"));
                return d;
            }

        } catch (Exception e) {
            System.out.println("Error al obtener dirección: " + e.getMessage());
        }
        return null;
    }

    public List<DireccionCliente> listarActivasPorCliente(int idCliente) throws SQLException {
        List<DireccionCliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM DireccionCliente WHERE idCliente=? AND estado = 1";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DireccionCliente d = new DireccionCliente();
                    d.setIdDireccion(rs.getInt("idDireccion"));
                    d.setIdCliente(rs.getInt("idCliente"));
                    d.setDepartamento(rs.getString("departamento"));
                    d.setProvincia(rs.getString("provincia"));
                    d.setDistrito(rs.getString("distrito"));
                    d.setDireccion(rs.getString("direccion"));
                    d.setReferencia(rs.getString("referencia"));
                    lista.add(d);
                }
            }
        }
        return lista;
    }
}