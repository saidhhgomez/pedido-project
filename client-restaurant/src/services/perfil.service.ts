import { useQuery } from "@tanstack/react-query";
import axiosClient from "./api.service";



const PATH="rest-restaurant-api/api/empleado/perfil";


 function getPerfilEmpleado (idEmpleado: number) {
  return axiosClient.get(
    `${PATH}/${idEmpleado}`,
  );
}

 function getPerfilCliente (idCliente: number) {
  return axiosClient.get(
    `rest-restaurant-api/api/cliente/perfil/${idCliente}`,
  );
}

export function useGetPerfilClienteId(idCliente?: number) {
  return useQuery({
    queryKey: ["getPerfilCliente", idCliente],
    queryFn: () => getPerfilCliente(idCliente!),
  });
}





export function useGetPerfilEmpleadoId(idEmpleado?: number) {
  return useQuery({
    queryKey: ["getPerfilEmpleado", idEmpleado],
    queryFn: () => getPerfilEmpleado(idEmpleado!),
  });
}
