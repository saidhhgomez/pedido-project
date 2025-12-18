package com.restaurant.service;

import java.util.List;
import com.restaurant.model.MetodoPago;

public interface MetodoPagoService {

    List<MetodoPago> obtenerTodos();
    List<MetodoPago> obtenerActivos();
    MetodoPago obtenerPorId(int id);
    String agregar(MetodoPago pago);
    boolean actualizar(int id, MetodoPago pago);
    boolean cambiarEstado(int id, String estado);
    boolean eliminarLogico(int id);
}
