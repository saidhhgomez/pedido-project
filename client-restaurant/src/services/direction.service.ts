import type { DireccionForm } from "../types/direction.type";
import axiosClient from "./api.service";
import { useMutation, useQuery } from "@tanstack/react-query";

const PATH="/rest-restaurant-api/api/direccion"

function getAllDirecciones(idCliente: number) {
  return axiosClient.get(`${PATH}/cliente/${idCliente}`);
}

function deleteDireccion(id:string){
return axiosClient.delete(`${PATH}/eliminar/${id}`);
}

 function createDireccion(payload: DireccionForm) {
  return axiosClient.post(
    "rest-restaurant-api/api/direccion/registrar",
    payload
  );
}




export function useGetDirecciones(idCliente?: number | null) {
  return useQuery({
    queryKey: ["direcciones", idCliente], // 🔥 se vuelve a ejecutar si cambia el id
    queryFn: () => getAllDirecciones(idCliente as number),
    enabled: !!idCliente, // 🔥 evita errores cuando idCliente es null
  });
}

export function useRemoveDireccion(){
  return useMutation(
  {
    mutationFn: deleteDireccion,
    mutationKey:["deleteDireccion"]
  }
  );
}


export function useCreateDirection(){
  return useMutation(
    {
      mutationFn:createDireccion,
      mutationKey:["createDireccion"],
    }
  )
}