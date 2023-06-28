package org.example.clases;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdenCompra {

    String numeroOrden;
    String codigoSuplidor;
    String ciudadSuplidor;
    Date fechaOrden;
    ArrayList<Componente> componentes;
    double montoTotal;

    public OrdenCompra() {
        this.numeroOrden = new String();
        this.codigoSuplidor = codigoSuplidor;
        this. ciudadSuplidor = new String();
        this.fechaOrden = new Date();
        this.componentes = new ArrayList<>();
        this.montoTotal = montoTotal;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public String getCodigoSuplidor() {
        return codigoSuplidor;
    }

    public String getCiudadSuplidor() {
        return ciudadSuplidor;
    }

    public Date getFechaOrden() {
        return fechaOrden;
    }

    public ArrayList<Componente> getComponentes() {
        return componentes;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public void setCodigoSuplidor(String codigoSuplidor) {
        this.codigoSuplidor = codigoSuplidor;
    }

    public void setCiudadSuplidor(String ciudadSuplidor) {
        this.ciudadSuplidor = ciudadSuplidor;
    }

    public void setFechaOrden(Date fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public void agregarComponente(Componente aux){
        componentes.add(aux);
    }

}
