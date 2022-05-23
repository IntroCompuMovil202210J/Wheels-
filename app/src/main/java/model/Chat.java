package model;

import java.util.ArrayList;

public class Chat {

    String idReceptor;
    ArrayList<Mensaje> mensajes;

    public Chat() {
    }

    public Chat(String idReceptor) {
        this.idReceptor = idReceptor;
    }

    public Chat(String idReceptor, ArrayList<Mensaje> mensajes) {
        this.idReceptor = idReceptor;
        this.mensajes = mensajes;
    }

    public String getIdReceptor() {
        return idReceptor;
    }

    public void setIdReceptor(String idReceptor) {
        this.idReceptor = idReceptor;
    }

    public ArrayList<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(ArrayList<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

}
