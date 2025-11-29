package com.restaurant.service;

import java.util.List;

import com.restaurant.dao.CatalogoComidaDAO;
import com.restaurant.model.CatalogoComida;

public class CatalogoComidaService {

    private CatalogoComidaDAO dao = new CatalogoComidaDAO();

    public List<CatalogoComida> listar() {
        return dao.obtenerTodos();
    }
    public boolean agregar(CatalogoComida d) {
    return dao.agregar(d);	
    }
    public boolean actualizar(int id, CatalogoComida d) {
        return dao.actualizar(id, d );
    }
    public boolean eliminar(int id) {
        return dao.eliminar(id);
    }
}
