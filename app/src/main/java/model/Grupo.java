package model;

import java.util.ArrayList;

public class Grupo {

    String nombreGrupo;
    String idConductor;
    double tarifa;
    int cupo;
    double latitudAcuerdo;
    double longitudAcuerdo;
    double latitudDestino;
    double longitudDestino;
    long fechaAcuerdo;
    ArrayList<String> preferenciasRuta;

    public Grupo() {
    }

    public Grupo(String nombreGrupo, String idConductor, double tarifa, int cupo, double latitudAcuerdo, double longitudAcuerdo, double latitudDestino, double longitudDestino, long fechaAcuerdo) {
        this.nombreGrupo = nombreGrupo;
        this.idConductor = idConductor;
        this.tarifa = tarifa;
        this.cupo = cupo;
        this.latitudAcuerdo = latitudAcuerdo;
        this.longitudAcuerdo = longitudAcuerdo;
        this.latitudDestino = latitudDestino;
        this.longitudDestino = longitudDestino;
        this.fechaAcuerdo = fechaAcuerdo;
    }

    public Grupo(String nombreGrupo, String idConductor, double tarifa, int cupo, double latitudAcuerdo, double longitudAcuerdo, double latitudDestino, double longitudDestino, long fechaAcuerdo, ArrayList<String> preferenciasRuta) {
        this.nombreGrupo = nombreGrupo;
        this.idConductor = idConductor;
        this.tarifa = tarifa;
        this.cupo = cupo;
        this.latitudAcuerdo = latitudAcuerdo;
        this.longitudAcuerdo = longitudAcuerdo;
        this.latitudDestino = latitudDestino;
        this.longitudDestino = longitudDestino;
        this.fechaAcuerdo = fechaAcuerdo;
        this.preferenciasRuta = preferenciasRuta;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(String idConductor) {
        this.idConductor = idConductor;
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

    public long getFechaAcuerdo() {
        return fechaAcuerdo;
    }

    public void setFechaAcuerdo(long fechaAcuerdo) {
        this.fechaAcuerdo = fechaAcuerdo;
    }

    public ArrayList<String> getPreferenciasRuta() {
        return preferenciasRuta;
    }

    public void setPreferenciasRuta(ArrayList<String> preferenciasRuta) {
        this.preferenciasRuta = preferenciasRuta;
    }

}
