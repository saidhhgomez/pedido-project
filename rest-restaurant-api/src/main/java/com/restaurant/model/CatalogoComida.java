package com.restaurant.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CatalogoComida {
    private int idCatalogo;
    private int idEmpleado;
    private String nombre;
    private String categoria;
    private double precio;
    private int stock;
    @JsonProperty("estadoplato")
    private boolean estadoPlato;
    private String imagenPlatoUrl;
	public int getIdCatalogo() {
		return idCatalogo;
	}
	public void setIdCatalogo(int idCatalogo) {
		this.idCatalogo = idCatalogo;
	}
	public int getIdEmpleado() {
		return idEmpleado;
	}
	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public boolean isEstadoPlato() {
		return estadoPlato;
	}
	public void setEstadoPlato(boolean estadoPlato) {
		this.estadoPlato = estadoPlato;
	}
	public String getImagenPlatoUrl() {
		return imagenPlatoUrl;
	}
	public void setImagenPlatoUrl(String imagenPlatoUrl) {
		this.imagenPlatoUrl = imagenPlatoUrl;
	}
    
}