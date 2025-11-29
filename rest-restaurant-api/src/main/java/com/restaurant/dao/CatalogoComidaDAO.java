package com.restaurant.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.restaurant.config.DBConnection;
import com.restaurant.model.CatalogoComida;

public class CatalogoComidaDAO {

    public List<CatalogoComida> obtenerTodos() {
        List<CatalogoComida> lista = new ArrayList<>();
        String sql = "SELECT * FROM CatalogoComida";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CatalogoComida comida = new CatalogoComida();
                comida.setIdCatalogo(rs.getInt("idCatalogo"));
                comida.setNombre(rs.getString("nombre"));
                comida.setCategoria(rs.getString("categoria"));
                comida.setPrecio(rs.getDouble("precio"));
                comida.setStock(rs.getInt("stock"));
                comida.setEstadoPlato(rs.getBoolean("estadoPlato"));
                comida.setImagenPlatoUrl(rs.getString("imagenPlato_url"));
                lista.add(comida);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    public boolean agregar(CatalogoComida d) {
        String sql = "INSERT INTO CatalogoComida (nombre, categoria, precio, stock, estadoPlato, imagenPlato_url) VALUES (?,?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            
            stmt.setString(1, d.getNombre());
            stmt.setString(2, d.getCategoria());
            stmt.setDouble(3, d.getPrecio());
            stmt.setInt(4, d.getStock());
            stmt.setBoolean(5,d.isEstadoPlato());
            stmt.setString(6,d.getImagenPlatoUrl());
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean actualizar(int id, CatalogoComida d ) {
        String sql = "UPDATE CatalogoComida SET nombre = ?,precio = ?, stock = ?, estadoPlato = ? WHERE idCatalogo = ?";
        	

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, d.getNombre());
            stmt.setDouble(2, d.getPrecio());
            stmt.setInt(3, d.getStock());
            stmt.setBoolean(4, d.isEstadoPlato());
            stmt.setInt(5, id);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean eliminar(int idCatalogo) {
        String sql = "DELETE FROM CatalogoComida WHERE idCatalogo = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCatalogo);
           
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}