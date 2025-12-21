// Lo que VIENE del backend
export interface Plate {
  idCatalogo: number;
  nombre: string;
  categoria: string;
  precio: number;
  stock: number;
  imagenPlatoUrl: string;
  estadoplato:boolean
}

// FORM (crear / editar)
export interface PlateForm {
  nombre: string;
  categoria: string;
  precio: number;
  stock: number;
  imagenPlato: FileList;
}

// JSON que va en "data"
export interface PlateDTO {
  nombre: string;
  categoria: string;
  precio: number;
  stock: number;
}
