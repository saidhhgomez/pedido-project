import type { Rol } from "../types/role.type";
import axiosClient from "./api.service";
import { useMutation, useQuery } from "@tanstack/react-query";

const PATH = "rest-restaurant-api/api/roles";

/* Requests */
function getAllRoles() {
  return axiosClient.get<Rol[]>(PATH);
}

function createRol(payload: Rol) {
  return axiosClient.post(PATH, {
    nombre: payload.nombre,
    descripcion: payload.descripcion,
  });
}

function updateRol({ id, data }: { id: number; data: Partial<Rol> }) {
  return axiosClient.put(`${PATH}/${id}`, {
    nombre: data.nombre,
    descripcion: data.descripcion,
    estado: data.estadoRol, // 🔥 corregido para tu backend
  });
}

function deleteRol(id: number) {
  return axiosClient.delete(`${PATH}/${id}`);
}

/* Hooks */
export function useGetAllRoles() {
  return useQuery({ queryKey: ["getAllRoles"], queryFn: getAllRoles });
}

export function useCreateRol() {
  return useMutation({
    mutationFn: createRol,
    mutationKey: ["createRol"],
  });
}

export function useUpdateRol() {
  return useMutation({ mutationFn: updateRol });
}

export function useRemoveRol() {
  return useMutation({ mutationFn: deleteRol });
}
