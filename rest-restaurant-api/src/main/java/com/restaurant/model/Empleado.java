package com.restaurant.model;

import java.util.Date;

public class Empleado {
    private int idEmpleado;
    private int idPersona;
    private String direccion;
    private String estadoEmpleado;
    private Date fechaRegistro;
    private String imagenConductor_url;
	public int getIdEmpleado() {
		return idEmpleado;
	}
	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
	public int getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(int idPersona) {
		this.idPersona = idPersona;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getEstadoEmpleado() {
		return estadoEmpleado;
	}
	public void setEstadoEmpleado(String estadoEmpleado) {
		this.estadoEmpleado = estadoEmpleado;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public String getImagenConductor_url() {
		return imagenConductor_url;
	}
	public void setImagenConductor_url(String imagenConductor_url) {
		this.imagenConductor_url = imagenConductor_url;
	}
}
