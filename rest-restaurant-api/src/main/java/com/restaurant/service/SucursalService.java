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

    public List<HashMap<String, Object>> listarSucursales() {
        return sucursalDAO.listarSucursales();
    }

    public HashMap<String, Object> obtenerSucursalPorId(int id) {
        return sucursalDAO.obtenerPorId(id);
    }

    public boolean actualizarSucursal(Sucursal s) {
        if (s.getIdSucursal() <= 0) {
            throw new IllegalArgumentException("El ID de la sucursal es obligatorio");
        }
        if (s.getNombre() == null || s.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        return sucursalDAO.actualizarSucursal(s);
    }

    public boolean eliminarSucursal(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID de la sucursal es obligatorio");
        }
        return sucursalDAO.eliminarSucursal(id);
    }
}
