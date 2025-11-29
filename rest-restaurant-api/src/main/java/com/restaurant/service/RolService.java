package com.restaurant.service;

import com.restaurant.dao.RolDAO;
import com.restaurant.model.Rol;

import java.util.HashMap;
import java.util.List;

public class RolService {

    private final RolDAO rolDAO = new RolDAO();

    public boolean crearRol(Rol rol) {
        if (rol.getNombre() == null || rol.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        }
        return rolDAO.crearRol(rol);
    }

    public List<HashMap<String, Object>> listarRoles() {
        return rolDAO.listarRoles();
    }

    public HashMap<String, Object> obtenerRolPorId(int idRol) {
        return rolDAO.obtenerPorId(idRol);
    }

    public boolean actualizarRol(Rol rol) {
        if (rol.getIdRol() <= 0) {
            throw new IllegalArgumentException("El ID del rol es obligatorio");
        }
        if (rol.getNombre() == null || rol.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        }
        return rolDAO.actualizarRol(rol);
    }

    public boolean eliminarRol(int idRol) {
        if (idRol <= 0) {
            throw new IllegalArgumentException("El ID del rol es obligatorio");
        }
        return rolDAO.eliminarRol(idRol);
    }
}