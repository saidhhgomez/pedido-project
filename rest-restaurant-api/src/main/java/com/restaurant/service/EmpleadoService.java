package com.restaurant.service;

import java.util.HashMap;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.restaurant.dao.EmpleadoDAO;
import com.restaurant.dao.RegistroDAO;
import com.restaurant.dao.StorageFileDAO;
import com.restaurant.model.Empleado;
import com.restaurant.model.EmpleadoCompletoRequest;
import com.restaurant.model.EmpleadoExisteCompletoRequest;
import com.restaurant.model.Cliente;
import com.restaurant.model.Contrato;
import com.restaurant.model.Credenciales;
import com.restaurant.model.Persona;
import com.restaurant.util.BackblazeUtil;
import com.restaurant.config.DBConnection;
import com.restaurant.config.BackblazeConfig;
import com.restaurant.model.StorageFile;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EmpleadoService {
	private final RegistroDAO empleadoDAO = new RegistroDAO();
	private final EmpleadoDAO empleDAO = new EmpleadoDAO();
	private final StorageFileDAO storageFileDAO = new StorageFileDAO();
	
	private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg", "webp");
    
    private String generateUniqueFileName(String originalFileName, String numDocumento) {
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1).toLowerCase();
        
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        
        return numDocumento + "_" + uniqueId + "_" + timestamp + "." + extension;
    }

    public HashMap<String, Object> buscarPorDni(String dni) {

        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI es obligatorio");
        }

        if (dni.length() < 8 || dni.length() > 15) {
            throw new IllegalArgumentException("El DNI debe tener entre 8 y 15 caracteres");
        }

        HashMap<String, Object> data = empleadoDAO.buscarPorDni(dni);

        if (data == null) {
            throw new RuntimeException("No se encontró ninguna persona con ese DNI");
        }

        return data;
    }
    
    public boolean actualizarPersona(int idPersona, Persona persona) {

        if (persona == null) {
            System.out.println("Error: Persona nula");
            return false;
        }

        if (persona.getNombres() == null || persona.getNombres().trim().isEmpty()) {
            System.out.println("Error: Nombre vacío");
            return false;
        }

        if (persona.getNumDocumento() == null || persona.getNumDocumento().trim().isEmpty()) {
            System.out.println("Error: Numero documento vacío");
            return false;
        }

        if (persona.getCorreo() != null && !persona.getCorreo().contains("@")) {
            System.out.println("Error: Correo inválido");
            return false;
        }

        return empleadoDAO.actualizarPersona(idPersona, persona);
    }

    public int registrarEmpleadoCompleto(
            EmpleadoCompletoRequest request, 
            File pdfTempFile, 
            FormDataContentDisposition pdfFileDetail, 
            File imagenTempFile, 
            FormDataContentDisposition imagenFileDetail,
            int idAdmin) throws Exception {

        Credenciales cred = request.getCredenciales();
        Persona persona = request.getPersona();
        Empleado empleado = request.getEmpleado();
        Contrato contrato = request.getContrato();
        
        if (cred == null || persona == null || empleado == null || contrato == null ||
            cred.getUsuario() == null || cred.getContrasena() == null ||
            cred.getUsuario().isEmpty() || cred.getContrasena().isEmpty() ||
            (persona.getCorreo() != null && !persona.getCorreo().contains("@")) ||
            contrato.getIdSucursal() <= 0 || contrato.getIdTipoContrato() <= 0 ||
            contrato.getIdRol() <= 0 || contrato.getSalario() == null ||
            contrato.getFechaInicio() == null) {
            
            throw new Exception("Datos de registro o contrato incompletos/inválidos.");
        }

        validarFechasContrato(contrato.getFechaInicio(), contrato.getFechaFin());
        
        if (empleadoDAO.existeUsuario(cred.getUsuario())) {
            throw new Exception("El usuario '" + cred.getUsuario() + "' ya está en uso.");
        }
        if (empleadoDAO.existeDocumento(persona.getNumDocumento())) {
            throw new Exception("El documento '" + persona.getNumDocumento() + "' ya está registrado.");
        }
        if (persona.getCorreo() != null && empleadoDAO.existeCorreo(persona.getCorreo())) {
            throw new Exception("El correo '" + persona.getCorreo() + "' ya pertenece a otra persona.");
        }
        
        String numDocumento = persona.getNumDocumento();
        if (numDocumento == null || numDocumento.isEmpty()) {
            throw new Exception("El número de documento (numDocumento) de la Persona es obligatorio para las Keys de B2.");
        }
        
        cred.setContrasena(BCrypt.hashpw(cred.getContrasena(), BCrypt.gensalt()));

        List<String> b2KeysToRollback = new ArrayList<>();
        
        String imagenOriginalName = imagenFileDetail.getFileName();
        String imagenExtension = imagenOriginalName.substring(imagenOriginalName.lastIndexOf('.') + 1).toLowerCase();
        
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(imagenExtension)) {
            throw new Exception("Tipo de archivo de imagen no soportado.");
        }
        
        String imagenUniqueName = generateUniqueFileName(imagenOriginalName, numDocumento);
        
        String imagenEmpleadoKey = "";
        String imagenClienteKey = "";

        String imagenEmpleadoPath = "imagen_empleado/" + numDocumento; 
        try {
            imagenEmpleadoKey = BackblazeUtil.uploadFile(imagenEmpleadoPath + "/" + imagenUniqueName, imagenTempFile);
            b2KeysToRollback.add(imagenEmpleadoKey);
        } catch (Exception e) {
            throw new Exception("Fallo al subir la Imagen de Empleado a Backblaze B2: " + e.getMessage());
        }
        
        String imagenClientePath = "imagen_cliente/" + numDocumento;
        try {
            imagenClienteKey = BackblazeUtil.uploadFile(imagenClientePath + "/" + imagenUniqueName, imagenTempFile);
            b2KeysToRollback.add(imagenClienteKey);
        } catch (Exception e) {
            BackblazeUtil.deleteFile(imagenEmpleadoKey); 
            throw new Exception("Fallo al subir la Imagen de Cliente a Backblaze B2: " + e.getMessage());
        }
        
        String pdfOriginalFileName = pdfFileDetail.getFileName();
        String pdfUniqueName = generateUniqueFileName(pdfOriginalFileName, numDocumento);
        String pdfKeyPath = "contratos/firmados/" + numDocumento; 
        String pdfB2KeyCompleta = "";

        try {
            pdfB2KeyCompleta = BackblazeUtil.uploadFile(pdfKeyPath + "/" + pdfUniqueName, pdfTempFile);
            b2KeysToRollback.add(pdfB2KeyCompleta);
        } catch (Exception e) {
            BackblazeUtil.deleteFile(imagenEmpleadoKey);
            BackblazeUtil.deleteFile(imagenClienteKey);
            throw new Exception("Fallo al subir el Contrato PDF a Backblaze B2: " + e.getMessage());
        }
        
        Cliente cliente = new Cliente();
        cliente.setImagenCliente_url(imagenClienteKey);
        empleado.setImagenConductor_url(imagenEmpleadoKey); 
        contrato.setPdfFirmadoKey(pdfB2KeyCompleta); 
        
        StorageFile pdfStorageFile = prepareStorageFile(
                pdfB2KeyCompleta, 
                pdfUniqueName, 
                "Contrato", 
                0, 
                idAdmin, 
                pdfFileDetail, 
                pdfTempFile
        );

        StorageFile imagenEmpleadoStorageFile = prepareStorageFile(
                imagenEmpleadoKey, 
                imagenUniqueName, 
                "Empleado", 
                0, 
                idAdmin, 
                imagenFileDetail, 
                imagenTempFile
        );
        
        StorageFile imagenClienteStorageFile = prepareStorageFile(
                imagenClienteKey, 
                imagenUniqueName, 
                "Cliente", 
                0, 
                idAdmin, 
                imagenFileDetail, 
                imagenTempFile
        ); 
        
        List<StorageFile> storageFiles = Arrays.asList(pdfStorageFile, imagenEmpleadoStorageFile, imagenClienteStorageFile);
        
        try {
             return empleadoDAO.registrarEmpleadoCompleto(
                    cred, 
                    persona, 
                    empleado, 
                    contrato, 
                    cliente,
                    storageFiles
             );

        } catch (Exception e) {
            System.out.println("Error fatal en EmpleadoService durante la transacción: " + e.getMessage());
            for (String key : b2KeysToRollback) {
                try { BackblazeUtil.deleteFile(key); } catch (Exception ex) {}
            }
            throw e; 
        }
    }
	    
    public int registrarEmpleadoExisteCompleto(
            EmpleadoExisteCompletoRequest request, 
            File pdfTempFile, 
            FormDataContentDisposition pdfFileDetail,
            File imagenTempFile,
            FormDataContentDisposition imagenFileDetail,
            int idAdmin) throws Exception {
    	
    	Contrato contrato = request.getContrato();

        int idPersona = request.getPersona().getIdPersona();
        String numDoc = request.getPersona().getNumDocumento();
        
        if (numDoc == null || numDoc.isEmpty()) {
            throw new Exception("El número de documento es obligatorio para procesar los archivos.");
        }
        
        validarFechasContrato(contrato.getFechaInicio(), contrato.getFechaFin());
        
        if (empleadoDAO.tieneContratoActivo(idPersona)) {
            throw new Exception("BLOQUEO: Esta persona ya cuenta con un contrato vigente activo.");
        }

        String pdfUniqueName = generateUniqueFileName(pdfFileDetail.getFileName(), numDoc);
        String imgUniqueName = generateUniqueFileName(imagenFileDetail.getFileName(), numDoc);
        
        String pdfB2Path = "contratos/firmados/" + numDoc + "/" + pdfUniqueName;
        String imgB2Path = "imagen_empleado/" + numDoc + "/" + imgUniqueName;

        String pdfKey = "";
        String imgKey = "";
        List<String> b2KeysToRollback = new ArrayList<>();

        try {
            imgKey = BackblazeUtil.uploadFile(imgB2Path, imagenTempFile);
            b2KeysToRollback.add(imgKey);
            
            pdfKey = BackblazeUtil.uploadFile(pdfB2Path, pdfTempFile);
            b2KeysToRollback.add(pdfKey);
        } catch (Exception e) {
            for (String key : b2KeysToRollback) {
                try { BackblazeUtil.deleteFile(key); } catch (Exception ex) {}
            }
            throw new Exception("Error al subir archivos a Backblaze: " + e.getMessage());
        }
        
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int idEmpleadoFinal = empleadoDAO.obtenerIdEmpleadoSiExiste(idPersona);
            Empleado empData = request.getEmpleado();
            empData.setImagenConductor_url(imgKey);

            if (idEmpleadoFinal > 0) {
                empleadoDAO.reactivarEmpleado(empData, idEmpleadoFinal, conn);
            } else {
                idEmpleadoFinal = empleadoDAO.insertarEmpleado(empData, idPersona, conn);
            }

            Contrato conData = request.getContrato();
            conData.setPdfFirmadoKey(pdfKey);
            int idContratoGenerado = empleadoDAO.insertarContrato(conData, idEmpleadoFinal, conn);

            if (idContratoGenerado <= 0) throw new Exception("Error al generar el registro de contrato.");

            StorageFile pdfMeta = prepareStorageFile(
                pdfKey, 
                pdfUniqueName, 
                "Contrato", 
                idContratoGenerado, 
                idAdmin, 
                pdfFileDetail, 
                pdfTempFile
            );

            StorageFile imgMeta = prepareStorageFile(
                imgKey, 
                imgUniqueName, 
                "Empleado", 
                idEmpleadoFinal, 
                idAdmin, 
                imagenFileDetail, 
                imagenTempFile
            );

            storageFileDAO.insertFileMetadata(conn, pdfMeta);
            storageFileDAO.insertFileMetadata(conn, imgMeta);

            conn.commit();
            return idEmpleadoFinal;

        } catch (Exception e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            for (String key : b2KeysToRollback) {
                try { BackblazeUtil.deleteFile(key); } catch (Exception ex) {}
            }
            
            throw e;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // MÉTODO AUXILIAR CORREGIDO
    private StorageFile prepareStorageFile(
            String b2Key, 
            String uniqueName, 
            String relatedTable, 
            int relatedId, 
            int uploadedBy, 
            FormDataContentDisposition fileDetail, 
            File tempFile) {
        
        StorageFile storageFile = new StorageFile();
        storageFile.setBucket(BackblazeConfig.getBucketName());
        storageFile.setObjectKey(b2Key);
        storageFile.setFilename(uniqueName);
        storageFile.setContentType(fileDetail.getType());
        storageFile.setSize(tempFile.length());
        storageFile.setRelatedTable(relatedTable);
        storageFile.setRelatedId(relatedId);
        storageFile.setUploadedBy(uploadedBy);
        return storageFile;
    }
    
    public List<HashMap<String, Object>> listarActivosVigentes() {
        List<HashMap<String, Object>> lista = empleDAO.listarEmpleados("SOLO_VIGENTES");
        for (HashMap<String, Object> emp : lista) {
            transformarKeysAUrls(emp);
        }
        return lista;
    }

    public List<HashMap<String, Object>> listarTodoParaGestion() {
        List<HashMap<String, Object>> lista = empleDAO.listarEmpleados("GESTION_TOTAL");
        for (HashMap<String, Object> emp : lista) {
            transformarKeysAUrls(emp);
        }
        return lista;
    }
    
    public HashMap<String, Object> darDeBajaEmpleado(int idEmpleado) {
        HashMap<String, Object> res = new HashMap<>();
        Connection cn = null;

        try {
            cn = DBConnection.getConnection();
            cn.setAutoCommit(false);

            empleDAO.finalizarContratoActivo(idEmpleado, cn);

            boolean empleadoInactivado = empleDAO.inactivarEmpleado(idEmpleado, cn);

            if (empleadoInactivado) {
                cn.commit();
                res.put("success", true);
                res.put("message", "Empleado cesado y contrato finalizado correctamente");
            } else {
                cn.rollback();
                res.put("success", false);
                res.put("message", "No se pudo encontrar al empleado para dar de baja");
            }

        } catch (Exception e) {
            try {
                if (cn != null) cn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            res.put("success", false);
            res.put("message", "Error en la transacción: " + e.getMessage());
        } finally {
            try {
                if (cn != null) cn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return res;
    }

    private void transformarKeysAUrls(HashMap<String, Object> emp) {
        final String BASE_URL = BackblazeConfig.getPublicFileEndpoint(); 
        
        String fotoKey = (String) emp.get("fotoUrl");
        if (fotoKey != null && !fotoKey.isEmpty()) {
            emp.put("fotoUrl", BASE_URL + fotoKey);
        }
        String pdfKey = (String) emp.get("pdfKey");
        if (pdfKey != null && !pdfKey.isEmpty()) {
            emp.put("pdfDownloadUrl", BASE_URL + pdfKey);
        }
    }
    
    private void validarFechasContrato(Date fechaInicio, Date fechaFin) throws Exception {
        long ahora = System.currentTimeMillis();
        long haceUnMes = ahora - (30L * 24 * 60 * 60 * 1000);

        if (fechaInicio.getTime() < haceUnMes) {
            throw new Exception("La fecha de inicio es demasiado antigua. No puede ser mayor a 30 días atrás.");
        }

        if (fechaFin != null && fechaFin.before(fechaInicio)) {
            throw new Exception("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
    }
    
    public List<HashMap<String, Object>> listarHistorialContratos(int idEmpleado) throws Exception {
        List<HashMap<String, Object>> lista = empleDAO.obtenerHistorialContratos(idEmpleado);
        
        if (lista.isEmpty()) {
            throw new Exception("No se encontró historial para el empleado con ID: " + idEmpleado);
        }

        for (HashMap<String, Object> contrato : lista) {
            transformarKeysAUrls(contrato);
        }

        return lista;
    }
    
    public List<HashMap<String, Object>> obtenerRepartidoresActivos(int idSucursal) throws Exception {
        List<HashMap<String, Object>> lista = empleDAO.listarRepartidoresPorSucursal(idSucursal);
        
        if (lista.isEmpty()) {
            throw new Exception("No hay repartidores con contrato activo para esta sucursal.");
        }
        
        return lista;
    }
    
    public HashMap<String, Object> obtenerPerfilEmpleadoCompleto(int idEmpleado) throws Exception {
        HashMap<String, Object> perfil = empleadoDAO.obtenerPerfilBasico(idEmpleado);
        if (perfil == null) throw new Exception("Empleado no encontrado.");

        List<HashMap<String, Object>> historial = empleDAO.obtenerHistorialContratos(idEmpleado);

        transformarKeysAUrls(perfil); 

        for (HashMap<String, Object> contrato : historial) {
            transformarKeysAUrls(contrato);
        }

        perfil.put("historialContratos", historial);
        
        return perfil;
    }
}