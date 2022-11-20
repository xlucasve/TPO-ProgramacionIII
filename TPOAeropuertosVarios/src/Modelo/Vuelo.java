package Modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Vuelo {
    private String nroVuelo;
    private String aeropuertoOrigen;
    private String aeropuertoDestino;
    private Date fechaDespegue;
    private Date fechaAterrizaje;

    public Vuelo(String nroVuelo, String aeropuertoOrigen, String aeropuertoDestino, Date fechaDespegue, Date fechaAterrizaje) {
        this.nroVuelo = nroVuelo;
        this.aeropuertoOrigen = aeropuertoOrigen;
        this.aeropuertoDestino = aeropuertoDestino;
        this.fechaDespegue = fechaDespegue;
        this.fechaAterrizaje = fechaAterrizaje;
    }

    public String getNroVuelo() {
        return nroVuelo;
    }

    public void setNroVuelo(String nroVuelo) {
        this.nroVuelo = nroVuelo;
    }

    public String getAeropuertoOrigen() {
        return aeropuertoOrigen;
    }

    public void setAeropuertoOrigen(String aeropuertoOrigen) {
        this.aeropuertoOrigen = aeropuertoOrigen;
    }

    public String getAeropuertoDestino() {
        return aeropuertoDestino;
    }

    public void setAeropuertoDestino(String aeropuertoDestino) {
        this.aeropuertoDestino = aeropuertoDestino;
    }

    public Date getFechaDespegue() {
        return fechaDespegue;
    }

    public void setFechaDespegue(Date fechaDespegue) {
        this.fechaDespegue = fechaDespegue;
    }

    public Date getFechaAterrizaje() {
        return fechaAterrizaje;
    }

    public void setFechaAterrizaje(Date fechaAterrizaje) {
        this.fechaAterrizaje = fechaAterrizaje;
    }
}
