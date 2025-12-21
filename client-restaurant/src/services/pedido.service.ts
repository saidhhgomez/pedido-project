import axiosClient from "./api.service";
import { useMutation, useQuery } from "@tanstack/react-query";

const PATH = "rest-restaurant-api/api/pedido";




function crearPedido(payload: any) {
  return axiosClient.post(`${PATH}/presencial`, payload);
}

function crearPedidoOnline(payload: any) {
  return axiosClient.post(`${PATH}/online`, payload);
}




export function useCrearPedidoOnline() {
  return useMutation({ mutationFn: crearPedidoOnline, mutationKey: ["crearPedidoOnline"] });
}




 function getPedidoActivoDetalle (idMesa: number) {
  return axiosClient.get(
    `${PATH}/activo/${idMesa}`,
  );
}




function FinalizarPedidoPago({ id, idmesa }: { id: number; idmesa: number }) {
  return axiosClient.put(`${PATH}/${id}/mesa/${idmesa}/liberar`);
}


export function useFinalizarpedido() {
  return useMutation({ mutationFn: FinalizarPedidoPago });
}



function HabilitarMesa({ idMesa}: { idMesa: number}) {
  return axiosClient.put(`${PATH}/mesa/${idMesa}/habilitar`);
}


export function useHablitarMesa() {
  return useMutation({ mutationFn: HabilitarMesa });
}




function cancelarPedido({ idPedido}: { idPedido: number}) {
  return axiosClient.put(`${PATH}/cancelar/${idPedido}`);
}


export function useCancelarPedido() {
  return useMutation({ mutationFn: cancelarPedido });
}



function DeleteDetallePeedido({ idPedido, iddetalle }: { idPedido: number; iddetalle: number }) {
  return axiosClient.delete(`${PATH}/${idPedido}/detalle/${iddetalle}`);
}


export function useDeletePedido() {
  return useMutation({ mutationFn: DeleteDetallePeedido });

}






function updateAgregarPlatoPedido({ idPedido, detalles }: { idPedido: number; detalles: any }) {
  return axiosClient.post(`${PATH}/detalles/${idPedido}`,detalles);
}

export function useAgregarPlato() {
  return useMutation({ mutationFn: updateAgregarPlatoPedido });
}



export function usePedidoActivoDetalle(idMesa?: number) {
  return useQuery({
    queryKey: ["getPedidoActivoDetalle", idMesa],
    queryFn: () => getPedidoActivoDetalle(idMesa!),
  });
}



export function useCrearPedidoPresencial() {
  return useMutation({ mutationFn: crearPedido, mutationKey: ["crearPedido"] });
}
