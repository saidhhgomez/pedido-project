import axiosClient from "./api.service";
import { useMutation } from "@tanstack/react-query";

function login(LoginPayload: { usuario: string; contrasena: string }) {
  return axiosClient.post("rest-restaurant-api/api/login", LoginPayload);
}



export function useLogin() {
  return useMutation({
    mutationFn: login,
    mutationKey: ["login"],
  });
}