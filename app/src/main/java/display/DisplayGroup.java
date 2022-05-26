package display;

import android.os.Parcel;
import android.os.Parcelable;

public class DisplayGroup implements Parcelable {

    String nombreConductor;
    String nombreGrupo;
    String origen;
    String destino;
    String urlFoto;

    public DisplayGroup() {
    }

    public DisplayGroup(String nombreConductor, String nombreGrupo, String origen, String destino, String urlFoto) {
        this.nombreConductor = nombreConductor;
        this.nombreGrupo = nombreGrupo;
        this.origen = origen;
        this.destino = destino;
        this.urlFoto = urlFoto;
    }

    protected DisplayGroup(Parcel in) {
        nombreConductor = in.readString();
        nombreGrupo = in.readString();
        origen = in.readString();
        destino = in.readString();
        urlFoto = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nombreConductor);
        parcel.writeString(nombreGrupo);
        parcel.writeString(origen);
        parcel.writeString(destino);
        parcel.writeString(urlFoto);
    }
}
