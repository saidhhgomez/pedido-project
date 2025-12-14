package com.restaurant.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Contrato {
	private int idContrato;
	private int idEmpleado;
	private int idSucursal;
	private int idTipoContrato;
	private int idRol;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaInicio;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaFin;
	private BigDecimal salario;
	private String estadoContrato;
	private String pdfGeneradoKey;
    private String pdfFirmadoKey;
    
    public Contrato() {
    }
    
	public int getIdContrato() {
		return idContrato;
	}
	public void setIdContrato(int idContrato) {
		this.idContrato = idContrato;
	}
	public int getIdEmpleado() {
		return idEmpleado;
	}
	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
	public int getIdSucursal() {
		return idSucursal;
	}
	public void setIdSucursal(int idSucursal) {
		this.idSucursal = idSucursal;
	}
	public int getIdTipoContrato() {
		return idTipoContrato;
	}
	public void setIdTipoContrato(int idTipoContrato) {
		this.idTipoContrato = idTipoContrato;
	}
	public int getIdRol() {
		return idRol;
	}
	public void setIdRol(int idRol) {
		this.idRol = idRol;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public BigDecimal getSalario() {
		return salario;
	}
	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}
	public String getEstadoContrato() {
		return estadoContrato;
	}
	public void setEstadoContrato(String estadoContrato) {
		this.estadoContrato = estadoContrato;
	}
	public String getPdfGeneradoKey() {
        return pdfGeneradoKey;
    }

    public void setPdfGeneradoKey(String pdfGeneradoKey) {
        this.pdfGeneradoKey = pdfGeneradoKey;
    }

    public String getPdfFirmadoKey() {
        return pdfFirmadoKey;
    }

    public void setPdfFirmadoKey(String pdfFirmadoKey) {
        this.pdfFirmadoKey = pdfFirmadoKey;
    }
}