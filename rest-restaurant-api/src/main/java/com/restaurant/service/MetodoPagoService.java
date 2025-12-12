package com.restaurant.service;

import java.util.List;
import com.restaurant.model.MetodoPago;

public interface MetodoPagoService {
    List<MetodoPago> obtenerTodos();
    MetodoPago obtenerPorId(int id);
    boolean agregar(MetodoPago pago);
    boolean actualizar(int id, MetodoPago pago);
    boolean eliminar(int id);
	
}
