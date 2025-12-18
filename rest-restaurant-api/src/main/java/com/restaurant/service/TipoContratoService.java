package com.restaurant.service;

import com.restaurant.dao.TipoContratoDAO;
import com.restaurant.model.TipoContrato;

import java.util.HashMap;
import java.util.List;

public class TipoContratoService {

    private final TipoContratoDAO dao = new TipoContratoDAO();

    public boolean crear(TipoContrato contrato) {
        if (contrato.getNombre() == null || contrato.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        return dao.crear(contrato);
    }

    public List<HashMap<String, Object>> listar() {
        return dao.listar();
    }

    public List<HashMap<String, Object>> listarActivos() {
        return dao.listarActivos();
    }

    public HashMap<String, Object> obtener(int id) {
        return dao.obtenerPorId(id);
    }

    public boolean actualizar(TipoContrato contrato) {
        if (contrato.getIdTipoContrato() <= 0) {
            throw new IllegalArgumentException("El ID es obligatorio");
        }
        if (contrato.getNombre() == null || contrato.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        return dao.actualizar(contrato);
    }

    public boolean cambiarEstado(int id, String estado) {
        return dao.cambiarEstado(id, estado);
    }
}
