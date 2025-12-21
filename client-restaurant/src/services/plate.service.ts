import axiosClient from "./api.service";
import axiosFormData from "./api.service.file";

import { useMutation, useQuery } from "@tanstack/react-query";
import type {   Plate } from "../types/Plate.type";


const PATH="rest-restaurant-api/api/catalogo";


export const createPlate = (formData: FormData) => {
  return axiosFormData.post<Plate>(
    `${PATH}?idAdmin=1`,
    formData
  );
};

  function getAllPlate(){
    return axiosClient.get("rest-restaurant-api/api/catalogo");

  }

  function updatePlateBackend(id: number, data) {
    return axiosClient.put(`${PATH}/${id}`, data);
  }

async function actualizarEstadoCatalogo(id: number, activo: boolean) {
  // enviamos el estado como query param
  const response = await axiosClient.put(
    `${PATH}/${id}/estado`, 
    {}, 
    { params: { activo } }
  );
  return response.data; // { message: "Plato desactivado correctamente" }
}


function deletePlate(id:number){
return axiosClient.delete(`${PATH}/${id}`);
}

// CREATE
export const useCreatePlate = () =>
  useMutation({
    mutationFn: (formData: FormData) => createPlate(formData),
  });


  function getAllPlateDisponible(){
    return axiosClient.get(`${PATH}/disponibles`);

  }

export function useGetPlateDisponible(){
  return useQuery({
        queryFn:getAllPlateDisponible,
    queryKey:["getAllPlateDisponible"],
  });
}



export function useGetPlate(){
  return useQuery({
        queryFn:getAllPlate,
    queryKey:["getAllPlate"],
  });
}



export function useRemovePlate(){
  return useMutation(
  {
    mutationFn: deletePlate,
    mutationKey:["deletePlate"]
  }
  );
}

// hooks/useCatalogo.ts


  export function useActualizarEstadoCatalogo() {

    return useMutation({
      mutationFn: ({ id, activo }: { id: number; activo: boolean }) =>
        actualizarEstadoCatalogo(id, activo),

    });
  }

export function useUpdatePlate() {
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data}) =>
      updatePlateBackend(id, data),
    mutationKey: ["updatePlate"],
  });
}