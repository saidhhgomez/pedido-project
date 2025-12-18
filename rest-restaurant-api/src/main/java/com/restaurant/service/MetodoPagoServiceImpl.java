package com.restaurant.service;

import java.util.List;

import com.restaurant.dao.MetodoPagoDAO;
import com.restaurant.model.MetodoPago;

public class MetodoPagoServiceImpl implements MetodoPagoService {

    private final MetodoPagoDAO dao = new MetodoPagoDAO();

    @Override
    public List<MetodoPago> obtenerTodos() {
        return dao.obtenerTodos();
    }

    @Override
    public List<MetodoPago> obtenerActivos() {
        return dao.obtenerActivos();
    }

    @Override
    public MetodoPago obtenerPorId(int id) {
        return dao.obtenerPorId(id);
    }

    @Override
    public String agregar(MetodoPago pago) {
        if (dao.existeNombre(pago.getNombre())) {
            return "DUPLICADO";
        }
        return dao.agregar(pago) ? "OK" : "ERROR";
    }

    @Override
    public boolean actualizar(int id, MetodoPago pago) {

        if (id <= 0) {
            throw new IllegalArgumentException("El ID es obligatorio");
        }

        if (pago.getNombre() == null || pago.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        return dao.actualizar(id, pago);
    }

    @Override
    public boolean cambiarEstado(int id, String estado) {

        if (id <= 0) {
            throw new IllegalArgumentException("El ID es obligatorio");
        }

        if (!estado.equalsIgnoreCase("activo") && !estado.equalsIgnoreCase("inactivo")) {
            throw new IllegalArgumentException("Estado inválido");
        }

        return dao.cambiarEstado(id, estado);
    }


    @Override
    public boolean eliminarLogico(int id) {
        return dao.eliminarLogico(id);
    }
}
