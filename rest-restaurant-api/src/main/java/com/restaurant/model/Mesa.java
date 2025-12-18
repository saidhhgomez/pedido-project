package com.restaurant.model;

public class Mesa {
    private int idMesa;
    private int idSucursal;
    private String numeroMesa;
    private int capacidad;
    private String ubicacion;
    private String estado;
    
	public int getIdMesa() {
		return idMesa;
	}
	public void setIdMesa(int idMesa) {
		this.idMesa = idMesa;
	}
	public int getIdSucursal() {
		return idSucursal;
	}
	public void setIdSucursal(int idSucursal) {
		this.idSucursal = idSucursal;
	}
	public String getNumeroMesa() {
		return numeroMesa;
	}
	public void setNumeroMesa(String numeroMesa) {
		this.numeroMesa = numeroMesa;
	}
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
}