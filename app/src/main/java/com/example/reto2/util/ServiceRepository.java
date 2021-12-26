package com.example.reto2.util;

import android.database.Cursor;

import com.example.reto2.modelo.Servicio;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceRepository {
    public List<Servicio> listService(Cursor cursor){
        List<Servicio> servicios = new ArrayList<>();
        if(cursor.getCount()==0){
            return servicios;
        }else{
            while (cursor.moveToNext()){
                Servicio servicio = new Servicio(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getBlob(4)

                );


                servicios.add(servicio);
            }
            return servicios;
        }
    }

    public Servicio JsonToServicio(JSONObject jsonObject, byte[] image){
        Servicio servicio=null;
        try {
            servicio = new Servicio(jsonObject.getInt("id"),
                    jsonObject.getString("nombre"),
                    jsonObject.getString("descripcion"),
                    jsonObject.getString("valor"),
                    image

            );
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return servicio;
    }
}

