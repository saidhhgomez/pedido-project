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


function deletePlate(id:number){
return axiosClient.delete(`${PATH}/${id}`);
}

// CREATE
export const useCreatePlate = () =>
  useMutation({
    mutationFn: (formData: FormData) => createPlate(formData),
  });



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


export function useUpdatePlate() {
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data}) =>
      updatePlateBackend(id, data),
    mutationKey: ["updatePlate"],
  });
}