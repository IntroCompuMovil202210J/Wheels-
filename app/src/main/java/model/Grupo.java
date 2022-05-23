package model;

import java.util.ArrayList;

public class Grupo {

    int idGrupo;
    String nombreGrupo;
    double tarifa;
    int cupo;
    double latitudAcuerdo;
    double longitudAcuerdo;
    ArrayList<String> preferenciasRuta;

    public Grupo() {
    }

    public Grupo(int idGrupo, String nombreGrupo, double tarifa, int cupo, double latitudAcuerdo, double longitudAcuerdo) {
        this.idGrupo = idGrupo;
        this.nombreGrupo = nombreGrupo;
        this.tarifa = tarifa;
        this.cupo = cupo;
        this.latitudAcuerdo = latitudAcuerdo;
        this.longitudAcuerdo = longitudAcuerdo;
    }

    public Grupo(int idGrupo, String nombreGrupo, double tarifa, int cupo, double latitudAcuerdo, double longitudAcuerdo, ArrayList<String> preferenciasRuta) {
        this.idGrupo = idGrupo;
        this.nombreGrupo = nombreGrupo;
        this.tarifa = tarifa;
        this.cupo = cupo;
        this.latitudAcuerdo = latitudAcuerdo;
        this.longitudAcuerdo = longitudAcuerdo;
        this.preferenciasRuta = preferenciasRuta;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public double getLatitudAcuerdo() {
        return latitudAcuerdo;
    }

    public void setLatitudAcuerdo(double latitudAcuerdo) {
        this.latitudAcuerdo = latitudAcuerdo;
    }

    public double getLongitudAcuerdo() {
        return longitudAcuerdo;
    }

    public void setLongitudAcuerdo(double longitudAcuerdo) {
        this.longitudAcuerdo = longitudAcuerdo;
    }

    public ArrayList<String> getPreferenciasRuta() {
        return preferenciasRuta;
    }

    public void setPreferenciasRuta(ArrayList<String> preferenciasRuta) {
        this.preferenciasRuta = preferenciasRuta;
    }

}
