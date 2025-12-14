import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import { type RootState } from "..";
import type { AuthState, LoginResponse } from "../../types/auth.types";

const initialState: AuthState = {
  token: null,
  rol: null,
  nombre: null,
  idCliente: null,
  idEmpleado: null,
};


export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    login: (state, action: PayloadAction<LoginResponse>) => {
      state.token = action.payload.token;
      state.rol = action.payload.usuario.rol;
      state.nombre = action.payload.usuario.nombre;
      state.idCliente = action.payload.usuario.idCliente;
      state.idEmpleado = action.payload.usuario.idEmpleado;
    },

    logout: (state) => {
      state.token = null;
      state.rol = null;
      state.nombre = null;
      state.idCliente = null;
      state.idEmpleado = null;
    },

        // 👤 USAR COMO CLIENTE
    usarComoCliente: (state) => {
      state.idEmpleado = null;
    },

    // 🛠 USAR COMO ADMIN
    usarComoAdmin: (state) => {
      state.idCliente = null;
    },
  },
});


export const {
  login,
  logout,
  usarComoCliente,
  usarComoAdmin,
} = authSlice.actions;
// Selectores
export const selectToken = (state: RootState) => state.authState.token;
export const selectRol = (state: RootState) => state.authState.rol;
export const selectNombre = (state: RootState) => state.authState.nombre;
export const selectIdCliente = (state: RootState) => state.authState.idCliente;
export const selectIdEmpleado = (state: RootState) => state.authState.idEmpleado;

export default authSlice.reducer;
