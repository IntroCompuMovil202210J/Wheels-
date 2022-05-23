package model;

import java.util.ArrayList;

public class Conductor {

    int cantidadViajes;
    String idUsuario;

    ArrayList<Calificacion> calificaciones = new ArrayList<>();
    ArrayList<Vehiculo> vehiculos = new ArrayList<>();

    public Conductor() {
    }

    public Conductor(int cantidadViajes, String idUsuario) {
        this.cantidadViajes = cantidadViajes;
        this.idUsuario = idUsuario;
    }

    public Conductor(int cantidadViajes, String idUsuario, ArrayList<Calificacion> calificaciones, ArrayList<Vehiculo> vehiculos) {
        this.cantidadViajes = cantidadViajes;
        this.idUsuario = idUsuario;
        this.calificaciones = calificaciones;
        this.vehiculos = vehiculos;
    }

    public int getCantidadViajes() {
        return cantidadViajes;
    }

    public void setCantidadViajes(int cantidadViajes) {
        this.cantidadViajes = cantidadViajes;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public ArrayList<Calificacion> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(ArrayList<Calificacion> calificaciones) {
        this.calificaciones = calificaciones;
    }

    public ArrayList<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(ArrayList<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

}
