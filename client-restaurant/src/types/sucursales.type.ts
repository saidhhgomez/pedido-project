export interface Sucursal {
  idSucursal?: number; // opcional al crear, obligatorio al editar/eliminar
  nombre: string;
  direccion: string;
  telefono: string;
  estado: "activo" | "inactivo";
}
