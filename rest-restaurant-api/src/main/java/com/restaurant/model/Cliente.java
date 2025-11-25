package com.restaurant.model;

import java.sql.Date;

public class Cliente {
    private int idCliente;
    private int idPersona;
    private Date fechaRegistro;
    private String categoria;
    private String imagenCliente_url;
	public int getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	public int getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(int idPersona) {
		this.idPersona = idPersona;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getImagenCliente_url() {
		return imagenCliente_url;
	}
	public void setImagenCliente_url(String imagenCliente_url) {
		this.imagenCliente_url = imagenCliente_url;
	}
}
