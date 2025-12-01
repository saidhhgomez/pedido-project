package com.restaurant.model;

public class DetallePedido {

    private int idDetalle;
    private int idPedido;
    private int idProducto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private double igv;
    private double total;

    public DetallePedido() {}

    public DetallePedido(int idDetalle, int idPedido, int idProducto, int cantidad,
                         double precioUnitario, double subtotal, double igv, double total) {
        this.idDetalle = idDetalle;
        this.idPedido = idPedido;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
    }

     public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getIgv() { return igv; }
    public void setIgv(double igv) { this.igv = igv; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}

