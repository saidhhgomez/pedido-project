package com.restaurant.dao;

import com.restaurant.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class LoginDAO {

    public HashMap<String, Object> login(String usuario) {
    	
        HashMap<String, Object> data = new HashMap<>();
    	
    	String sql = "SELECT " +
    	        "  cr.idCredencial, cr.usuario, cr.contrasena, " +
    	        "  p.idPersona, p.nombres, p.apPaterno, p.apMaterno, " +
    	        "  c.idCliente, " +
    	        "  e.idEmpleado, r.nombre AS rolNombre " +
    	        "FROM Credenciales cr " +
    	        "JOIN Persona p ON p.idCredencial = cr.idCredencial " +
    	        "LEFT JOIN Cliente c ON c.idPersona = p.idPersona " +
    	        "LEFT JOIN Empleado e ON e.idPersona = p.idPersona " +
    	        "LEFT JOIN Contrato con ON con.idEmpleado = e.idEmpleado AND con.estadoContrato = 'activo' " +
    	        "LEFT JOIN Roles r ON r.idRol = con.idRol " +
    	        "WHERE cr.usuario = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                data.put("contrasena", rs.getString("contrasena"));

                String nombre = rs.getString("nombres") + " " +
                                rs.getString("apPaterno") + " " +
                                rs.getString("apMaterno");
                data.put("nombre", nombre.trim());
                
                int idCliente = rs.getInt("idCliente");
                int idEmpleado = rs.getInt("idEmpleado");
                
                data.put("idCliente", idCliente > 0 ? idCliente : null);
                data.put("idEmpleado", idEmpleado > 0 ? idEmpleado : null);
                
                String rolNombre = rs.getString("rolNombre");
                data.put("rol", rolNombre != null ? rolNombre.toLowerCase() : "cliente");

                return data;
            }

        } catch (Exception e) {
            System.out.println("Error en login DAO: " + e.getMessage());
        }

        return null; // USER NO EXISTE
    }
}