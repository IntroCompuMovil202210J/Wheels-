package display;

import android.os.Parcel;
import android.os.Parcelable;

public class DisplayGroup implements Parcelable {

    String nombreConductor;
    String nombreGrupo;
    String idConductor;
    String tarifa;
    String origen;
    String destino;
    String urlFoto;
    String idGrupo;
    String fecha;


    double latOrigin, lonOrigin;



    public DisplayGroup() {
    }

    public DisplayGroup(String nombreConductor, String nombreGrupo, String idConductor, String tarifa, String origen, String destino, String urlFoto, String idGrupo, String fecha) {
        this.nombreConductor = nombreConductor;
        this.nombreGrupo = nombreGrupo;
        this.idConductor = idConductor;
        this.tarifa = tarifa;
        this.origen = origen;
        this.destino = destino;
        this.urlFoto = urlFoto;
        this.idGrupo = idGrupo;
        this.fecha = fecha;
    }

    public DisplayGroup(String nombreConductor, String nombreGrupo, String idConductor, String tarifa, String origen, String destino, String urlFoto, String idGrupo, String fecha, double latOrigin, double lonOrigin) {
        this.nombreConductor = nombreConductor;
        this.nombreGrupo = nombreGrupo;
        this.idConductor = idConductor;
        this.tarifa = tarifa;
        this.origen = origen;
        this.destino = destino;
        this.urlFoto = urlFoto;
        this.idGrupo = idGrupo;
        this.fecha = fecha;
        this.latOrigin = latOrigin;
        this.lonOrigin = lonOrigin;
    }

    public double getLatOrigin() {
        return latOrigin;
    }

    public void setLatOrigin(double latOrigin) {
        this.latOrigin = latOrigin;
    }

    public double getLonOrigin() {
        return lonOrigin;
    }

    public void setLonOrigin(double lonOrigin) {
        this.lonOrigin = lonOrigin;
    }

    protected DisplayGroup(Parcel in) {
        nombreConductor = in.readString();
        nombreGrupo = in.readString();
        idConductor = in.readString();
        tarifa = in.readString();
        origen = in.readString();
        destino = in.readString();
        urlFoto = in.readString();
        idGrupo = in.readString();
        fecha = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombreConductor);
        dest.writeString(nombreGrupo);
        dest.writeString(idConductor);
        dest.writeString(tarifa);
        dest.writeString(origen);
        dest.writeString(destino);
        dest.writeString(urlFoto);
        dest.writeString(idGrupo);
        dest.writeString(fecha);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DisplayGroup> CREATOR = new Creator<DisplayGroup>() {
        @Override
        public DisplayGroup createFromParcel(Parcel in) {
            return new DisplayGroup(in);
        }

        @Override
        public DisplayGroup[] newArray(int size) {
            return new DisplayGroup[size];
        }
    };

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
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

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
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

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
