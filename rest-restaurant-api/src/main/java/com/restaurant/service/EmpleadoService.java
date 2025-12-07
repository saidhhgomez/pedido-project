package com.restaurant.service;

import java.util.HashMap;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.restaurant.dao.RegistroDAO;
import com.restaurant.model.Empleado;
import com.restaurant.model.EmpleadoCompletoRequest;
import com.restaurant.model.EmpleadoExisteCompletoRequest;
import com.restaurant.model.Contrato;
import com.restaurant.model.Credenciales;
import com.restaurant.model.Persona;

public class EmpleadoService {
	private final RegistroDAO empleadoDAO = new RegistroDAO();

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

    public int registrarEmpleadoCompleto(EmpleadoCompletoRequest request) {
        try {
            Credenciales cred = request.getCredenciales();
            Persona persona = request.getPersona();
            Empleado empleado = request.getEmpleado();
            Contrato contrato = request.getContrato();

            if (cred == null || persona == null || empleado == null || contrato == null) {
                System.out.println("Error: Algún objeto está nulo. cred: " + cred + 
                                   ", persona: " + persona + 
                                   ", empleado: " + empleado +
                                   ", contrato: " + contrato);
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

            if (contrato.getIdSucursal() <= 0 ||
                contrato.getIdTipoContrato() <= 0 ||
                contrato.getIdRol() <= 0 ||
                contrato.getSalario() == null ||
                contrato.getFechaInicio() == null ||
                contrato.getFechaFin() == null) {

                System.out.println("Error: Datos de contrato incompletos.");
                return 0;
            }
            
            return empleadoDAO.registrarEmpleadoCompleto(cred, persona, empleado, contrato);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
	
    public int registrarEmpleadoExisteCompleto(EmpleadoExisteCompletoRequest request) {

        try {
            if (request == null) {
                System.out.println("Error: El request está vacío.");
                return 0;
            }

            Persona persona = request.getPersona();
            Empleado empleado = request.getEmpleado();
            Contrato contrato = request.getContrato();

            if (persona == null || empleado == null || contrato == null) {
                System.out.println("Error: Los datos enviados están incompletos.");
                return 0;
            }

            if (persona.getIdPersona() <= 0) {
                System.out.println("Error: El idPersona es inválido.");
                return 0;
            }

            if (empleado.getDireccion() == null || empleado.getDireccion().isEmpty()) {
                System.out.println("Error: La dirección del empleado es obligatoria.");
                return 0;
            }

            if (empleado.getImagenConductor_url() == null) {
                empleado.setImagenConductor_url("");
            }

            if (contrato.getIdSucursal() <= 0 ||
                contrato.getIdTipoContrato() <= 0 ||
                contrato.getIdRol() <= 0) {

                System.out.println("Error: Sucursal, TipoContrato y Rol son obligatorios.");
                return 0;
            }

            if (contrato.getFechaInicio() == null || contrato.getFechaFin() == null) {
                System.out.println("Error: Las fechas del contrato son obligatorias.");
                return 0;
            }

            if (contrato.getSalario() == null || contrato.getSalario().doubleValue() <= 0) {
                System.out.println("Error: El salario debe ser mayor a cero.");
                return 0;
            }
            
            return empleadoDAO.registrarEmpleadoExisteCompleto(
                    empleado,
                    contrato,
                    persona.getIdPersona()
            );

        } catch (Exception e) {
            System.out.println("Error en EmpleadoService: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}