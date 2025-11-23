package com.restaurant.model;

public class ClienteCompletoRequest {
    private Credenciales credenciales;
    private Persona persona;
    private Cliente cliente;
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
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}
