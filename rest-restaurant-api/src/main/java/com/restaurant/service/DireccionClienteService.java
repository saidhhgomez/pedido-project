package com.restaurant.service;

import com.restaurant.dao.DireccionClienteDAO;
import com.restaurant.model.DireccionCliente;

import java.util.List;

public class DireccionClienteService {

    private final DireccionClienteDAO direccionDAO = new DireccionClienteDAO();

    public boolean registrar(DireccionCliente d) {
        if (d == null || d.getIdCliente() <= 0) {
            System.out.println("Error: datos incompletos para registrar dirección.");
            return false;
        }
        return direccionDAO.registrarDireccion(d);
    }

    public boolean eliminar(int idDireccion) {
        if (idDireccion <= 0) {
            System.out.println("Error: ID inválido para eliminar.");
            return false;
        }
        return direccionDAO.eliminarDireccion(idDireccion);
    }

    public DireccionCliente obtenerPorId(int idDireccion) {
        return direccionDAO.obtenerPorId(idDireccion);
    }

    public List<DireccionCliente> listarPorCliente(int idCliente) {
        return direccionDAO.listarPorCliente(idCliente);
    }
}