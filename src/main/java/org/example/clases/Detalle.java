package org.example.clases;

import java.util.List;

public class Detalle {

    private String unidad;
    private String codigoComp;
    private int cantidad;

    List valores;

    public Detalle(String unidad, String codigoComp, int cantidad) {
        this.unidad = unidad;
        this.codigoComp = codigoComp;
        this.cantidad = cantidad;
    }

    public Detalle() {

    }

    public List getValores() {
        return valores;
    }

    public void setValores(List valores) {
        this.valores = valores;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getCodigoComp() {
        return codigoComp;
    }

    public void setCodigoComp(String codigoComp) {
        this.codigoComp = codigoComp;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
