package com.restaurant.service;

import com.restaurant.dao.DireccionClienteDAO;
import com.restaurant.model.DireccionCliente;

import java.util.List;

public class DireccionClienteService {

    private final DireccionClienteDAO direccionDAO = new DireccionClienteDAO();

    public void registrar(DireccionCliente d) throws Exception {
            if (d.getIdCliente() <= 0 || d.getDireccion() == null || d.getDireccion().isEmpty()) {
                throw new Exception("Datos obligatorios faltantes (Cliente o Dirección).");
            }

            if (direccionDAO.existeDireccion(d.getIdCliente(), d.getDireccion(), d.getDistrito())) {
                throw new Exception("Ya tienes registrada esta dirección en tu cuenta.");
            }

            if (!direccionDAO.registrarDireccion(d)) {
                throw new Exception("Error interno al intentar guardar la dirección.");
            }
    }

    public void eliminar(int idDireccion) throws Exception {
            if (idDireccion <= 0) throw new Exception("ID de dirección no válido.");
            
            if (!direccionDAO.eliminarLogico(idDireccion)) {
                throw new Exception("No se encontró la dirección o ya fue eliminada.");
            }
    }

    public DireccionCliente obtenerPorId(int idDireccion) {
        return direccionDAO.obtenerPorId(idDireccion);
    }

    public List<DireccionCliente> listarPorCliente(int idCliente) throws Exception {
        if (idCliente <= 0) {
            throw new Exception("ID de cliente inválido.");
        }
        
        List<DireccionCliente> lista = direccionDAO.listarActivasPorCliente(idCliente);
        return lista; 
    }
}