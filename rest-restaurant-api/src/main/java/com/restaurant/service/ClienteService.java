package com.restaurant.service;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.restaurant.dao.RegistroDAO;
import com.restaurant.model.Cliente;
import com.restaurant.model.ClienteCompletoRequest;
import com.restaurant.model.Credenciales;
import com.restaurant.model.Persona;

public class ClienteService {
	private final RegistroDAO clientDAO = new RegistroDAO();
	
	public int registrarClienteCompleto(ClienteCompletoRequest request) {
	    try {
	        Credenciales cred = request.getCredenciales();
	        Persona persona = request.getPersona();
	        Cliente cliente = request.getCliente();

	        if (cred == null || persona == null || cliente == null) {
	            System.out.println("Error: Algún objeto está nulo. cred: " + cred + ", persona: " + persona + ", empleado: " + cliente);
	            return 0;
	        }
	        if (cred.getUsuario() == null || cred.getContrasena() == null ||
	            cred.getUsuario().isEmpty() || cred.getContrasena().isEmpty()) {
	            System.out.println("Error: Usuario o contraseña vacíos.");
	            return 0;
	        }

	        String passwordEncriptada = BCrypt.hashpw(cred.getContrasena(), BCrypt.gensalt());
	        cred.setContrasena(passwordEncriptada);

	        if (persona.getCorreo() != null && !persona.getCorreo().contains("@")) {
	            System.out.println("Error: Correo no válido.");
	            return 0;
	        }

	        return clientDAO.registrarClienteCompleto(cred, persona, cliente);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
}
