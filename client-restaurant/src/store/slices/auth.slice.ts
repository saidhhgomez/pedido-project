import { createSlice, type PayloadAction,  } from "@reduxjs/toolkit";
import { type RootState } from "..";

export interface AuthState {
  token: string | null;
    rol: string | null;
  nombre: string | null;
}

const initialState: AuthState = {
  token: null,
  rol: null,
  nombre: null,
};

export const counterSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    login: (state, action: PayloadAction<AuthState>) => {
      state.token = action.payload.token;
      state.rol=action.payload.rol;
      state.nombre=action.payload.nombre;
    },
    logout: (state) => {
      state.token = null;
      state.rol = null;
      state.nombre = null;
    },
  },
});

export const { login, logout } = counterSlice.actions;

export const selectToken = (state: RootState) => state.authState.token;
export const selectRol = (state:RootState) => state.authState.rol;

export default counterSlice.reducer;