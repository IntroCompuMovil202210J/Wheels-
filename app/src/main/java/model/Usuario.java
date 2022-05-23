package model;

import java.util.ArrayList;

public class Usuario {

    String nombre;
    String apellido;
    String telefono;
    String urlFoto;
    double latitud;
    double longitud;

    ArrayList<Grupo> grupos = new ArrayList<>();
    ArrayList<Viaje> viajes = new ArrayList<>();
    ArrayList<Chat> chats = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String telefono, String urlFoto, double latitud, double longitud) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.urlFoto = urlFoto;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Usuario(String nombre, String apellido, String telefono, String urlFoto, double latitud, double longitud, ArrayList<Grupo> grupos, ArrayList<Viaje> viajes, ArrayList<Chat> chats) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.urlFoto = urlFoto;
        this.latitud = latitud;
        this.longitud = longitud;
        this.grupos = grupos;
        this.viajes = viajes;
        this.chats = chats;
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

    public ArrayList<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(ArrayList<Grupo> grupos) {
        this.grupos = grupos;
    }

    public ArrayList<Viaje> getViajes() {
        return viajes;
    }

    public void setViajes(ArrayList<Viaje> viajes) {
        this.viajes = viajes;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }

}
