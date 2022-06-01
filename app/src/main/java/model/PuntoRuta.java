package model;

public class PuntoRuta {

    String idUsuario;
    Double latitud;
    Double longitud;

    public PuntoRuta() {
    }

    public PuntoRuta(String idUsuario, Double latitud, Double longitud) {
        this.idUsuario = idUsuario;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
