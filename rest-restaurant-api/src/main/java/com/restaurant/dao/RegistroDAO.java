package com.restaurant.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
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
import com.restaurant.model.StorageFile;

public class RegistroDAO {
	
    private StorageFileDAO storageFileDAO = new StorageFileDAO(); 
	
    public int registrarEmpleadoCompleto(
            Credenciales cred, 
            Persona persona, 
            Empleado empleado, 
            Contrato contrato,
            Cliente cliente,
            List<StorageFile> storageFiles
    ) {
        Connection conn = null;
        int idGenerado = 0;
        int idCredencial = 0;
        int idPersona = 0;
        int idEmpleado = 0;
        int idContrato = 0;
        int idCliente = 0; 
        
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String sqlCred = "INSERT INTO Credenciales (usuario, contrasena, fechaCreacion) VALUES (?, ?, NOW())";
            try (PreparedStatement psCred = conn.prepareStatement(sqlCred, Statement.RETURN_GENERATED_KEYS)) {
                psCred.setString(1, cred.getUsuario());
                psCred.setString(2, cred.getContrasena());
                psCred.executeUpdate();
                ResultSet rsCred = psCred.getGeneratedKeys();
                if (rsCred.next()) { idCredencial = rsCred.getInt(1); } else { throw new Exception("Error: No se obtuvo idCredencial."); }
            }

            String sqlPersona = "INSERT INTO Persona (idCredencial, nombres, apPaterno, apMaterno, genero, tipoDocumento, numDocumento, telefono, correo, fechaNacimiento) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                } else { psPersona.setNull(10, java.sql.Types.DATE); }
                psPersona.executeUpdate();

                ResultSet rsPersona = psPersona.getGeneratedKeys();
                if (rsPersona.next()) { idPersona = rsPersona.getInt(1); } else { throw new Exception("Error: No se obtuvo idPersona."); }
            }
            
            String sqlCliente = "INSERT INTO Cliente (idPersona, fechaRegistro, imagenCliente_url) VALUES (?, NOW(), ?)";
            try (PreparedStatement psCliente = conn.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
                psCliente.setInt(1, idPersona);
                psCliente.setString(2, cliente.getImagenCliente_url()); 
                psCliente.executeUpdate();
                
                ResultSet rsCliente = psCliente.getGeneratedKeys();
                if (rsCliente.next()) { idCliente = rsCliente.getInt(1); } else { throw new Exception("Error: No se obtuvo idCliente."); }
            }
            
            String sqlEmpleado = "INSERT INTO Empleado (idPersona, direccion, fechaRegistro, imagenEmpleado_url) VALUES (?, ?, NOW(), ?)";
            try (PreparedStatement psEmpleado = conn.prepareStatement(sqlEmpleado, Statement.RETURN_GENERATED_KEYS)) {
                psEmpleado.setInt(1, idPersona);
                psEmpleado.setString(2, empleado.getDireccion());
                psEmpleado.setString(3, empleado.getImagenConductor_url());
                psEmpleado.executeUpdate();

                ResultSet rsEmpleado = psEmpleado.getGeneratedKeys();
                if (rsEmpleado.next()) { idEmpleado = rsEmpleado.getInt(1); } else { throw new Exception("Error: No se obtuvo idEmpleado."); }
            }

            String sqlContrato = "INSERT INTO Contrato (idEmpleado, idSucursal, idTipoContrato, idRol, fechaInicio, fechaFin, salario, pdf_firmado_key) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psContrato = conn.prepareStatement(sqlContrato, Statement.RETURN_GENERATED_KEYS)) {
                psContrato.setInt(1, idEmpleado);
                psContrato.setInt(2, contrato.getIdSucursal());
                psContrato.setInt(3, contrato.getIdTipoContrato());
                psContrato.setInt(4, contrato.getIdRol());
                psContrato.setTimestamp(5, new java.sql.Timestamp(contrato.getFechaInicio().getTime()));
                
                if (contrato.getFechaFin() != null) {
                    psContrato.setTimestamp(6, new java.sql.Timestamp(contrato.getFechaFin().getTime()));
                } else {
                    psContrato.setNull(6, Types.TIMESTAMP);
                }
                
                psContrato.setBigDecimal(7, contrato.getSalario());
                psContrato.setString(8, contrato.getPdfFirmadoKey());

                psContrato.executeUpdate();
                
                ResultSet rsContrato = psContrato.getGeneratedKeys();
                if (rsContrato.next()) { idContrato = rsContrato.getInt(1); } else { throw new Exception("Error: No se obtuvo idContrato."); }
            }

            for (StorageFile metadata : storageFiles) {
                if (metadata.getObjectKey().contains("contratos/firmados")) {
                    metadata.setRelatedTable("Contrato");
                    metadata.setRelatedId(idContrato); 
                } else if (metadata.getObjectKey().contains("imagen_empleado")) {
                    metadata.setRelatedTable("Empleado");
                    metadata.setRelatedId(idEmpleado); 
                } else if (metadata.getObjectKey().contains("imagen_cliente")) {
                    metadata.setRelatedTable("Cliente");
                    metadata.setRelatedId(idCliente);
                }
                
                storageFileDAO.insertFileMetadata(conn, metadata);
            }
            
            conn.commit();
            idGenerado = idEmpleado;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {}
            return 0;

        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {}
        }

        return idGenerado;
    }
	
    public int registrarClienteCompleto(Credenciales cred, Persona persona, Cliente cliente, List<StorageFile> storageFiles) throws Exception {
	    int idGenerado = 0; // idCliente
	    Connection conn = null;

	    try {
	        conn = DBConnection.getConnection();
	        conn.setAutoCommit(false);

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
	                throw new Exception("Error: No se pudo obtener el ID de las credenciales.");
	            }
	        }

	        String sqlPersona = "INSERT INTO Persona (idCredencial, nombres, apPaterno, apMaterno, genero, tipoDocumento, numDocumento, telefono, correo, fechaNacimiento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
	            if (rsPersona.next()) {
	                idPersona = rsPersona.getInt(1);
	            } else {
	                throw new Exception("Error: No se pudo obtener el ID de la persona.");
	            }
	        }

	        String sqlCliente = "INSERT INTO Cliente (idPersona, fechaRegistro, imagenCliente_url) VALUES (?, NOW(), ?)";
	        int idCliente = 0;
	        try (PreparedStatement psCliente = conn.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
	            psCliente.setInt(1, idPersona);
	            psCliente.setString(2, cliente.getImagenCliente_url());
	            psCliente.executeUpdate();

	            ResultSet rsCliente = psCliente.getGeneratedKeys();
	            if (rsCliente.next()) {
	                idCliente = rsCliente.getInt(1);
	            } else {
	                throw new Exception("Error: No se pudo obtener el ID del cliente.");
	            }
	            idGenerado = idCliente;
	        }
        
            for (StorageFile metadata : storageFiles) {
                if (metadata.getObjectKey().contains("imagen_cliente")) { 
                    metadata.setRelatedTable("Cliente");
                    metadata.setRelatedId(idCliente); 
                    metadata.setUploadedBy(idCliente);
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
	        return 0; 
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

    public int registrarEmpleadoExisteCompleto(
            Empleado empleado, 
            Contrato contrato, 
            int idPersona, 
            List<StorageFile> storageFiles
    ) {
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
                    throw new Exception("Error: No se pudo insertar Empleado.");
                }

                try (ResultSet rs = psEmp.getGeneratedKeys()) {
                    if (rs.next()) {
                        idEmpleadoGenerado = rs.getInt(1);
                    } else {
                        throw new Exception("Error: No se obtuvo idEmpleado.");
                    }
                }
            }
            
            String sqlContrato = "INSERT INTO Contrato (idEmpleado, idSucursal, idTipoContrato, idRol, fechaInicio, fechaFin, salario, estadoContrato, pdf_firmado_key) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, 'activo', ?)";

            int idContratoGenerado = 0; 
            
            try (PreparedStatement psCon = conn.prepareStatement(sqlContrato, Statement.RETURN_GENERATED_KEYS)) {
                psCon.setInt(1, idEmpleadoGenerado);
                psCon.setInt(2, contrato.getIdSucursal());
                psCon.setInt(3, contrato.getIdTipoContrato());
                psCon.setInt(4, contrato.getIdRol());
                psCon.setTimestamp(5, new java.sql.Timestamp(contrato.getFechaInicio().getTime()));
                
                if (contrato.getFechaFin() != null) {
                    psCon.setTimestamp(6, new java.sql.Timestamp(contrato.getFechaFin().getTime()));
                } else {
                    psCon.setNull(6, java.sql.Types.TIMESTAMP);
                }
                
                psCon.setBigDecimal(7, contrato.getSalario());
                psCon.setString(8, contrato.getPdfFirmadoKey());

                int rowsCon = psCon.executeUpdate();
                if (rowsCon == 0) {
                    throw new Exception("Error: No se pudo insertar Contrato.");
                }
                
                try (ResultSet rs = psCon.getGeneratedKeys()) {
                     if (rs.next()) {
                         idContratoGenerado = rs.getInt(1);
                     } else {
                         throw new Exception("Error: No se obtuvo idContrato.");
                     }
                }
            }

            for (StorageFile metadata : storageFiles) {
                
                if (metadata.getObjectKey().contains("contratos/firmados")) {
                    metadata.setRelatedTable("Contrato");
                    metadata.setRelatedId(idContratoGenerado);
                } else if (metadata.getObjectKey().contains("imagen_empleado")) {
                    metadata.setRelatedTable("Empleado");
                    metadata.setRelatedId(idEmpleadoGenerado);
                } else {
                    System.err.println("ADVERTENCIA: Key de B2 no reconocida, no se pudo asignar relación: " + metadata.getObjectKey());
                    continue; 
                }
                storageFileDAO.insertFileMetadata(conn, metadata);
            }
            
            conn.commit();
            return idEmpleadoGenerado;

        } catch (Exception e) {

            try {
                if (conn != null) {
                    System.out.println("Transacción fallida. Realizando Rollback.");
                    conn.rollback();
                }
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

    
    public boolean actualizarPersona(int idPersona, Persona persona) {
        String sql = "UPDATE Persona SET nombres=?, apPaterno=?, apMaterno=?, genero=?, tipoDocumento=?, numDocumento=?, telefono=?, correo=?, fechaNacimiento=? WHERE idPersona=?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, persona.getNombres());
            ps.setString(2, persona.getApPaterno());
            ps.setString(3, persona.getApMaterno());
            ps.setString(4, String.valueOf(persona.getGenero()));
            ps.setString(5, persona.getTipoDocumento());
            ps.setString(6, persona.getNumDocumento());
            ps.setString(7, persona.getTelefono());
            ps.setString(8, persona.getCorreo());

            if (persona.getFechaNacimiento() != null) {
                LocalDate localDate = persona.getFechaNacimiento()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                ps.setDate(9, java.sql.Date.valueOf(localDate));
            } else {
                ps.setNull(9, java.sql.Types.DATE);
            }

            ps.setInt(10, idPersona);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al actualizar persona: " + e.getMessage());
            return false;
        }
    }

    

    public boolean tieneContratoActivo(int idPersona) {
        String sql = "SELECT COUNT(*) FROM Contrato c " +
                     "INNER JOIN Empleado e ON c.idEmpleado = e.idEmpleado " +
                     "WHERE e.idPersona = ? AND c.estadoContrato = 'activo'";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idPersona);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int obtenerIdEmpleadoSiExiste(int idPersona) {
        String sql = "SELECT idEmpleado FROM Empleado WHERE idPersona = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idPersona);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int insertarEmpleado(Empleado emp, int idPersona, Connection cn) throws SQLException {
        String sql = "INSERT INTO Empleado (idPersona, direccion, estadoEmpleado, fechaRegistro, imagenEmpleado_url) VALUES (?, ?, 'activo', NOW(), ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idPersona);
            ps.setString(2, emp.getDireccion());
            ps.setString(3, emp.getImagenConductor_url());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public void reactivarEmpleado(Empleado emp, int idEmpleado, Connection cn) throws SQLException {
        String sql = "UPDATE Empleado SET direccion = ?, estadoEmpleado = 'activo', imagenEmpleado_url = ?, fechaRegistro = NOW() WHERE idEmpleado = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, emp.getDireccion());
            ps.setString(2, emp.getImagenConductor_url());
            ps.setInt(3, idEmpleado);
            ps.executeUpdate();
        }
    }

    public int insertarContrato(Contrato c, int idEmpleado, Connection cn) throws SQLException {
        String sql = "INSERT INTO Contrato (idEmpleado, idSucursal, idTipoContrato, idRol, fechaInicio, fechaFin, salario, estadoContrato, pdf_firmado_key) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 'activo', ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idEmpleado);
            ps.setInt(2, c.getIdSucursal());
            ps.setInt(3, c.getIdTipoContrato());
            ps.setInt(4, c.getIdRol());
            ps.setTimestamp(5, new java.sql.Timestamp(c.getFechaInicio().getTime()));
            if (c.getFechaFin() != null) ps.setTimestamp(6, new java.sql.Timestamp(c.getFechaFin().getTime()));
            else ps.setNull(6, java.sql.Types.TIMESTAMP);
            ps.setBigDecimal(7, c.getSalario());
            ps.setString(8, c.getPdfFirmadoKey());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    
    public HashMap<String, Object> obtenerPerfil(int idCliente) throws SQLException {
        HashMap<String, Object> perfil = null;
        String sql = "SELECT c.idCliente, c.fechaRegistro, c.categoria, c.imagenCliente_url, " +
                     "p.nombres, p.apPaterno, p.apMaterno, p.genero, p.tipoDocumento, " +
                     "p.numDocumento, p.telefono, p.correo, p.fechaNacimiento " +
                     "FROM Cliente c " +
                     "JOIN Persona p ON c.idPersona = p.idPersona " +
                     "WHERE c.idCliente = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    perfil = new HashMap<>();
                    perfil.put("idCliente", rs.getInt("idCliente"));
                    perfil.put("nombres", rs.getString("nombres"));
                    perfil.put("apPaterno", rs.getString("apPaterno"));
                    perfil.put("apMaterno", rs.getString("apMaterno"));
                    perfil.put("genero", rs.getString("genero"));
                    perfil.put("tipoDocumento", rs.getString("tipoDocumento"));
                    perfil.put("numDocumento", rs.getString("numDocumento"));
                    perfil.put("telefono", rs.getString("telefono"));
                    perfil.put("correo", rs.getString("correo"));
                    perfil.put("fechaNacimiento", rs.getString("fechaNacimiento"));
                    perfil.put("fechaRegistro", rs.getString("fechaRegistro"));
                    perfil.put("categoria", rs.getString("categoria"));
                    perfil.put("imagenUrl", rs.getString("imagenCliente_url"));
                }
            }
        }
        return perfil;
    }
    
    public HashMap<String, Object> obtenerPerfilBasico(int idEmpleado) throws SQLException {
        HashMap<String, Object> perfil = null;
        String sql = "SELECT e.idEmpleado, e.direccion, e.estadoEmpleado, e.fechaRegistro, e.imagenEmpleado_url, " +
                     "p.nombres, p.apPaterno, p.apMaterno, p.numDocumento, p.telefono, p.correo " +
                     "FROM Empleado e " +
                     "JOIN Persona p ON e.idPersona = p.idPersona " +
                     "WHERE e.idEmpleado = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    perfil = new HashMap<>();
                    perfil.put("idEmpleado", rs.getInt("idEmpleado"));
                    perfil.put("nombres", rs.getString("nombres") + " " + rs.getString("apPaterno"));
                    perfil.put("correo", rs.getString("correo"));
                    perfil.put("telefono", rs.getString("telefono"));
                    perfil.put("fotoUrl", rs.getString("imagenEmpleado_url"));
                    perfil.put("estado", rs.getString("estadoEmpleado"));
                }
            }
        }
        return perfil;
    }
    
    public boolean existeUsuario(String usuario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Credenciales WHERE usuario = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean existeDocumento(String numDocumento) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Persona WHERE numDocumento = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, numDocumento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean existeCorreo(String correo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Persona WHERE correo = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
