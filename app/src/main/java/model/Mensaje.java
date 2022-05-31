package model;

public class Mensaje {

    String dato;
    long fecha;
    String idEnvio;
    String tipo;

    public Mensaje() {
    }

    public Mensaje(String dato, long fecha, String idEnvio, String tipo) {
        this.dato = dato;
        this.fecha = fecha;
        this.idEnvio = idEnvio;
        this.tipo = tipo;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(String idEnvio) {
        this.idEnvio = idEnvio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
