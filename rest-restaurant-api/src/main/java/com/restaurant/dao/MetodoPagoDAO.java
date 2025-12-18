package com.restaurant.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.restaurant.config.DBConnection;
import com.restaurant.model.MetodoPago;

public class MetodoPagoDAO {

    // ==========================
    // LISTAR TODOS
    // ==========================
    public List<MetodoPago> obtenerTodos() {
        List<MetodoPago> lista = new ArrayList<>();
        String sql = "SELECT * FROM FormaPago";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MetodoPago mp = new MetodoPago();
                mp.setIdFormaPago(rs.getInt("idFormaPago"));
                mp.setNombre(rs.getString("nombre"));
                mp.setEstadoFormaPago(rs.getString("estadoFormaPago"));
                lista.add(mp);
            }

        } catch (Exception e) {
            System.out.println("Error listar forma pago: " + e.getMessage());
        }
        return lista;
    }

    // ==========================
    // LISTAR SOLO ACTIVOS
    // ==========================
    public List<MetodoPago> obtenerActivos() {
        List<MetodoPago> lista = new ArrayList<>();
        String sql = "SELECT * FROM FormaPago WHERE estadoFormaPago = 'activo'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MetodoPago mp = new MetodoPago();
                mp.setIdFormaPago(rs.getInt("idFormaPago"));
                mp.setNombre(rs.getString("nombre"));
                mp.setEstadoFormaPago(rs.getString("estadoFormaPago"));
                lista.add(mp);
            }

        } catch (Exception e) {
            System.out.println("Error listar activos: " + e.getMessage());
        }
        return lista;
    }

    // ==========================
    // OBTENER POR ID (ACTIVO)
    // ==========================
    public MetodoPago obtenerPorId(int id) {
        String sql = "SELECT * FROM FormaPago WHERE idFormaPago = ? AND estadoFormaPago = 'activo'";
        MetodoPago mp = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                mp = new MetodoPago();
                mp.setIdFormaPago(rs.getInt("idFormaPago"));
                mp.setNombre(rs.getString("nombre"));
                mp.setEstadoFormaPago(rs.getString("estadoFormaPago"));
            }

        } catch (Exception e) {
            System.out.println("Error obtener por ID: " + e.getMessage());
        }
        return mp;
    }

    // ==========================
    // VALIDAR DUPLICADO
    // ==========================
    public boolean existeNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM FormaPago WHERE nombre = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            System.out.println("Error validar duplicado: " + e.getMessage());
        }
        return false;
    }

    // ==========================
    // AGREGAR
    // ==========================
    public boolean agregar(MetodoPago pago) {
        String sql = "INSERT INTO FormaPago (nombre) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pago.getNombre());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error agregar forma pago: " + e.getMessage());
            return false;
        }
    }

    // ==========================
    // ACTUALIZAR
    // ==========================
    public boolean actualizar(int id, MetodoPago pago) {
        String sql = "UPDATE FormaPago SET nombre = ? WHERE idFormaPago = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pago.getNombre());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error actualizar forma pago: " + e.getMessage());
            return false;
        }
    }

    // ==========================
    // CAMBIAR ESTADO (BOTÓN)
    // ==========================
    public boolean cambiarEstado(int id, String estado) {

        String sql = "UPDATE FormaPago SET estadoFormaPago = ? WHERE idFormaPago = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al cambiar estado FormaPago: " + e.getMessage());
            return false;
        }
    }


    // ==========================
    // DELETE LÓGICO
    // ==========================
    public boolean eliminarLogico(int id) {
        String sql = "UPDATE FormaPago SET estadoFormaPago = 'inactivo' WHERE idFormaPago = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error eliminar lógico: " + e.getMessage());
            return false;
        }
    }
}
