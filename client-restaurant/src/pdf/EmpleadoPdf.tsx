// EmpleadoPdf.jsx
import React from "react";

export default function EmpleadoPdf({ data }) {
  const sectionStyle = {
    marginBottom: "20px",
    padding: "10px 0",
    borderBottom: "1px solid #000",
  };

  const labelStyle = {
    display: "block",
    fontWeight: "bold",
    marginBottom: "5px",
  };

  const valueStyle = {
    marginBottom: "10px",
  };

  return (
    <div
      id="empleado-pdf"
      style={{
        padding: "40px",
        fontFamily: "Times New Roman, serif",
        background: "white",
        color: "black",
        width: "100%",
        lineHeight: "1.6",
      }}
    >
      <h1 style={{ textAlign: "center", textDecoration: "underline" }}>
        Contrato de Empleado
      </h1>

      {/* Credenciales */}
      <div style={sectionStyle}>
        <h2>Credenciales</h2>
        <span style={labelStyle}>Usuario:</span>
        <span style={valueStyle}>{data.usuario}</span>
      </div>

      {/* Datos Personales */}
      <div style={sectionStyle}>
        <h2>Datos Personales</h2>
        <span style={labelStyle}>Nombres:</span>
        <span style={valueStyle}>{data.nombres}</span>

        <span style={labelStyle}>Apellido Paterno:</span>
        <span style={valueStyle}>{data.apPaterno}</span>

        <span style={labelStyle}>Apellido Materno:</span>
        <span style={valueStyle}>{data.apMaterno}</span>

        <span style={labelStyle}>Tipo Documento:</span>
        <span style={valueStyle}>{data.tipoDocumento}</span>

        <span style={labelStyle}>Número Documento:</span>
        <span style={valueStyle}>{data.numDocumento}</span>

        <span style={labelStyle}>Género:</span>
        <span style={valueStyle}>{data.genero}</span>

        <span style={labelStyle}>Teléfono:</span>
        <span style={valueStyle}>{data.telefono}</span>

        <span style={labelStyle}>Correo:</span>
        <span style={valueStyle}>{data.correo}</span>

        <span style={labelStyle}>Fecha de Nacimiento:</span>
        <span style={valueStyle}>{data.fechaNacimiento}</span>
      </div>

      {/* Datos del Empleado */}
      <div style={sectionStyle}>
        <h2>Datos del Empleado</h2>
        <span style={labelStyle}>Dirección:</span>
        <span style={valueStyle}>{data.direccion}</span>

        <span style={labelStyle}>Estado:</span>
        <span style={valueStyle}>{data.estadoEmpleado}</span>

        <span style={labelStyle}>URL Imagen:</span>
        <span style={valueStyle}>{data.imagenConductor_url}</span>
      </div>

      {/* Contrato */}
      <div style={sectionStyle}>
        <h2>Contrato</h2>
        <span style={labelStyle}>Sucursal:</span>
        <span style={valueStyle}>{data.idSucursal}</span>

        <span style={labelStyle}>Tipo Contrato:</span>
        <span style={valueStyle}>{data.idTipoContrato}</span>

        <span style={labelStyle}>Rol:</span>
        <span style={valueStyle}>{data.idRol}</span>

        <span style={labelStyle}>Fecha Inicio:</span>
        <span style={valueStyle}>{data.fechaInicio}</span>

        <span style={labelStyle}>Fecha Fin:</span>
        <span style={valueStyle}>{data.fechaFin}</span>

        <span style={labelStyle}>Salario:</span>
        <span style={valueStyle}>{data.salario}</span>
      </div>

      <p style={{ textAlign: "center", marginTop: "40px" }}>
        Firma del Empleado: _________________________
      </p>
      <p style={{ textAlign: "center", marginTop: "10px" }}>
        Firma del Representante: ____________________
      </p>
    </div>
  );
}
