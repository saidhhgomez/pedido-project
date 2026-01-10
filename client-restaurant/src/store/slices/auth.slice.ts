import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "..";
import type { AuthState, LoginResponse } from "../../types/auth.types";

const initialState: AuthState = {
  token: null,
  status: null,

  usuario: null,
  perfilCliente: null,
  perfilEmpleado: null,
};

export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    login: (state, action: PayloadAction<LoginResponse>) => {
      const {
        token,
        status,
        usuario,
        perfilCliente,
        perfilEmpleado,
      } = action.payload;

      state.token = token;
      state.status = status;

      state.usuario = usuario; // incluye rolPrincipal
      state.perfilCliente = perfilCliente ?? null;
      state.perfilEmpleado = perfilEmpleado ?? null;
    },

    logout: (state) => {
      state.token = null;
      state.status = null;

      state.usuario = null;
      state.perfilCliente = null;
      state.perfilEmpleado = null;
    },

// 👤 USAR COMO CLIENTE 
usarComoCliente: (state) => { 
  state.perfilEmpleado = null; },

    usarComoEmpleado: (state) => { 
  state.perfilCliente = null; },

  },


  },
);

export const { login, logout,usarComoCliente,usarComoEmpleado} = authSlice.actions;
export default authSlice.reducer;
export const selectAuth = (state: RootState) => state.authState;

export const selectToken = (state: RootState) =>
  state.authState.token;

export const selectUsuario = (state: RootState) =>
  state.authState.usuario;

export const selectRol = (state: RootState) =>
  state.authState.usuario?.rolPrincipal;

export const selectPerfilCliente = (state: RootState) =>
  state.authState.perfilCliente;

export const selectPerfilEmpleado = (state: RootState) =>
  state.authState.perfilEmpleado;
