package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Grupo implements Parcelable {

    String id_Grupo;
    String nombreGrupo;
    String idConductor;
    double tarifa;
    int cupo;
    double latitudAcuerdo;
    double longitudAcuerdo;
    double latitudDestino;
    double longitudDestino;

    double latUser, lonUser;

    long fechaAcuerdo;
    String idVehiculo;
    ArrayList<String> preferenciasRuta;

    public Grupo() {
    }

    public Grupo(String id_Grupo, String nombreGrupo, String idConductor, double tarifa, int cupo, double latitudAcuerdo, double longitudAcuerdo, double latitudDestino, double longitudDestino, long fechaAcuerdo, String idVehiculo) {
        this.id_Grupo = id_Grupo;
        this.nombreGrupo = nombreGrupo;
        this.idConductor = idConductor;
        this.tarifa = tarifa;
        this.cupo = cupo;
        this.latitudAcuerdo = latitudAcuerdo;
        this.longitudAcuerdo = longitudAcuerdo;
        this.latitudDestino = latitudDestino;
        this.longitudDestino = longitudDestino;
        this.fechaAcuerdo = fechaAcuerdo;
        this.idVehiculo = idVehiculo;
    }

    protected Grupo(Parcel in) {
        id_Grupo = in.readString();
        nombreGrupo = in.readString();
        idConductor = in.readString();
        tarifa = in.readDouble();
        cupo = in.readInt();
        latitudAcuerdo = in.readDouble();
        longitudAcuerdo = in.readDouble();
        latitudDestino = in.readDouble();
        longitudDestino = in.readDouble();
        latUser = in.readDouble();
        lonUser = in.readDouble();
        fechaAcuerdo = in.readLong();
        idVehiculo = in.readString();
        preferenciasRuta = in.createStringArrayList();
    }

    public static final Creator<Grupo> CREATOR = new Creator<Grupo>() {
        @Override
        public Grupo createFromParcel(Parcel in) {
            return new Grupo(in);
        }

        @Override
        public Grupo[] newArray(int size) {
            return new Grupo[size];
        }
    };

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

    public String getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(String idVehiculo) {
        this.idVehiculo = idVehiculo;
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

    public String getId_Grupo() {
        return id_Grupo;
    }

    public void setId_Grupo(String id_Grupo) {
        this.id_Grupo = id_Grupo;
    }

    public double getLatUser() {
        return latUser;
    }

    public void setLatUser(double latUser) {
        this.latUser = latUser;
    }

    public double getLonUser() {
        return lonUser;
    }

    public void setLonUser(double lonUser) {
        this.lonUser = lonUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id_Grupo);
        parcel.writeString(nombreGrupo);
        parcel.writeString(idConductor);
        parcel.writeDouble(tarifa);
        parcel.writeInt(cupo);
        parcel.writeDouble(latitudAcuerdo);
        parcel.writeDouble(longitudAcuerdo);
        parcel.writeDouble(latitudDestino);
        parcel.writeDouble(longitudDestino);
        parcel.writeDouble(latUser);
        parcel.writeDouble(lonUser);
        parcel.writeLong(fechaAcuerdo);
        parcel.writeString(idVehiculo);
        parcel.writeStringList(preferenciasRuta);
    }
}
