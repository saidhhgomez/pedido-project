package com.restaurant.service;

import com.restaurant.dao.RolDAO;
import com.restaurant.model.Rol;

import java.util.HashMap;
import java.util.List;

public class RolService {

    private final RolDAO dao = new RolDAO();

    public boolean crearRol(Rol rol) {
        if (rol.getNombre() == null || rol.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        }
        return dao.crearRol(rol);
    }

    public List<HashMap<String, Object>> listarRoles() {
        return dao.listarRoles();
    }

    public List<HashMap<String, Object>> listarActivos() {
        return dao.listarActivos();
    }

    public HashMap<String, Object> obtenerRolPorId(int idRol) {
        return dao.obtenerPorId(idRol);
    }

    public boolean actualizarRol(Rol rol) {
        if (rol.getIdRol() <= 0) throw new IllegalArgumentException("El ID del rol es obligatorio");
        if (rol.getNombre() == null || rol.getNombre().isEmpty())
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        return dao.actualizarRol(rol);
    }

    public boolean cambiarEstado(int id, String estado) {
        return dao.cambiarEstado(id, estado);
    }
}
