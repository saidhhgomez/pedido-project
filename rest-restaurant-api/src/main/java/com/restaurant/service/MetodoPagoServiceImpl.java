package com.restaurant.service;

import java.util.List;
import com.restaurant.dao.MetodoPagoDAO;
import com.restaurant.model.MetodoPago;

public class MetodoPagoServiceImpl implements MetodoPagoService {

    private MetodoPagoDAO dao = new MetodoPagoDAO();

    @Override
    public List<MetodoPago> obtenerTodos() {
        return dao.obtenerTodos();
    }

    @Override
    public boolean agregar(MetodoPago pago) {
        return dao.agregar(pago);
    }

    @Override
    public boolean actualizar(int id, MetodoPago pago) {
        return dao.actualizar(id, pago);
    }

    @Override
    public boolean eliminar(int id) {
        return dao.eliminar(id);
    }

	@Override
	public MetodoPago obtenerPorId(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
