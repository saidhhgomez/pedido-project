export interface Usuario {
  id: number;
  nombre: string;
  rol: string;
  tipoId: string; // puede ser "cliente", "empleado" o cualquier otro tipo que agregue el backend
}

export interface LoginResponse {
  token: string;
  usuario: Usuario;
}

export interface AuthState {
  token: string | null;
  rol: string | null;
  nombre: string | null;
  idCliente: number | null;
  idEmpleado: number | null;
   tipoId: string | null;
}
