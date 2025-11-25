package com.restaurant.service;

import java.util.HashMap;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.restaurant.dao.LoginDAO;

public class LoginService {

    private LoginDAO loginDAO = new LoginDAO();

    public HashMap<String, Object> login(String usuario, String contrasena) {

        HashMap<String, Object> data = loginDAO.login(usuario);

        if (data == null) {
            return null; // USER NO EXISTE
        }

        String hash = (String) data.get("contrasena");

        // COMPARAR
        boolean coincide = BCrypt.checkpw(contrasena, hash);

        if (!coincide) {
            return null; // INCORRECTA
        }
        
        String token = "aun no";
        /*
        String token = JWTUtil.generarToken(
                data.get("id").toString(),
                data.get("rol").toString()
        );*/

        // QUITAR CONTRASENA
        data.remove("contrasena");

        // JSON FINAL
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("token", token);
        response.put("usuario", data);

        return response; // EL SERVICE SOLO DEVUELVE HASHMAP
    }
}