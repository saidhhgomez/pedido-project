import axiosClient from "./api.service";
import { useMutation } from "@tanstack/react-query";
import type { RegistrarClienteDTO } from "../types/cliente.types";


function registrarCliente(payload:RegistrarClienteDTO){
    return axiosClient.post("/rest-restaurant-api/api/cliente/registrar",payload);
}

export function useRegisterCliente() {
  return useMutation({
    mutationFn: registrarCliente,
    mutationKey: ["registrarCliente"],
  });
}


