package model;

public class Calificacion {

    int puntaje;
    String comentario;
    long fecha;

    public Calificacion() {
    }

    public Calificacion(int puntaje, String comentario, long fecha) {
        this.puntaje = puntaje;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

}
