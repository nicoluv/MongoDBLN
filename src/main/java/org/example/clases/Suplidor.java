package org.example.clases;

import java.util.Date;

public class Suplidor {

    String codigoSuplidor;
    String nombre;
    String rnc;
    String ciudad;
    String direccion;
    public Suplidor() {
        this.codigoSuplidor = new String();
        this.nombre = new String();
        this.rnc = new String();
        this.ciudad = new String();
        this.direccion = new String();
    }

    public Suplidor(String codigoSuplidor, String nombre, String rnc, String ciudad, String direccion) {
        this.codigoSuplidor = codigoSuplidor;
        this.nombre = nombre;
        this.rnc = rnc;
        this.ciudad = ciudad;
        this.direccion = direccion;
    }

    public String getCodigoSuplidor() {
        return codigoSuplidor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        nombre = nombre;
    }

    public String getRnc() {
        return rnc;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getDireccion() {
        return direccion;
    }
}
