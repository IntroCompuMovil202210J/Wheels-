package model;

import org.joda.time.DateTime;

public class Mensaje {

    String dato;
    DateTime fecha;
    TipoMensaje tipo;

    public Mensaje() {
    }

    public Mensaje(String dato, DateTime fecha, TipoMensaje tipo) {
        this.dato = dato;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public DateTime getFecha() {
        return fecha;
    }

    public void setFecha(DateTime fecha) {
        this.fecha = fecha;
    }

    public TipoMensaje getTipo() {
        return tipo;
    }

    public void setTipo(TipoMensaje tipo) {
        this.tipo = tipo;
    }

}
