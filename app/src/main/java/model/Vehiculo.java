package model;

public class Vehiculo {

    String placa;
    int capacidad;
    String marca;
    String modelo;
    int anno;

    public Vehiculo() {
    }

    public Vehiculo(String placa, int capacidad, String marca, String modelo, int anno) {
        this.placa = placa;
        this.capacidad = capacidad;
        this.marca = marca;
        this.modelo = modelo;
        this.anno = anno;
    }

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

}
