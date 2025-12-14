import { useMutation } from "@tanstack/react-query";
import axiosFormData from "./api.service.file";

// ==============================
// TIPOS
// ==============================

export interface CrearEmpleadoNuevoFD {
  data: any; // JSON COMPLETO (credenciales + persona + empleado + contrato)
  pdfFirmado: File;
  imagenEmpleado: File;
}

export interface CrearEmpleadoExistenteFD {
  data: any; // JSON (persona.idPersona + empleado + contrato)
  pdfFirmado: File;
  imagenEmpleado: File;
}

// ==============================
// EMPLEADO NUEVO (FormData)
// POST /api/empleado/registrar?idAdmin=1
// ==============================

export function crearEmpleadoNuevoFormData(payload: CrearEmpleadoNuevoFD) {
  const formData = new FormData();

  // JSON
  formData.append("data", JSON.stringify(payload.data));

  // Archivos
  formData.append("pdfFirmado", payload.pdfFirmado);
  formData.append("imagenEmpleado", payload.imagenEmpleado);

  return axiosFormData.post(
    "rest-restaurant-api/api/empleado/registrar?idAdmin=1",
    formData
  );
}

export function useCrearEmpleadoNuevoFormData() {
  return useMutation({
    mutationFn: crearEmpleadoNuevoFormData,
  });
}

// ==============================
// EMPLEADO EXISTENTE (FormData)
// POST /api/empleado/contrato-existente?idAdmin=1
// ==============================

export function crearEmpleadoContratoExistenteFormData(
  payload: CrearEmpleadoExistenteFD
) {
  const formData = new FormData();

  // JSON
  formData.append("data", JSON.stringify(payload.data));

  // Archivos
  formData.append("pdfFirmado", payload.pdfFirmado);
  formData.append("imagenEmpleado", payload.imagenEmpleado);

  return axiosFormData.post(
    "rest-restaurant-api/api/empleado/contrato-existente?idAdmin=1",
    formData
  );
}

export function useCrearEmpleadoContratoExistenteFormData() {
  return useMutation({
    mutationFn: crearEmpleadoContratoExistenteFormData,
  });
}
