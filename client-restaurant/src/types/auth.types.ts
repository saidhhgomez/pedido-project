// auth.types.ts
export interface Usuario {
  idPersona: number;
  nombre: string;
  correo: string;
  fotoUrl: string;
  rolPrincipal: string; // 👈 AQUÍ vive el rol
}

export interface PerfilCliente {
  idCliente: number;
  categoria: string;
}

export interface PerfilEmpleado {
  idEmpleado: number;
  idSucursal: number;
  estado: string;
  nombreSucursal: string | null;
}

export interface LoginResponse {
  status: string;
  token: string;
  usuario: Usuario;
  perfilCliente?: PerfilCliente;
  perfilEmpleado?: PerfilEmpleado;
}

export interface AuthState {
  token: string | null;
  status: string | null;

  usuario: Usuario | null;
  perfilCliente: PerfilCliente | null;
  perfilEmpleado: PerfilEmpleado | null;
}
