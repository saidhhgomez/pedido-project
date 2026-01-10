export interface DireccionForm {
  departamento: string;
  provincia: string;
  distrito: string;
  direccion: string;
  referencia: string;
}


export interface DireccionPayload extends DireccionForm {
  idCliente: number | null;
}