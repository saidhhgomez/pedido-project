package com.restaurant.dao;

import com.restaurant.model.StorageFile;
import java.sql.*;

public class StorageFileDAO {

    public int insertFileMetadata(Connection conn, StorageFile file) throws SQLException {
        
        String sql = "INSERT INTO storage_file (bucket, object_key, filename, content_type, size, uploaded_by, related_table, related_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, file.getBucket());
            stmt.setString(2, file.getObjectKey());
            stmt.setString(3, file.getFilename());
            stmt.setString(4, file.getContentType());
            stmt.setLong(5, file.getSize());
            
            if (file.getUploadedBy() != null && file.getUploadedBy() > 0) {
                 stmt.setInt(6, file.getUploadedBy());
            } else {
                 stmt.setNull(6, Types.INTEGER);
            }
            
            stmt.setString(7, file.getRelatedTable());
            stmt.setInt(8, file.getRelatedId());
            
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La inserción de metadatos falló, no se insertó ninguna fila.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("La inserción de metadatos falló, no se obtuvo ID.");
                }
            }
        }
    }
}