package org.example.clases;

import java.util.Date;

public class Proveedor {

    String Nombre;
    Date tiempoEntrega;

    double precio;

    double descuento;

    boolean activo;

    public Proveedor() {
        this.Nombre = Nombre;
        this.tiempoEntrega = tiempoEntrega;
        this.precio = precio;
        this.descuento = descuento;
        this.activo = activo;
    }

    public String getNombre() {
        return Nombre;
    }

    public Date getTiempoEntrega() {
        return tiempoEntrega;
    }

    public double getPrecio() {
        return precio;
    }

    public double getDescuento() {
        return descuento;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setTiempoEntrega(Date tiempoEntrega) {
        this.tiempoEntrega = tiempoEntrega;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
