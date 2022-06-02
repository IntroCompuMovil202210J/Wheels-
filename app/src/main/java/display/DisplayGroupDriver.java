package display;

import android.os.Parcel;
import android.os.Parcelable;

public class DisplayGroupDriver implements Parcelable {

    String nombre;
    String origen;
    String destino;
    String fecha;
    String tarifa;
    String placa;
    String marca;
    String idGrupo;
    String modelo;
    String urlImagen;

    public DisplayGroupDriver() {
    }

    public DisplayGroupDriver(String nombre, String origen, String destino, String fecha, String tarifa, String placa, String marca, String idGrupo, String modelo, String urlImagen) {
        this.nombre = nombre;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.tarifa = tarifa;
        this.placa = placa;
        this.marca = marca;
        this.idGrupo = idGrupo;
        this.modelo = modelo;
        this.urlImagen = urlImagen;
    }

    protected DisplayGroupDriver(Parcel in) {
        nombre = in.readString();
        origen = in.readString();
        destino = in.readString();
        fecha = in.readString();
        tarifa = in.readString();
        placa = in.readString();
        marca = in.readString();
        idGrupo = in.readString();
        modelo = in.readString();
        urlImagen = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(origen);
        dest.writeString(destino);
        dest.writeString(fecha);
        dest.writeString(tarifa);
        dest.writeString(placa);
        dest.writeString(marca);
        dest.writeString(idGrupo);
        dest.writeString(modelo);
        dest.writeString(urlImagen);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DisplayGroupDriver> CREATOR = new Creator<DisplayGroupDriver>() {
        @Override
        public DisplayGroupDriver createFromParcel(Parcel in) {
            return new DisplayGroupDriver(in);
        }

        @Override
        public DisplayGroupDriver[] newArray(int size) {
            return new DisplayGroupDriver[size];
        }
    };

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}
