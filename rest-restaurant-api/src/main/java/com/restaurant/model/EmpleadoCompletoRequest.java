package com.restaurant.model;

public class EmpleadoCompletoRequest {
    private Credenciales credenciales;
    private Persona persona;
    private Empleado empleado;
    private Contrato contrato;
	public Credenciales getCredenciales() {
		return credenciales;
	}
	public void setCredenciales(Credenciales credenciales) {
		this.credenciales = credenciales;
	}
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	public Empleado getEmpleado() {
		return empleado;
	}
	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	public Contrato getContrato() {
		return contrato;
	}
	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}
}