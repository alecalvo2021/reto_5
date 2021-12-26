package com.example.reto2.modelo;

public class Producto {
    private int id;
    private final String nombre;
    private final String descripcion;
    private byte[] image;
    private final String valor;

    public Producto(int id, String nombre, String descripcion, String valor, byte[] image) {
        this.id=id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.valor = valor;
        this.image= image;
    }

    public Producto(String nombre, String descripcion, String valor, byte[] image) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.valor = valor;
        this.image= image;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getValor() {
        return valor;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
