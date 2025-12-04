import type { TipoJornada } from "../types/contrato.type";
import axiosClient from "./api.service";
import { useMutation, useQuery } from "@tanstack/react-query";

const PATH="rest-restaurant-api/api/tipocontrato"

function getAllContratoId(id: number) {
  return axiosClient.get(`${PATH}/cliente/${id}`);
}

function deleteContrato(id:number){
return axiosClient.delete(`${PATH}/eliminar/${id}`);
}

 function createContrato(payload:TipoJornada ) {
  return axiosClient.post(
    `${PATH}`,
    payload
  );
}

function getAllContrato() {
  return axiosClient.get(`${PATH}`);
}



export function useGetAllContrato(){
  return useQuery({
        queryFn:getAllContrato,
    queryKey:["getAllContrato"],
  });
}


export function useGetContratoId(id?: number | null) {
  return useQuery({
    queryKey: ["getAllContratoId", id], // 🔥 se vuelve a ejecutar si cambia el id
    queryFn: () => getAllContratoId(id as number),
    enabled: !!id, // 🔥 evita errores cuando idCliente es null
  });
}

export function useRemoveContrato(){
  return useMutation(
  {
    mutationFn: deleteContrato,
    mutationKey:["deleteContrato"]
  }
  );
}


export function useCreateContrato(){
  return useMutation(
    {
      mutationFn:createContrato,
      mutationKey:["createContrato"],
    }
  )
}