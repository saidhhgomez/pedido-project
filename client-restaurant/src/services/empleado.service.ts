import axiosClient from "./api.serviceRestaurant";
import { useMutation } from "@tanstack/react-query";
import type { RegistrarEmpleadoDTO } from "../types/empleado.types";

function crearEmpleado(payload: RegistrarEmpleadoDTO) {
  return axiosClient
    .post("rest-restaurant-api/api/empleado/registrar", payload);
  }

export function useCrearEmpleado() {
  return useMutation({
    mutationFn: crearEmpleado,
    mutationKey: ["crearEmpleado"],
  });
}
