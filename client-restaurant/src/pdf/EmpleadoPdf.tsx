// EmpleadoPdf.jsx
import React from "react";

export default function EmpleadoPdf({ data }) {
  return (
    <div
      id="empleado-pdf"
      style={{
        padding: "20px",
        fontFamily: "Arial",
        background: "white",
        color: "black",
        width: "100%",
      }}
    >
      <h2>Ficha del Empleado</h2>

      <h3>Credenciales</h3>
      <p><strong>Usuario:</strong> {data.usuario}</p>

      <h3>Datos Personales</h3>
      <p><strong>Nombres:</strong> {data.nombres}</p>
      <p><strong>Apellido Paterno:</strong> {data.apPaterno}</p>
      <p><strong>Apellido Materno:</strong> {data.apMaterno}</p>
      <p><strong>Tipo Documento:</strong> {data.tipoDocumento}</p>
      <p><strong>Número Documento:</strong> {data.numDocumento}</p>
      <p><strong>Género:</strong> {data.genero}</p>
      <p><strong>Teléfono:</strong> {data.telefono}</p>
      <p><strong>Correo:</strong> {data.correo}</p>
      <p><strong>Fecha Nacimiento:</strong> {data.fechaNacimiento}</p>

      <h3>Datos del Empleado</h3>
      <p><strong>Dirección:</strong> {data.direccion}</p>
      <p><strong>Estado:</strong> {data.estadoEmpleado}</p>
      <p><strong>URL Imagen:</strong> {data.imagenConductor_url}</p>

      <h3>Contrato</h3>
      <p><strong>Sucursal:</strong> {data.idSucursal}</p>
      <p><strong>Tipo Contrato:</strong> {data.idTipoContrato}</p>
      <p><strong>Rol:</strong> {data.idRol}</p>
      <p><strong>Fecha Inicio:</strong> {data.fechaInicio}</p>
      <p><strong>Fecha Fin:</strong> {data.fechaFin}</p>
      <p><strong>Salario:</strong> {data.salario}</p>
    </div>
  );
}
