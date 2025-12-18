package com.restaurant.service;

import com.restaurant.dao.SucursalDAO;
import com.restaurant.model.Sucursal;

import java.util.HashMap;
import java.util.List;

public class SucursalService {

    private final SucursalDAO sucursalDAO = new SucursalDAO();

    public boolean crearSucursal(Sucursal s) {
        if (s.getNombre() == null || s.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        return sucursalDAO.crearSucursal(s);
    }
    
    public List<HashMap<String, Object>> listarSucursalesActivas() {
        return sucursalDAO.listarSucursalesActivas();
    }

    public List<HashMap<String, Object>> listarSucursales() {
        return sucursalDAO.listarSucursales();
    }

    public HashMap<String, Object> obtenerSucursalPorId(int id) {
        return sucursalDAO.obtenerPorId(id);
    }

    public boolean actualizarSucursal(Sucursal s) {
        if (s.getIdSucursal() <= 0) {
            throw new IllegalArgumentException("El ID es obligatorio");
        }
        if (s.getNombre() == null || s.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        return sucursalDAO.actualizarSucursal(s);
    }

    public boolean cambiarEstado(int id, String estado) {
        return sucursalDAO.cambiarEstado(id, estado);
    }

    public boolean eliminarLogico(int id) {
        return sucursalDAO.cambiarEstado(id, "inactivo");
    }
}

