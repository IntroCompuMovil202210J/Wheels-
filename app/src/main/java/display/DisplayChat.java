package display;

import android.os.Parcel;
import android.os.Parcelable;

public class DisplayChat implements Parcelable {

    String idChat;
    String urlFoto;
    String nombreChat;
    String idOther;
    String ultimoMensaje;
    String horaUltimoMensaje;

    public DisplayChat() {
    }

    public DisplayChat(String idChat, String urlFoto, String nombreChat, String idOther, String ultimoMensaje, String horaUltimoMensaje) {
        this.idChat = idChat;
        this.urlFoto = urlFoto;
        this.nombreChat = nombreChat;
        this.idOther = idOther;
        this.ultimoMensaje = ultimoMensaje;
        this.horaUltimoMensaje = horaUltimoMensaje;
    }

    protected DisplayChat(Parcel in) {
        idChat = in.readString();
        urlFoto = in.readString();
        nombreChat = in.readString();
        idOther = in.readString();
        ultimoMensaje = in.readString();
        horaUltimoMensaje = in.readString();
    }

    public static final Creator<DisplayChat> CREATOR = new Creator<DisplayChat>() {
        @Override
        public DisplayChat createFromParcel(Parcel in) {
            return new DisplayChat(in);
        }

        @Override
        public DisplayChat[] newArray(int size) {
            return new DisplayChat[size];
        }
    };

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getNombreChat() {
        return nombreChat;
    }

    public void setNombreChat(String nombreChat) {
        this.nombreChat = nombreChat;
    }

    public String getIdOther() {
        return idOther;
    }

    public void setIdOther(String idOther) {
        this.idOther = idOther;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }

    public String getHoraUltimoMensaje() {
        return horaUltimoMensaje;
    }

    public void setHoraUltimoMensaje(String horaUltimoMensaje) {
        this.horaUltimoMensaje = horaUltimoMensaje;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idChat);
        parcel.writeString(urlFoto);
        parcel.writeString(nombreChat);
        parcel.writeString(idOther);
        parcel.writeString(ultimoMensaje);
        parcel.writeString(horaUltimoMensaje);
    }
}
