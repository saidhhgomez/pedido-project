// EmpleadoPdf.jsx
import React from "react";

export default function EmpleadoPdf({ data }) {
  const sectionStyle = {
    marginBottom: "25px",
  };

  const labelStyle = {
    fontWeight: "bold",
  };

  const valueStyle = {
    marginLeft: "5px",
  };

  const lineStyle = {
    borderBottom: "1px solid #000",
    display: "inline-block",
    width: "200px",
    marginLeft: "5px",
  };

  return (
    <div
      id="empleado-pdf"
      style={{
        padding: "50px",
        fontFamily: "Times New Roman, serif",
        background: "white",
        color: "black",
        width: "100%",
        lineHeight: "1.7",
        fontSize: "14px",
      }}
    >
      <h1 style={{ textAlign: "center", textDecoration: "underline", marginBottom: "40px" }}>
        CONTRATO DE TRABAJO
      </h1>

      <p style={{ textAlign: "justify" }}>
        En la ciudad de <span style={valueStyle}>{data.sucursalTexto}</span>, a la fecha <span style={valueStyle}>{data.fechaInicio}</span>, se celebra el presente contrato de trabajo entre <strong>la Empresa</strong> y el Sr./Sra. <strong>{data.nombres} {data.apPaterno} {data.apMaterno}</strong>, identificado con {data.tipoDocumento} N° {data.numDocumento}, de acuerdo a las siguientes cláusulas:
      </p>

      {/* Datos Personales */}
      <div style={sectionStyle}>
        <h2 style={{ textDecoration: "underline" }}>I. Datos del Empleado</h2>
        <p><span style={labelStyle}>Nombres:</span> <span style={valueStyle}>{data.nombres}</span></p>
        <p><span style={labelStyle}>Apellido Paterno:</span> <span style={valueStyle}>{data.apPaterno}</span></p>
        <p><span style={labelStyle}>Apellido Materno:</span> <span style={valueStyle}>{data.apMaterno}</span></p>
        <p><span style={labelStyle}>Tipo de Documento:</span> <span style={valueStyle}>{data.tipoDocumento}</span></p>
        <p><span style={labelStyle}>Número de Documento:</span> <span style={valueStyle}>{data.numDocumento}</span></p>
        <p><span style={labelStyle}>Género:</span> <span style={valueStyle}>{data.genero}</span></p>
        <p><span style={labelStyle}>Teléfono:</span> <span style={valueStyle}>{data.telefono}</span></p>
        <p><span style={labelStyle}>Correo:</span> <span style={valueStyle}>{data.correo}</span></p>
        <p><span style={labelStyle}>Fecha de Nacimiento:</span> <span style={valueStyle}>{data.fechaNacimiento}</span></p>
        <p><span style={labelStyle}>Dirección:</span> <span style={valueStyle}>{data.direccion}</span></p>
      </div>

      {/* Contrato */}
      <div style={sectionStyle}>
        <h2 style={{ textDecoration: "underline" }}>II. Condiciones del Contrato</h2>
        <p><span style={labelStyle}>Sucursal:</span> <span style={valueStyle}>{data.sucursalTexto}</span></p>
        <p><span style={labelStyle}>Tipo de Contrato:</span> <span style={valueStyle}>{data.tipoContratoTexto}</span></p>
        <p><span style={labelStyle}>Rol:</span> <span style={valueStyle}>{data.rolTexto}</span></p>
        <p><span style={labelStyle}>Fecha de Inicio:</span> <span style={valueStyle}>{data.fechaInicio}</span></p>
        <p><span style={labelStyle}>Salario:</span> <span style={valueStyle}>S/. {data.salario}</span></p>
      </div>

      {/* Firma y cierre */}
      <div style={{ marginTop: "50px" }}>
        <p style={{ textAlign: "center" }}>
          _______________________________<br/>
          Firma del Empleado
        </p>
        <p style={{ textAlign: "center", marginTop: "30px" }}>
          _______________________________<br/>
          Firma del Representante de la Empresa
        </p>
      </div>
    </div>
  );
}
