import { useMutation } from "@tanstack/react-query";
import axiosFormData from "./api.service.file";

// ==============================
// REGISTRAR CLIENTE (FormData)
// ==============================
export function registrarClienteFormData(payload: {
  data: any;
  imagenCliente: File;
}) {
  const formData = new FormData();

  // 🔴 JSON EXACTO QUE ESPERA EL BACKEND
  formData.append("data", JSON.stringify(payload.data));

  // 🔴 ARCHIVO REAL
  formData.append("imagenCliente", payload.imagenCliente);

  return axiosFormData.post(
    "rest-restaurant-api/api/cliente/registrar",
    formData
  );
}

// ==============================
// HOOK
// ==============================
export function useRegistrarClienteFormData() {
  return useMutation({
    mutationFn: registrarClienteFormData,
  });
}
