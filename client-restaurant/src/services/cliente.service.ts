// cliente.service.ts
import { useMutation } from "@tanstack/react-query";
import axiosFormData from "./api.service.file";
import type { RegistrarClienteDTO } from "../types/cliente.types";

// ==============================
// REGISTRAR CLIENTE (FormData)
// ==============================
export function registrarClienteFormData(data: RegistrarClienteDTO) {
  const formData = new FormData();

  // 🔴 BACKEND ESPERA: @FormDataParam("data") String
  formData.append(
    "data",
    JSON.stringify({
      credenciales: data.credenciales,
      persona: data.persona,
      cliente: {
        imagenCliente_url: "",
      },
    })
  );

  // 🔴 BACKEND ESPERA: @FormDataParam("imagenCliente") File
  formData.append(
    "imagenCliente",
    data.cliente.imagenCliente[0] // FILE REAL
  );

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
