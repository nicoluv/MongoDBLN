package org.example.clases;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovimientoInventario {

    String codigoMovimiento;
    String fechaMovimiento;
    String codigoAlmacen;
    String tipoMovimiento;
    String componente;
    ArrayList<Detalle> detalle;
    int cantidad;

    public MovimientoInventario() {
        this.codigoMovimiento = new String();
        this.fechaMovimiento = new String();
        this.codigoAlmacen = new String();
        this.tipoMovimiento = new String();
        this.componente = new String();
        this.detalle = detalle;
        this.cantidad = cantidad;
    }

    public MovimientoInventario(String nuevoMov, String s, String s1, String salida, List detalles) {
    }

    public MovimientoInventario(String ultimo, String fecha, String s, String salida, String s1, List detalles) {
    }

    /*
        public MovimientoInventario(String codigoMovimiento, String fechaMovimiento, String codigoAlmacen, String tipoMovimiento,  List detalle) {
            this.codigoMovimiento = codigoMovimiento;
            this.fechaMovimiento = fechaMovimiento;
            this.codigoAlmacen = codigoAlmacen;
            this.tipoMovimiento = tipoMovimiento;
            this.detalle = detalle;
        }
    */
    public void setCodigoMovimiento(String codigoMovimiento) {
        this.codigoMovimiento = codigoMovimiento;
    }

    public void setFechaMovimiento(String fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public void setCodigoAlmacen(String codigoAlmacen) {
        this.codigoAlmacen = codigoAlmacen;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public void setComponente(String componente) {
        this.componente = componente;
    }

    public List getDetalle() {
        return detalle;
    }

    public MovimientoInventario(String codigoMovimiento, String fechaMovimiento, String codigoAlmacen, String tipoMovimiento, String componente) {
        this.codigoMovimiento = codigoMovimiento;
        this.fechaMovimiento = fechaMovimiento;
        this.codigoAlmacen = codigoAlmacen;
        this.tipoMovimiento = tipoMovimiento;
        this.componente = componente;
    }

    public void setDetalle(ArrayList<Detalle> detalle) {
        this.detalle = detalle;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodigoMovimiento() {
        return codigoMovimiento;
    }

    public String getFechaMovimiento() {
        return fechaMovimiento;
    }

    public String getCodigoAlmacen() {
        return codigoAlmacen;
    }

    public String getComponente() {
        return componente;
    }

    public int getCantidad() {
        return cantidad;
    }




}