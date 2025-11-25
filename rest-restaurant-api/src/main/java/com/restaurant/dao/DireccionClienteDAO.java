package com.restaurant.dao;

import com.restaurant.config.DBConnection;
import com.restaurant.model.DireccionCliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DireccionClienteDAO {

    // INSERT
    public boolean registrarDireccion(DireccionCliente d) {
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

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error al registrar dirección: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean eliminarDireccion(int idDireccion) {
        String sql = "DELETE FROM DireccionCliente WHERE idDireccion=?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idDireccion);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al eliminar dirección: " + e.getMessage());
            return false;
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

    // GET idCliente
    public List<DireccionCliente> listarPorCliente(int idCliente) {
        List<DireccionCliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM DireccionCliente WHERE idCliente=?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

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

        } catch (Exception e) {
            System.out.println("Error al listar direcciones por cliente: " + e.getMessage());
        }
        return lista;
    }
}