// types/Plate.type.ts
export interface Plate {
  idCatalogo: number;        // ID asignado por el backend
  nombre: string;
  categoria: string;
  precio: number;
  stock: number;
  estadoplato: boolean;
  imagenPlatoUrl: string;
}

// Para crear un nuevo plato (sin idCatalogo)
export type PlateEit
 = Omit<Plate, 'idCatalogo'>;


 export type PlateUpdate = Omit<Plate, "idCatalogo">;
