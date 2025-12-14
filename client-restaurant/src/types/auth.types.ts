// Roles permitidos según backend
export type Rol = "cliente" | "empleado";

// Usuario que viene del backend
export interface Usuario {
  idCliente: number | null;
  idEmpleado: number | null;
  nombre: string;
  rol: Rol;
}

// Respuesta del login
export interface LoginResponse {
  token: string;
  usuario: Usuario;
}

// Estado global de autenticación (Redux)
export interface AuthState {
  token: string | null;
  rol: Rol | null;
  nombre: string | null;
  idCliente: number | null;
  idEmpleado: number | null;
}
