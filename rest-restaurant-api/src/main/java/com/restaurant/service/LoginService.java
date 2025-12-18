package com.restaurant.service;

import java.util.HashMap;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.restaurant.config.BackblazeConfig;
import com.restaurant.dao.LoginDAO;

public class LoginService {

    private LoginDAO loginDAO = new LoginDAO();

    public HashMap<String, Object> login(String usuario, String contrasena) {
        HashMap<String, Object> data = loginDAO.login(usuario);

        if (data == null) return null;

        String hash = (String) data.get("contrasena");
        if (!BCrypt.checkpw(contrasena, hash)) return null;

        final String BASE_URL = BackblazeConfig.getPublicFileEndpoint();
        
        String fotoCliente = (String) data.get("fotoCliente");
        String fotoEmpleado = (String) data.get("fotoEmpleado");
        
        String urlFinalFoto = (fotoEmpleado != null && !fotoEmpleado.isEmpty()) 
                              ? BASE_URL + fotoEmpleado 
                              : (fotoCliente != null ? BASE_URL + fotoCliente : null);

        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("token", "token_pendiente_jwt");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("idPersona", data.get("idPersona"));
        userMap.put("nombre", data.get("nombreCompleto"));
        userMap.put("correo", data.get("correo"));
        userMap.put("fotoUrl", urlFinalFoto);
        userMap.put("rolPrincipal", data.get("rol"));
        response.put("usuario", userMap);

        if ((int)data.get("idCliente") > 0) {
            HashMap<String, Object> clienteMap = new HashMap<>();
            clienteMap.put("idCliente", data.get("idCliente"));
            clienteMap.put("categoria", data.get("categoriaCliente"));
            response.put("perfilCliente", clienteMap);
        }

        int idEmp = (int) data.get("idEmpleado");
        String estadoEmp = (String) data.get("estadoEmpleado");
        
        if (idEmp > 0) {
            HashMap<String, Object> empleadoMap = new HashMap<>();
            empleadoMap.put("idEmpleado", idEmp);
            empleadoMap.put("estado", estadoEmp);
            empleadoMap.put("idSucursal", data.get("idSucursal"));
            empleadoMap.put("nombreSucursal", data.get("nombreSucursal"));
            
            if (!"activo".equalsIgnoreCase(estadoEmp)) {
                userMap.put("rolPrincipal", "cliente");
            }
            
            response.put("perfilEmpleado", empleadoMap);
        }

        return response;
    }
}