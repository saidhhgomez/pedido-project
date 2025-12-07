import axiosClient from "./api.service";
import type { Pedido } from "../types/pedido.types";
import { useMutation } from "@tanstack/react-query";

const PATH = "rest-restaurant-api/api/pedido";

function crearPedido(payload: Pedido) {
  return axiosClient.post(PATH, payload);
}

export function useCrearPedido() {
  return useMutation({ mutationFn: crearPedido, mutationKey: ["crearPedido"] });
}
