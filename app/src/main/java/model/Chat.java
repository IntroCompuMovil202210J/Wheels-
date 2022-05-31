package model;

import java.util.ArrayList;

public class Chat {

    String idChat;
    String idEmisor;
    String idReceptor;
    ArrayList<Mensaje> mensajes;

    public Chat() {
    }

    public Chat(String idChat, String idEmisor, String idReceptor) {
        this.idChat = idChat;
        this.idEmisor = idEmisor;
        this.idReceptor = idReceptor;
    }

    public Chat(String idChat, String idEmisor, String idReceptor, ArrayList<Mensaje> mensajes) {
        this.idChat = idChat;
        this.idEmisor = idEmisor;
        this.idReceptor = idReceptor;
        this.mensajes = mensajes;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getIdEmisor() {
        return idEmisor;
    }

    public void setIdEmisor(String idEmisor) {
        this.idEmisor = idEmisor;
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
