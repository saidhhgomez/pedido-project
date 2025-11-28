import axiosClient from "./api.service";
import { useMutation } from "@tanstack/react-query";
import type { Plate } from "../types/Plate.type";

function createPlate(payload: Plate) {
  return axiosClient
    .post("rest-restaurant-api/api/catalogo", payload);
  }

  
export function useCreatePlate(){
    return useMutation ({
        mutationFn:createPlate,
        mutationKey:["createPlate"],
    });

}