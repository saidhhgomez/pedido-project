package com.restaurant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.restaurant.config.DBConnection;
import com.restaurant.model.CatalogoComida;
import com.restaurant.model.StorageFile;

public class CatalogoComidaDAO {

    private StorageFileDAO storageFileDAO = new StorageFileDAO(); 

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
    public CatalogoComida obtenerPorId(int id) {
        String sql = "SELECT * FROM CatalogoComida WHERE idCatalogo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                CatalogoComida comida = new CatalogoComida();
                comida.setIdCatalogo(rs.getInt("idCatalogo"));
                comida.setNombre(rs.getString("nombre"));
                comida.setCategoria(rs.getString("categoria"));
                comida.setPrecio(rs.getDouble("precio"));
                comida.setStock(rs.getInt("stock"));
                comida.setEstadoPlato(rs.getBoolean("estadoPlato"));
                comida.setImagenPlatoUrl(rs.getString("imagenPlato_url"));
                return comida;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int agregarPlatoCompleto(CatalogoComida d, List<StorageFile> storageFiles) throws Exception {
        Connection conn = null;
        int idPlatoGenerado = 0;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String sql = "INSERT INTO CatalogoComida (nombre, categoria, precio, stock, imagenPlato_url) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                
                stmt.setString(1, d.getNombre());
                stmt.setString(2, d.getCategoria());
                stmt.setDouble(3, d.getPrecio());
                stmt.setInt(4, d.getStock());
                stmt.setString(5, d.getImagenPlatoUrl());
                
                if (stmt.executeUpdate() > 0) {
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        idPlatoGenerado = rs.getInt(1);
                    } else {
                        throw new Exception("Error: No se obtuvo el ID del plato insertado.");
                    }
                } else {
                    throw new Exception("Error al insertar el plato en CatalogoComida.");
                }
            }
            
            for (StorageFile metadata : storageFiles) {
                if (metadata.getRelatedTable().equals("CatalogoComida")) {
                    metadata.setRelatedId(idPlatoGenerado); 
                }
                storageFileDAO.insertFileMetadata(conn, metadata);
            }

            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw e;
            
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {}
        }
        return idPlatoGenerado;
    }
    public List<CatalogoComida> obtenerPlatosActivos() {
        List<CatalogoComida> lista = new ArrayList<>();
        String sql = "SELECT * FROM CatalogoComida WHERE estadoPlato = true";

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
    
    public boolean existePlatoActivo(String nombre, String categoria) {
        String sql = "SELECT COUNT(*) FROM CatalogoComida " +
                     "WHERE LOWER(nombre)=LOWER(?) " +
                     "AND LOWER(categoria)=LOWER(?) " +
                     "AND estadoPlato = true";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, categoria);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean actualizarEstado(int idCatalogo, boolean estado) {
        String sql = "UPDATE CatalogoComida SET estadoPlato = ? WHERE idCatalogo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, estado);
            stmt.setInt(2, idCatalogo);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean eliminar(int idCatalogo) {
        String sql = "UPDATE CatalogoComida SET estadoPlato = false WHERE idCatalogo = ?";

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