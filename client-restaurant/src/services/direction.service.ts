import axiosClient from "./api.service";
import { useMutation, useQuery } from "@tanstack/react-query";

const PATH="http://localhost:8080/rest-restaurant-api/api/direccion"

function getAllDirecciones(idCliente: number) {
  return axiosClient.get(`${PATH}/cliente/${idCliente}`);
}

function deleteDireccion(id:string){

return axiosClient.delete(`${PATH}/eliminar/${id}`);
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