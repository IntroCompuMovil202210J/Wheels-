package model;

import java.util.ArrayList;

public class Usuario {

    String idUsuario;
    String nombre;
    String apellido;
    String telefono;
    String urlFoto;
    double latitud;
    double longitud;

   /* ArrayList<Grupo> grupos = new ArrayList<>();
    ArrayList<Chat> chats = new ArrayList<>();
    */

    public Usuario() {
    }

    public Usuario(String idUsuario, String nombre, String apellido, String telefono, String urlFoto, double latitud, double longitud) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.urlFoto = urlFoto;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}