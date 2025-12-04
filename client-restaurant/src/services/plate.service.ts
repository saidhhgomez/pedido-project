import axiosClient from "./api.service";
import { useMutation, useQuery } from "@tanstack/react-query";
import type {  PlateEit, PlateUpdate } from "../types/Plate.type";


const PATH="rest-restaurant-api/api/catalogo";

function createPlate(payload: PlateEit) {
  return axiosClient
    .post("rest-restaurant-api/api/catalogo", payload);
  }

  function getAllPlate(){
    return axiosClient.get("rest-restaurant-api/api/catalogo");

  }




function updatePlateBackend(id: number, data: PlateUpdate) {
  return axiosClient.put(`${PATH}/${id}`, data);
}


function deletePlate(id:number){
return axiosClient.delete(`${PATH}/${id}`);
}

export function useCreatePlate(){
    return useMutation ({
        mutationFn:createPlate,
        mutationKey:["createPlate"],
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


export function useUpdatePlate() {
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: PlateUpdate }) =>
      updatePlateBackend(id, data),
    mutationKey: ["updatePlate"],
  });
}