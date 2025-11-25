import { createSlice, type PayloadAction,  } from "@reduxjs/toolkit";
import { type RootState } from "..";
import type { AuthState } from "../../types/auth.types";


const initialState: AuthState = {
  token: null,
  rol: null,
  nombre: null,
  idCliente: null,
  idEmpleado: null,
  tipoId: null,
};

export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    login: (state, action: PayloadAction<{
    token: string;
    rol: string;
    nombre: string;
    tipoId: string;
    id: number;
  }>) => {

      state.token = action.payload.token;
      state.rol = action.payload.rol;
      state.nombre = action.payload.nombre;
      state.tipoId=action.payload.tipoId;

      // Guardar id según el rol
      if (action.payload.rol === "cliente") {
        state.idCliente = action.payload.id;
        state.idEmpleado = null;
      } else if (action.payload.rol === "empleado") {
        state.idEmpleado = action.payload.id;
        state.idCliente = null;
      }
    },
    logout: (state) => {
      state.token = null;
      state.rol = null;
      state.nombre = null;
      state.idCliente = null;
      state.idEmpleado = null;
      state.tipoId=null;
    },
  },
});


export const { login, logout } = authSlice.actions;

export const selectToken = (state: RootState) => state.authState.token;
export const selectRol = (state: RootState) => state.authState.rol;
export const selectNombre = (state: RootState) => state.authState.nombre;
export const selectIdCliente = (state: RootState) => state.authState.idCliente;
export const selectIdEmpleado = (state: RootState) => state.authState.idEmpleado;
export const selectTipoId = (state: RootState) => state.authState.tipoId;



export default authSlice.reducer;