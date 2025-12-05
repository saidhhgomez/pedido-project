package com.restaurant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        String sql = "SELECT * FROM formaPago";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MetodoPago pago = new MetodoPago();
                pago.setIdFormaPago(rs.getInt("idFormaPago"));
                pago.setNombre(rs.getString("nombre"));
                lista.add(pago);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ==========================
    // OBTENER POR ID
    // ==========================
    public MetodoPago obtenerPorId(int id) {
        MetodoPago pago = null;
        String sql = "SELECT * FROM formapago WHERE idFormaPago = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                pago = new MetodoPago();
                pago.setIdFormaPago(rs.getInt("idFormaPago"));
                pago.setNombre(rs.getString("nombre"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pago;
    }

    // ==========================
    // AGREGAR
    // ==========================
    public boolean agregar(MetodoPago pago) {
        String sql = "INSERT INTO formapago (nombre) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pago.getNombre());
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==========================
    // ACTUALIZAR
    // ==========================
    public boolean actualizar(int id, MetodoPago pago) {
        String sql = "UPDATE formapago SET nombre = ? WHERE idFormaPago = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pago.getNombre());
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==========================
    // ELIMINAR
    // ==========================
    public boolean eliminar(int id) {
        String sql = "DELETE FROM formapago WHERE idFormaPago = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
