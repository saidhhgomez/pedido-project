import axiosClient from "./api.service";
import { useMutation, useQuery } from "@tanstack/react-query";



const PATH="/rest-restaurant-api/api/pedido/cliente"



function getHistorialCliente(idCliente: number) {
  return axiosClient.get(`${PATH}/mis-pedidos/${idCliente}`);
}



export function useGetAllMetodosPago() {
  return useQuery({ queryKey: ["getHistorialCliente"], 
  queryFn: getHistorialCliente });
}
