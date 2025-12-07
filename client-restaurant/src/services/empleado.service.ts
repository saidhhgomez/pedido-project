import axiosClient from "./api.service";
import { useMutation, useQuery } from "@tanstack/react-query";
import type { RegistrarEmpleadoDTO, PersonaDniResponse } from "../types/empleado.types";

// ▶ Registrar empleado con persona nueva
export function crearEmpleadoNuevaPersona(payload: RegistrarEmpleadoDTO) {
  return axiosClient.post(
    "rest-restaurant-api/api/empleado/registrar",
    payload
  );
}

export function useCrearEmpleadoNuevaPersona() {
  return useMutation({
    mutationFn: crearEmpleadoNuevaPersona,
    mutationKey: ["crearEmpleadoNuevaPersona"],
  });
}

// ▶ Registrar empleado con persona existente
export function crearEmpleadoPersonaExistente(payload: RegistrarEmpleadoDTO) {
  return axiosClient.post(
    "rest-restaurant-api/api/empleado/contrato-existente",
    payload
  );
}

export function useCrearEmpleadoPersonaExistente() {
  return useMutation({
    mutationFn: crearEmpleadoPersonaExistente,
    mutationKey: ["crearEmpleadoPersonaExistente"],
  });
}

// ▶ Buscar persona por DNI
export function buscarEmpleadoPorDni(dni: string) {
  return axiosClient.get<PersonaDniResponse>(
    `rest-restaurant-api/api/empleado/dni/${dni}`
  );
}

export function useBuscarPorDni(dni: string) {
  return useQuery({
    queryKey: ["buscarPorDni", dni],
    queryFn: () => buscarEmpleadoPorDni(dni).then((r) => r.data),
    enabled: dni.length === 8,
    retry: false,
  });
}
