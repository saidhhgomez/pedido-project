package com.restaurant.dao;

import com.restaurant.config.DBConnection;

import java.text.SimpleDateFormat;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmpleadoDAO {

	public List<HashMap<String, Object>> listarEmpleados(String tipo) {
	    List<HashMap<String, Object>> lista = new ArrayList<>();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    
	    String sql = "SELECT e.idEmpleado, p.nombres, p.apPaterno, p.apMaterno, p.numDocumento, " +
	                 "e.estadoEmpleado, e.imagenEmpleado_url AS fotoUrl, " + 
	                 "c.idContrato, c.fechaInicio, c.fechaFin, c.estadoContrato, " +
	                 "c.pdf_firmado_key AS pdfKey, " + 
	                 "s.nombre AS sucursal, r.nombre AS rol " +
	                 "FROM Empleado e " +
	                 "INNER JOIN Persona p ON e.idPersona = p.idPersona " +
	                 "LEFT JOIN Contrato c ON c.idContrato = (" +
	                 "    SELECT MAX(idContrato) FROM Contrato WHERE idEmpleado = e.idEmpleado" +
	                 ") " +
	                 "LEFT JOIN Sucursal s ON c.idSucursal = s.idSucursal " +
	                 "LEFT JOIN Roles r ON c.idRol = r.idRol ";

	    if (tipo.equals("SOLO_VIGENTES")) {
	        sql += "WHERE e.estadoEmpleado = 'activo' AND c.estadoContrato = 'activo' " +
	               "AND (c.fechaFin IS NULL OR c.fechaFin >= CURRENT_DATE)";
	    } else {
	        sql += "WHERE e.estadoEmpleado != 'borrado'"; 
	    }

	    try (Connection cn = DBConnection.getConnection();
	         PreparedStatement ps = cn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            HashMap<String, Object> emp = new HashMap<>();
	            emp.put("idEmpleado", rs.getInt("idEmpleado"));
	            emp.put("nombre", rs.getString("nombres") + " " + rs.getString("apPaterno") + " " + rs.getString("apMaterno"));
	            emp.put("dni", rs.getString("numDocumento"));
	            emp.put("estadoEmpleado", rs.getString("estadoEmpleado"));
	            emp.put("fotoUrl", rs.getString("fotoUrl")); 
	            emp.put("pdfKey", rs.getString("pdfKey"));
	            emp.put("idContrato", rs.getInt("idContrato"));

	            Date fInicio = rs.getDate("fechaInicio");
	            emp.put("fechaInicio", (fInicio != null) ? sdf.format(fInicio) : null);

	            Date fFin = rs.getDate("fechaFin");
	            if (fFin != null) {
	                emp.put("fechaFin", sdf.format(fFin));
	            } else {
	                emp.put("fechaFin", "Indefinido");
	            }

	            emp.put("estadoContrato", rs.getString("estadoContrato"));
	            emp.put("sucursal", rs.getString("sucursal"));
	            emp.put("rol", rs.getString("rol"));
	            lista.add(emp);
	        }
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    }
	    return lista;
	}
	
	public boolean finalizarContratoActivo(int idEmpleado, Connection cn) throws SQLException {
	    String sql = "UPDATE Contrato SET estadoContrato = 'finalizado', fechaFin = CURRENT_TIMESTAMP " +
	                 "WHERE idEmpleado = ? AND estadoContrato = 'activo'";
	    try (PreparedStatement ps = cn.prepareStatement(sql)) {
	        ps.setInt(1, idEmpleado);
	        return ps.executeUpdate() > 0;
	    }
	}
	
	public boolean inactivarEmpleado(int idEmpleado, Connection cn) throws SQLException {
	    String sql = "UPDATE Empleado SET estadoEmpleado = 'inactivo' WHERE idEmpleado = ?";
	    try (PreparedStatement ps = cn.prepareStatement(sql)) {
	        ps.setInt(1, idEmpleado);
	        return ps.executeUpdate() > 0;
	    }
	}
	
	public List<HashMap<String, Object>> obtenerHistorialContratos(int idEmpleado) throws SQLException {
	    List<HashMap<String, Object>> historial = new ArrayList<>();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    
	    String sql = "SELECT c.idContrato, s.nombre as sucursal, r.nombre as rol, tc.nombre as tipo, " +
	                 "c.fechaInicio, c.fechaFin, c.salario, c.estadoContrato, " +
	                 "c.pdf_firmado_key as pdfKey, e.imagenEmpleado_url as fotoUrl " +
	                 "FROM Contrato c " +
	                 "JOIN Empleado e ON c.idEmpleado = e.idEmpleado " +
	                 "JOIN Sucursal s ON c.idSucursal = s.idSucursal " +
	                 "JOIN Roles r ON c.idRol = r.idRol " +
	                 "JOIN TipoContrato tc ON c.idTipoContrato = tc.idTipoContrato " +
	                 "WHERE c.idEmpleado = ? ORDER BY c.fechaInicio DESC";

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, idEmpleado);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                HashMap<String, Object> fila = new HashMap<>();
	                fila.put("idContrato", rs.getInt("idContrato"));
	                fila.put("sucursal", rs.getString("sucursal"));
	                fila.put("rol", rs.getString("rol"));
	                fila.put("tipo", rs.getString("tipo"));
	                
	                fila.put("fechaInicio", rs.getTimestamp("fechaInicio") != null ? 
	                         sdf.format(rs.getTimestamp("fechaInicio")) : "");
	                
	                fila.put("fechaFin", rs.getTimestamp("fechaFin") != null ? 
	                         sdf.format(rs.getTimestamp("fechaFin")) : "Vigente");
	                
	                fila.put("salario", rs.getBigDecimal("salario"));
	                fila.put("estado", rs.getString("estadoContrato"));
	                
	                fila.put("pdfKey", rs.getString("pdfKey"));
	                fila.put("fotoUrl", rs.getString("fotoUrl"));
	                
	                historial.add(fila);
	            }
	        }
	    }
	    return historial;
	}
	
	public List<HashMap<String, Object>> listarRepartidoresPorSucursal(int idSucursal) throws SQLException {
	    List<HashMap<String, Object>> repartidores = new ArrayList<>();
	    
	    String sql = "SELECT e.idEmpleado, per.nombres, per.apPaterno, per.apMaterno " +
	                 "FROM Empleado e " +
	                 "JOIN Persona per ON e.idPersona = per.idPersona " +
	                 "JOIN Contrato c ON e.idEmpleado = c.idEmpleado " +
	                 "JOIN Roles r ON c.idRol = r.idRol " +
	                 "WHERE r.nombre = 'Repartidor' " +
	                 "AND c.idSucursal = ? " + 
	                 "AND c.estadoContrato = 'activo' " + 
	                 "AND e.estadoEmpleado = 'activo'";

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        ps.setInt(1, idSucursal);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                HashMap<String, Object> rep = new HashMap<>();
	                rep.put("idEmpleado", rs.getInt("idEmpleado"));
	                
	                String nombreCompleto = rs.getString("nombres") + " " + 
	                                       rs.getString("apPaterno") + " " + 
	                                       (rs.getString("apMaterno") != null ? rs.getString("apMaterno") : "");
	                
	                rep.put("nombreCompleto", nombreCompleto.trim());
	                repartidores.add(rep);
	            }
	        }
	    }
	    return repartidores;
	}
}