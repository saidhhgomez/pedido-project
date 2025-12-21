import { useMutation } from "@tanstack/react-query";
import axiosFormData from "./api.service.file";

// ==============================
// TIPOS
// ==============================

export interface CrearEmpleadoNuevoFD {
  data: any;
  pdfFirmado: File;
  imagenEmpleado: File;
  idAdmin: number;
}


export interface CrearEmpleadoExistenteFD {
  data: any;
  pdfFirmado: File;
  imagenEmpleado: File;
  idAdmin: number;
}


// ==============================
// EMPLEADO NUEVO (FormData)
// POST /api/empleado/registrar?idAdmin=1
// ==============================
function crearEmpleadoNuevoFormData(payload: CrearEmpleadoNuevoFD) {
  const formData = new FormData();

  formData.append("data", JSON.stringify(payload.data));
  formData.append("pdfFirmado", payload.pdfFirmado);
  formData.append("imagenEmpleado", payload.imagenEmpleado);

  return axiosFormData.post(
    `rest-restaurant-api/api/empleado/registrar?idAdmin=${payload.idAdmin}`,
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

function crearEmpleadoContratoExistenteFormData(
  payload: CrearEmpleadoExistenteFD
) {
  const formData = new FormData();

  formData.append("data", JSON.stringify(payload.data));
  formData.append("pdfFirmado", payload.pdfFirmado);
  formData.append("imagenEmpleado", payload.imagenEmpleado);

  return axiosFormData.post(
    `rest-restaurant-api/api/empleado/contrato-existente?idAdmin=${payload.idAdmin}`,
    formData
  );
}

export function useCrearEmpleadoContratoExistenteFormData() {
  return useMutation({
    mutationFn: crearEmpleadoContratoExistenteFormData,
  });
}
