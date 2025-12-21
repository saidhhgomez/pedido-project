export interface MetodoPago {
  idFormaPago?: number; // Viene del GET
  nombre: string;
  estadoFormaPago: "activo" | "inactivo";
}
