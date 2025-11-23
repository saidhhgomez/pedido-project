export interface CredencialesDTO {
  usuario: string;
  contrasena: string;
}

export interface PersonaDTO {
  nombres: string;
  apPaterno: string;
  apMaterno: string;
  genero: string;
  tipoDocumento: string;
  numDocumento: string;
  telefono: string;
  correo: string;
  fechaNacimiento: string;
}

export interface EmpleadoDataDTO {
  direccion: string;
  estadoEmpleado: string;
  imagenConductor_url: string;
}

export interface RegistrarEmpleadoDTO {
  credenciales: CredencialesDTO;
  persona: PersonaDTO;
  empleado: EmpleadoDataDTO;
}
