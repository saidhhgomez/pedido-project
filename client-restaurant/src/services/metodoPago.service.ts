import type { MetodoPago } from "../types/metodoPago.type";
import axiosClient from "./api.service";
import { useMutation, useQuery } from "@tanstack/react-query";

const PATH = "rest-restaurant-api/api/metodopago";

/* Requests */
function getAllMetodosPago() {
  return axiosClient.get<MetodoPago[]>(PATH);
}

function createMetodoPago(payload: MetodoPago) {
  return axiosClient.post(PATH, { nombre: payload.nombre });
}

function updateMetodoPago({ id, data }: { id: number; data: MetodoPago }) {
  return axiosClient.put(`${PATH}/${id}`, { nombre: data.nombre });
}

function deleteMetodoPago(id: number) {
  return axiosClient.delete(`${PATH}/${id}`);
}

/* Hooks */
export function useGetAllMetodosPago() {
  return useQuery({ queryKey: ["getAllMetodosPago"], queryFn: getAllMetodosPago });
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
