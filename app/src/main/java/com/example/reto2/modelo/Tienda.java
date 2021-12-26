package com.example.reto2.modelo;

import com.google.android.gms.maps.model.LatLng;

public class Tienda {


    private  String nombreTienda;
    private  String description;
    private  String ubicacion;
    private byte[] imagenTienda;
    private  int id;

    public Tienda(String nombreTienda, String ubicacionTienda) {
        this.nombreTienda = nombreTienda;
        this.ubicacion = ubicacionTienda;

    }

    public Tienda(int id, String nombreTienda, String descriptionTienda, String ubicacionTienda, byte [] imagenTienda) {
        this.id=id;
        this.nombreTienda = nombreTienda;
        this.description = descriptionTienda;
        this.ubicacion = ubicacionTienda;
        this.imagenTienda = imagenTienda;
    }


    public Tienda(String nombreTienda,  String descriptionTienda ,String ubicacionTienda, byte[] imagenTienda) {
        this.nombreTienda = nombreTienda;
        this.description = descriptionTienda;
        this.ubicacion = ubicacionTienda;
        this.imagenTienda = imagenTienda;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }


    public String getDescription() {
        return description;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public byte[] getImagenTienda() {
        return imagenTienda;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LatLng locationToCoord(){
        System.out.println("la ubicacion es =>"+ubicacion);
        String[] coords = this.ubicacion.split(",");
        Double latitud = Double.parseDouble(coords[0]);
        Double longitud = Double.parseDouble(coords[1]);
        LatLng latLng = new LatLng(latitud,longitud);
        return latLng;
    }
}

