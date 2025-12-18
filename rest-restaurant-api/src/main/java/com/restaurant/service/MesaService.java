package com.restaurant.service;

import com.restaurant.dao.MesaDAO;
import com.restaurant.model.Mesa;
import java.util.HashMap;
import java.util.List;

public class MesaService {
    private final MesaDAO mesaDAO = new MesaDAO();

    private String formatearNumero(String num) {
        if (num == null) return null;
        String s = num.trim().toUpperCase();
        if (!s.startsWith("M")) {
            try {
                return String.format("M%02d", Integer.parseInt(s));
            } catch (Exception e) { return "M" + s; }
        }
        return s;
    }

    public HashMap<String, Object> crear(Mesa mesa) {
        HashMap<String, Object> res = new HashMap<>();
        mesa.setNumeroMesa(formatearNumero(mesa.getNumeroMesa()));

        if (mesaDAO.existeNumeroMesa(mesa.getNumeroMesa(), mesa.getIdSucursal(), 0)) {
            res.put("success", false);
            res.put("message", "El número de mesa ya existe en esta sucursal.");
            return res;
        }

        boolean ok = mesaDAO.crearMesa(mesa);
        res.put("success", ok);
        res.put("message", ok ? "Mesa creada con éxito" : "Error al guardar en BD");
        return res;
    }

    public List<HashMap<String, Object>> listarParaMesero(int idSucursal) throws Exception {
        List<HashMap<String, Object>> lista = mesaDAO.listarMesas(idSucursal, "OPERATIVO");
        
        if (lista.isEmpty()) {
            throw new Exception("No hay mesas operativas disponibles para la sucursal ID: " + idSucursal);
        }
        
        return lista;
    }

    public List<HashMap<String, Object>> listarParaAdmin(int idSucursal) throws Exception {
        List<HashMap<String, Object>> lista = mesaDAO.listarMesas(idSucursal, "TODOS");
        
        if (lista.isEmpty()) {
            throw new Exception("No se encontraron mesas registradas en el sistema para esta sucursal.");
        }
        
        return lista;
    }

    public HashMap<String, Object> actualizar(Mesa mesa) {
        HashMap<String, Object> res = new HashMap<>();
        mesa.setNumeroMesa(formatearNumero(mesa.getNumeroMesa()));

        if (mesaDAO.existeNumeroMesa(mesa.getNumeroMesa(), mesa.getIdSucursal(), mesa.getIdMesa())) {
            res.put("success", false);
            res.put("message", "No se puede actualizar: el número de mesa ya está en uso.");
            return res;
        }

        boolean ok = mesaDAO.actualizarMesa(mesa);
        res.put("success", ok);
        res.put("message", ok ? "Mesa actualizada correctamente" : "Error al actualizar");
        return res;
    }

    public boolean eliminarLogico(int id) {
        return mesaDAO.cambiarEstado(id, "inactivo");
    }
}