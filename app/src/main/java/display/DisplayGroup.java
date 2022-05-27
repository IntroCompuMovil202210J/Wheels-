package display;

import android.os.Parcel;
import android.os.Parcelable;

public class DisplayGroup implements Parcelable {

    String nombreConductor;
    String nombreGrupo;
    String tarifa;
    String origen;
    String destino;
    String urlFoto;
    String idGrupo;
    String fecha;

    public DisplayGroup() {
    }

    public DisplayGroup(String nombreConductor, String nombreGrupo, String tarifa, String origen, String destino, String urlFoto, String idGrupo, String fecha) {
        this.nombreConductor = nombreConductor;
        this.nombreGrupo = nombreGrupo;
        this.tarifa = tarifa;
        this.origen = origen;
        this.destino = destino;
        this.urlFoto = urlFoto;
        this.idGrupo = idGrupo;
        this.fecha = fecha;
    }

    protected DisplayGroup(Parcel in) {
        nombreConductor = in.readString();
        nombreGrupo = in.readString();
        tarifa = in.readString();
        origen = in.readString();
        destino = in.readString();
        urlFoto = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombreConductor);
        dest.writeString(nombreGrupo);
        dest.writeString(tarifa);
        dest.writeString(origen);
        dest.writeString(destino);
        dest.writeString(urlFoto);
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
