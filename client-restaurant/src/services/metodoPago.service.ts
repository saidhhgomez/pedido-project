import type { MetodoPago } from "../types/metodoPago.type";
import axiosClient from "./api.service";
import { useMutation, useQuery } from "@tanstack/react-query";

const PATH = "rest-restaurant-api/api/metodopago";

/* Requests */
function getAllMetodosPago() {
  return axiosClient.get<MetodoPago[]>(PATH);
}

/* Requests */
function getAllMetodosPagoActivo() {
  return axiosClient.get(`${PATH}/activos`);
}

function createMetodoPago(payload: MetodoPago) {
  return axiosClient.post(PATH, { nombre: payload.nombre });
}

function updateMetodoPago({ id, data }: { id: number; data: MetodoPago }) {
  return axiosClient.put(`${PATH}/${id}`, { nombre: data.nombre });
}


// Función que hace PATCH al backend
function updateMetodoPagoEstado  ({ id, estado }: { id: number; estado: "activo" | "inactivo" }){
  return axiosClient.put(`${PATH}/${id}/estado`, { estado });

}


function deleteMetodoPago(id: number) {
  return axiosClient.delete(`${PATH}/${id}`);
}

/* Hooks */
export function useGetAllMetodosPago() {
  return useQuery({ queryKey: ["getAllMetodosPago"], 
  queryFn: getAllMetodosPago });
}


export function useGetAllMetodosPagoActivo() {
  return useQuery({ queryKey: ["getAllMetodosPagoActivo"], 
  queryFn: getAllMetodosPagoActivo });
}


export function useCreateMetodoPago() {
  return useMutation({ mutationFn: createMetodoPago, mutationKey: ["createMetodoPago"] });
}

export function useUpdateMetodoPago() {
  return useMutation({ mutationFn: updateMetodoPago });
}

export function useRemoveMetodoPago() {
  return useMutation({ mutationFn: deleteMetodoPago });
}

export function useUpdateMetodoPagoEstado() {
  return useMutation({
    mutationFn: updateMetodoPagoEstado,
    mutationKey: ["updateMetodoPagoEstado"],
  });
}
