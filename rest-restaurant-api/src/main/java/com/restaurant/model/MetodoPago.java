package com.restaurant.model;

public class MetodoPago {

    private int idFormaPago;
    private String nombre;
    private String estadoFormaPago;

    public int getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(int idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstadoFormaPago() {
        return estadoFormaPago;
    }

    public void setEstadoFormaPago(String estadoFormaPago) {
        this.estadoFormaPago = estadoFormaPago;
    }
}
