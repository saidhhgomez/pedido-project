package com.restaurant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.restaurant.config.DBConnection;
import com.restaurant.model.Empleado;
import com.restaurant.model.Credenciales;
import com.restaurant.model.Persona;

public class RegistroDAO {
	
	public int registrarEmpleadoCompleto(Credenciales cred, Persona persona, Empleado empleado) {
	    int idGenerado = 0;
	    Connection conn = null;

	    try {
	        conn = DBConnection.getConnection();
	        conn.setAutoCommit(false);

	        // Credenciales
	        String sqlCred = "INSERT INTO Credenciales (usuario, contrasena, fechaCreacion) VALUES (?, ?, NOW())";
	        try (PreparedStatement psCred = conn.prepareStatement(sqlCred, Statement.RETURN_GENERATED_KEYS)) {
	            psCred.setString(1, cred.getUsuario());
	            psCred.setString(2, cred.getContrasena());
	            psCred.executeUpdate();

	            ResultSet rsCred = psCred.getGeneratedKeys();
	            int idCredencial = 0;
	            if (rsCred.next()) {
	                idCredencial = rsCred.getInt(1);
	            } else {
	                System.out.println("Error: No se pudo obtener el ID de las credenciales.");
	                conn.rollback();
	                return -1;
	            }

	            // Insertar Persona
	            String sqlPersona = "INSERT INTO Persona (idCredencial, nombres, apPaterno, apMaterno, genero, tipoDocumento, numDocumento, telefono, correo, fechaNacimiento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	            try (PreparedStatement psPersona = conn.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS)) {
	                psPersona.setInt(1, idCredencial);
	                psPersona.setString(2, persona.getNombres());
	                psPersona.setString(3, persona.getApPaterno());
	                psPersona.setString(4, persona.getApMaterno());
	                psPersona.setString(5, String.valueOf(persona.getGenero()));
	                psPersona.setString(6, persona.getTipoDocumento());
	                psPersona.setString(7, persona.getNumDocumento());
	                psPersona.setString(8, persona.getTelefono());
	                psPersona.setString(9, persona.getCorreo());

	                if (persona.getFechaNacimiento() != null) {
	                    java.time.LocalDate localDate = persona.getFechaNacimiento()
	                        .toInstant()
	                        .atZone(java.time.ZoneId.systemDefault())
	                        .toLocalDate();
	                    psPersona.setDate(10, java.sql.Date.valueOf(localDate));
	                } else {
	                    psPersona.setNull(10, java.sql.Types.DATE);
	                }
	                psPersona.executeUpdate();

	                ResultSet rsPersona = psPersona.getGeneratedKeys();
	                int idPersona = 0;
	                if (rsPersona.next()) {
	                    idPersona = rsPersona.getInt(1);
	                } else {
	                    System.out.println("Error: No se pudo obtener el ID de la persona.");
	                    conn.rollback();
	                    return -1;
	                }

	                // Empleado
	                String sqlEmpleado = "INSERT INTO Empleado (idPersona, direccion, estadoEmpleado, fechaRegistro, imagenEmpleado_url) VALUES (?, ?, ?, NOW(), ?)";
	                try (PreparedStatement psEmpleado = conn.prepareStatement(sqlEmpleado, Statement.RETURN_GENERATED_KEYS)) {
	                    psEmpleado.setInt(1, idPersona);
	                    psEmpleado.setString(2, empleado.getDireccion());
	                    psEmpleado.setString(3, empleado.getEstadoEmpleado());
	                    psEmpleado.setString(4, empleado.getImagenConductor_url());
	                    psEmpleado.executeUpdate();

	                    ResultSet rsEmpleado = psEmpleado.getGeneratedKeys();
	                    if (rsEmpleado.next()) {
	                        idGenerado = rsEmpleado.getInt(1);
	                    } else {
	                        System.out.println("Error: No se pudo obtener el ID del empleado.");
	                        conn.rollback();
	                        return -1;
	                    }

	                    conn.commit();
	                }
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        try {
	            if (conn != null) conn.rollback();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        return -1;
	    } finally {
	        try {
	            if (conn != null) conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    return idGenerado;
	}
}
