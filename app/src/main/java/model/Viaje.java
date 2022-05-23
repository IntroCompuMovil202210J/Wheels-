package model;

public class Viaje {

    long fecha;
    double latitudDestino;
    double longitudDestino;
    double latitudOrigen;
    double longitudOrigen;

    public Viaje() {
    }

    public Viaje(long fecha, double latitudDestino, double longitudDestino, double latitudOrigen, double longitudOrigen) {
        this.fecha = fecha;
        this.latitudDestino = latitudDestino;
        this.longitudDestino = longitudDestino;
        this.latitudOrigen = latitudOrigen;
        this.longitudOrigen = longitudOrigen;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public double getLatitudDestino() {
        return latitudDestino;
    }

    public void setLatitudDestino(double latitudDestino) {
        this.latitudDestino = latitudDestino;
    }

    public double getLongitudDestino() {
        return longitudDestino;
    }

    public void setLongitudDestino(double longitudDestino) {
        this.longitudDestino = longitudDestino;
    }

    public double getLatitudOrigen() {
        return latitudOrigen;
    }

    public void setLatitudOrigen(double latitudOrigen) {
        this.latitudOrigen = latitudOrigen;
    }

    public double getLongitudOrigen() {
        return longitudOrigen;
    }

    public void setLongitudOrigen(double longitudOrigen) {
        this.longitudOrigen = longitudOrigen;
    }

}
