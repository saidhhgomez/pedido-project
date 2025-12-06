package com.restaurant.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import com.restaurant.config.DBConnection;
import com.restaurant.model.Empleado;
import com.restaurant.model.Cliente;
import com.restaurant.model.Contrato;
import com.restaurant.model.Credenciales;
import com.restaurant.model.Persona;

public class RegistroDAO {
	
	public int registrarEmpleadoCompleto(Credenciales cred, Persona persona, Empleado empleado, Contrato contrato) {
	    int idGenerado = 0;
	    Connection conn = null;

	    try {
	        conn = DBConnection.getConnection();
	        conn.setAutoCommit(false);

	        // Credenciales
	        String sqlCred = "INSERT INTO Credenciales (usuario, contrasena, fechaCreacion) VALUES (?, ?, NOW())";
	        int idCredencial = 0;

	        try (PreparedStatement psCred = conn.prepareStatement(sqlCred, Statement.RETURN_GENERATED_KEYS)) {

	            psCred.setString(1, cred.getUsuario());
	            psCred.setString(2, cred.getContrasena());
	            psCred.executeUpdate();

	            ResultSet rsCred = psCred.getGeneratedKeys();
	            if (rsCred.next()) {
	                idCredencial = rsCred.getInt(1);
	            } else {
	                conn.rollback();
	                return -1;
	            }
	        }

	        // Persona
	        String sqlPersona = "INSERT INTO Persona (idCredencial, nombres, apPaterno, apMaterno, genero, tipoDocumento, numDocumento, telefono, correo, fechaNacimiento) "
	        		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	        int idPersona = 0;

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
	                Instant instant = persona.getFechaNacimiento().toInstant();
	                LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
	                psPersona.setDate(10, java.sql.Date.valueOf(localDate));
	            } else {
	                psPersona.setNull(10, java.sql.Types.DATE);
	            }

	            psPersona.executeUpdate();

	            ResultSet rsPersona = psPersona.getGeneratedKeys();
	            if (rsPersona.next()) {
	                idPersona = rsPersona.getInt(1);
	            } else {
	                conn.rollback();
	                return -1;
	            }
	        }

	        // Empleado
	        String sqlEmpleado = "INSERT INTO Empleado (idPersona, direccion, estadoEmpleado, fechaRegistro, imagenEmpleado_url) "
	        		+ "VALUES (?, ?, ?, NOW(), ?)";

	        int idEmpleado = 0;

	        try (PreparedStatement psEmpleado =
	                     conn.prepareStatement(sqlEmpleado, Statement.RETURN_GENERATED_KEYS)) {

	            psEmpleado.setInt(1, idPersona);
	            psEmpleado.setString(2, empleado.getDireccion());
	            psEmpleado.setString(3, empleado.getEstadoEmpleado());
	            psEmpleado.setString(4, empleado.getImagenConductor_url());
	            psEmpleado.executeUpdate();

	            ResultSet rsEmpleado = psEmpleado.getGeneratedKeys();
	            if (rsEmpleado.next()) {
	                idEmpleado = rsEmpleado.getInt(1);
	            } else {
	                conn.rollback();
	                return -1;
	            }
	        }

	        // Contrato
	        String sqlContrato = "INSERT INTO Contrato (idEmpleado, idSucursal, idTipoContrato, idRol, fechaInicio, fechaFin, salario, estadoContrato) "
	        		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	        try (PreparedStatement psContrato = conn.prepareStatement(sqlContrato)) {

	            psContrato.setInt(1, idEmpleado);
	            psContrato.setInt(2, contrato.getIdSucursal());
	            psContrato.setInt(3, contrato.getIdTipoContrato());
	            psContrato.setInt(4, contrato.getIdRol());

	            psContrato.setTimestamp(5, new java.sql.Timestamp(contrato.getFechaInicio().getTime()));
	            psContrato.setTimestamp(6, new java.sql.Timestamp(contrato.getFechaFin().getTime()));
	            psContrato.setBigDecimal(7, contrato.getSalario());

	            psContrato.setString(8, contrato.getEstadoContrato() != null ? contrato.getEstadoContrato() : "ACTIVO");

	            psContrato.executeUpdate();
	        }

	        conn.commit();
	        idGenerado = idEmpleado;

	    } catch (Exception e) {
	        e.printStackTrace();
	        try {
	            if (conn != null) conn.rollback();
	        } catch (Exception ex) {}
	        return -1;
	    } finally {
	        try {
	            if (conn != null) conn.close();
	        } catch (Exception e) {}
	    }

	    return idGenerado;
	}
	
	public int registrarClienteCompleto(Credenciales cred, Persona persona, Cliente cliente) {
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

	                // Cliente
	                String sqlCliente = "INSERT INTO Cliente (idPersona, fechaRegistro, imagenCliente_url) VALUES (?, NOW(), ?)";
	                    try (PreparedStatement psCliente = conn.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
	                    psCliente.setInt(1, idPersona);
	                    psCliente.setString(2, cliente.getImagenCliente_url());
	                    psCliente.executeUpdate();


	                    ResultSet rsCliente = psCliente.getGeneratedKeys();
	                    if (rsCliente.next()) {
	                        idGenerado = rsCliente.getInt(1);
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
	
	//BUSCAR DNI PARA CONTRATO
    public HashMap<String, Object> buscarPorDni(String dni) {
        String sql = "SELECT * FROM Persona WHERE numDocumento = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                HashMap<String, Object> data = new HashMap<>();

                data.put("idPersona", rs.getInt("idPersona"));

                String nombreCompleto = rs.getString("nombres") + " " +
                                        rs.getString("apPaterno") + " " +
                                        rs.getString("apMaterno");
                data.put("nombreCompleto", nombreCompleto.trim());

                data.put("genero", rs.getString("genero"));
                data.put("tipoDocumento", rs.getString("tipoDocumento"));
                data.put("numDocumento", rs.getString("numDocumento"));
                data.put("telefono", rs.getString("telefono"));
                data.put("correo", rs.getString("correo"));

                // --- FORMATEAR FECHA ---
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaNac = rs.getDate("fechaNacimiento");
                data.put("fechaNacimiento", fechaNac != null ? sdf.format(fechaNac) : null);

                return data;
            }

        } catch (Exception e) {
            System.out.println("Error al buscar persona por DNI: " + e.getMessage());
        }

        return null;
    }
    
    //REGISTRAR EMPLEADO EXISTENTE CONTRATO
    public int registrarEmpleadoExisteCompleto(Empleado empleado, Contrato contrato, int idPersona) {

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String sqlEmpleado = "INSERT INTO Empleado (idPersona, direccion, estadoEmpleado, fechaRegistro, imagenEmpleado_url) "
                    + "VALUES (?, ?, 'activo', NOW(), ?)";

            int idEmpleadoGenerado = 0;

            try (PreparedStatement psEmp = conn.prepareStatement(sqlEmpleado, Statement.RETURN_GENERATED_KEYS)) {

                psEmp.setInt(1, idPersona);
                psEmp.setString(2, empleado.getDireccion());
                psEmp.setString(3, empleado.getImagenConductor_url());

                int rowsEmp = psEmp.executeUpdate();
                if (rowsEmp == 0) {
                    conn.rollback();
                    System.out.println("Error: No se pudo insertar Empleado.");
                    return 0;
                }

                try (ResultSet rs = psEmp.getGeneratedKeys()) {
                    if (rs.next()) {
                        idEmpleadoGenerado = rs.getInt(1);
                    } else {
                        conn.rollback();
                        System.out.println("Error: No se obtuvo idEmpleado.");
                        return 0;
                    }
                }
            }

            String sqlContrato = "INSERT INTO Contrato (idEmpleado, idSucursal, idTipoContrato, idRol, fechaInicio, fechaFin, salario, estadoContrato) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, 'activo')";

            try (PreparedStatement psCon = conn.prepareStatement(sqlContrato)) {

                psCon.setInt(1, idEmpleadoGenerado);
                psCon.setInt(2, contrato.getIdSucursal());
                psCon.setInt(3, contrato.getIdTipoContrato());
                psCon.setInt(4, contrato.getIdRol());

                psCon.setTimestamp(5, new java.sql.Timestamp(contrato.getFechaInicio().getTime()));
                psCon.setTimestamp(6, new java.sql.Timestamp(contrato.getFechaFin().getTime()));
                psCon.setBigDecimal(7, contrato.getSalario());

                int rowsCon = psCon.executeUpdate();
                if (rowsCon == 0) {
                    conn.rollback();
                    System.out.println("Error: No se pudo insertar Contrato.");
                    return 0;
                }
            }

            conn.commit();
            return idEmpleadoGenerado;

        } catch (Exception e) {

            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            System.out.println("Error en registrarEmpleadoExisteCompleto: " + e.getMessage());
            return 0;

        } finally {

            try {
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
