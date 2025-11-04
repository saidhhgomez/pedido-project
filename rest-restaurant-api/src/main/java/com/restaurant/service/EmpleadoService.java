package com.restaurant.service;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.restaurant.dao.RegistroDAO;
import com.restaurant.model.Empleado;
import com.restaurant.model.EmpleadoCompletoRequest;
import com.restaurant.model.Credenciales;
import com.restaurant.model.Persona;

public class EmpleadoService {
	private final RegistroDAO empleadoDAO = new RegistroDAO();

	public int registrarEmpleadoCompleto(EmpleadoCompletoRequest request) {
	    try {
	        Credenciales cred = request.getCredenciales();
	        Persona persona = request.getPersona();
	        Empleado empleado = request.getEmpleado();

	        if (cred == null || persona == null || empleado == null) {
	            System.out.println("Error: Algún objeto está nulo. cred: " + cred + ", persona: " + persona + ", empleado: " + empleado);
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

	        return empleadoDAO.registrarEmpleadoCompleto(cred, persona, empleado);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
}
