import axiosClient from "./api.service";
import {useQuery } from "@tanstack/react-query";



const PATH="/rest-restaurant-api/api/pedido/cliente"

function getHistorialCliente(idCliente: any) {
  return axiosClient.get(`${PATH}/mis-pedidos/${idCliente}`);
}

export function useGetHistorialCliente(idCliente: any) {
  return useQuery({
    queryKey: ["getHistorialCliente", idCliente],
    queryFn: () => getHistorialCliente(idCliente),
  });
}
