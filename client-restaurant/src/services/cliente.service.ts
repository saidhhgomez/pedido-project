import { useMutation } from "@tanstack/react-query";
import axiosFormData from "./api.service.file"; // Tu instancia de axios con token

// ==============================
// TIPOS
// ==============================
export interface RegistrarClienteConArchivoDTO {
  data: any; // JSON con los datos del cliente
  imagenCliente: File; // PDF o imagen del cliente
}

// ==============================
// REGISTRAR CLIENTE (FormData)
// ==============================
export function registrarClienteFormData(payload: RegistrarClienteConArchivoDTO) {
  const formData = new FormData();

  // Datos del cliente como JSON
  formData.append("data", JSON.stringify(payload.data));

  // Archivo
  formData.append("imagenCliente", payload.imagenCliente);

  return axiosFormData.post(
    "rest-restaurant-api/api/cliente/registrar",
    formData
  );
}

// ==============================
// HOOK DE REACT QUERY
// ==============================
export function useRegistrarClienteFormData() {
  return useMutation({
    mutationFn: registrarClienteFormData,
  });
}
