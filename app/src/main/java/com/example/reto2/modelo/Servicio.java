package com.example.reto2.modelo;

public class Servicio {

    private final String nombreServicio;
    private final String descripcionServicio;
    private byte[] imageService;
    private String valor;
    private int id;


    public Servicio(int id, String nombreServicio, String descripcionServicio, String valor, byte[] imageService) {
        this.id=id;
        this.nombreServicio = nombreServicio;
        this.descripcionServicio = descripcionServicio;
        this.valor=valor;
        this.imageService = imageService;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public void setImagenServicio(byte[] imageService) {
        this.imageService = imageService;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImageService() {
        return imageService;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void setImageService(byte[] imageService) {
        this.imageService = imageService;
    }
}
