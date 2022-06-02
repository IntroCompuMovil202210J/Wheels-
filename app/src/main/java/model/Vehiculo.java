package model;

import android.os.Parcel;
import android.os.Parcelable;

public class Vehiculo implements Parcelable {

    String idVehiculo;
    String placa;
    int capacidad;
    String marca;
    String modelo;
    int anno;
    String urlImagen;

    public Vehiculo() {
    }

    public Vehiculo(String idVehiculo, String placa, int capacidad, String marca, String modelo, int anno) {
        this.idVehiculo = idVehiculo;
        this.placa = placa;
        this.capacidad = capacidad;
        this.marca = marca;
        this.modelo = modelo;
        this.anno = anno;
    }


    protected Vehiculo(Parcel in) {
        idVehiculo = in.readString();
        placa = in.readString();
        capacidad = in.readInt();
        marca = in.readString();
        modelo = in.readString();
        anno = in.readInt();
        urlImagen = in.readString();
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public static final Creator<Vehiculo> CREATOR = new Creator<Vehiculo>() {
        @Override
        public Vehiculo createFromParcel(Parcel in) {
            return new Vehiculo(in);
        }

        @Override
        public Vehiculo[] newArray(int size) {
            return new Vehiculo[size];
        }
    };

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public String getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(String idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idVehiculo);
        parcel.writeString(placa);
        parcel.writeInt(capacidad);
        parcel.writeString(marca);
        parcel.writeString(modelo);
        parcel.writeInt(anno);
        parcel.writeString(urlImagen);
    }
}
