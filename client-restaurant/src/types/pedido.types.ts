export interface PedidoDetalle {
  idProducto: number;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
  igv: number;
  total: number;
}

export interface Pedido {
  idCliente: number;
  idDireccion: number;
  idEmpleado: number;
  idMesa: number;
  idFormaPago: number;
  estado: string;
  detalles: PedidoDetalle[];
}
