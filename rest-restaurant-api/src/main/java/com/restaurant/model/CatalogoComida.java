package com.restaurant.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CatalogoComida {
	private int idCatalogo;
    private String nombre;
    private String categoria;
    private Double precio; 
    private Integer stock; 
    
    @JsonProperty("estadoplato")
    private boolean estadoPlato;
    private String imagenPlatoUrl;
    
	public int getIdCatalogo() {
		return idCatalogo;
	}
	public void setIdCatalogo(int idCatalogo) {
		this.idCatalogo = idCatalogo;
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
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
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