import axiosClient from "./api.service";
import { useQuery, useMutation } from "@tanstack/react-query";
import type { PersonaDniResponse } from "../types/empleado.types";

/* ================= BUSCAR PERSONA POR DNI ================= */
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

/* ================= ACTUALIZAR PERSONA ================= */
export interface ActualizarPersonaPayload {
  nombres: string;
  apPaterno: string;
  apMaterno: string;
  genero: string;
  tipoDocumento: string;
  numDocumento: string;
  telefono: string;
  correo: string;
  fechaNacimiento: string;
}

// Función que llama al endpoint PUT /empleado/actualizar/{id}
export function actualizarPersona(id: number, payload: ActualizarPersonaPayload) {
  return axiosClient.put(
    `rest-restaurant-api/api/empleado/actualizar/${id}`,
    payload
  );
}

// Hook para usar la mutación en React
export function useActualizarPersona() {
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: ActualizarPersonaPayload }) =>
      actualizarPersona(id, data),
  });
}
