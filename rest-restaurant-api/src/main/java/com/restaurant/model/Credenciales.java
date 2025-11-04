package com.restaurant.model;

import java.util.Date;

public class Credenciales {
    private int idCredencial;
    private String usuario;
    private String contrasena;
    private Date fechaCreacion;
	public int getIdCredencial() {
		return idCredencial;
	}
	public void setIdCredencial(int idCredencial) {
		this.idCredencial = idCredencial;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getContrasena() {
		return contrasena;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}
