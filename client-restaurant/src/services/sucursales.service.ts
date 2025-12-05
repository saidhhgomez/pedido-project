import type { Sucursal } from "../types/sucursales.type";
import axiosClient from "./api.service";
import { useMutation, useQuery } from "@tanstack/react-query";

const PATH = "rest-restaurant-api/api/sucursales";

function getAllSucursales() {
  return axiosClient.get(PATH);
}

function getSucursalId(id: number) {
  return axiosClient.get(`${PATH}/${id}`);
}

function createSucursal(payload: Sucursal) {
  return axiosClient.post(PATH, payload);
}

function updateSucursal({ id, data }: { id: number; data: Partial<Sucursal> }) {
  return axiosClient.put(`${PATH}/${id}`, data);
}

function deleteSucursal(id: number) {
  return axiosClient.delete(`${PATH}/${id}`);
}

// Hooks
export function useGetAllSucursales() {
  return useQuery({ queryKey: ["getAllSucursales"], queryFn: getAllSucursales });
}

export function useGetSucursalId(id?: number | null) {
  return useQuery({
    queryKey: ["getSucursalId", id],
    queryFn: () => getSucursalId(id as number),
    enabled: !!id,
  });
}

export function useCreateSucursal() {
  return useMutation({ mutationFn: createSucursal, mutationKey: ["createSucursal"] });
}

export function useUpdateSucursal() {
  return useMutation({ mutationFn: updateSucursal, mutationKey: ["updateSucursal"] });
}

export function useRemoveSucursal() {
  return useMutation({ mutationFn: deleteSucursal, mutationKey: ["deleteSucursal"] });
}
