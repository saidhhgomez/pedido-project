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

export interface EmpleadoDTO {
  direccion: string;
  imagenConductor_url: string;
  estadoEmpleado: string;
}

export interface ContratoDTO {
  idSucursal: number;
  idTipoContrato: number;
  idRol: number;
  fechaInicio: string;
  fechaFin: string;
  salario: number;
}

export interface RegistrarEmpleadoDTO {
  credenciales: CredencialesDTO;
  persona: PersonaDTO;
  empleado: EmpleadoDTO;
  contrato: ContratoDTO;
}

// Respuesta del DNI
export interface PersonaDniResponse {
  tipoDocumento: string;
  numDocumento: string;
  fechaNacimiento: string;
  genero: string;
  correo: string;
  nombreCompleto: string;
  telefono: string;
  idPersona: number;
}
