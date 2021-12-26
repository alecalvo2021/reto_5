package com.example.reto2.util;

import android.database.Cursor;

import com.example.reto2.modelo.Servicio;
import com.example.reto2.modelo.Tienda;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TiendaRepository {
    public List<Tienda> listTiendas(Cursor cursor){
        List<Tienda>  tiendas= new ArrayList<>();
        if(cursor.getCount()==0){
            return tiendas;
        }else{
            while (cursor.moveToNext()){
                Tienda tienda = new Tienda(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getBlob(4)

                );


                tiendas.add(tienda);
            }
            return tiendas;
        }
    }


    public Tienda JsonToTienda(JSONObject jsonObject, byte[] image){
        Tienda tienda=null;
        try {
            tienda = new Tienda(jsonObject.getInt("id"),
                    jsonObject.getString("nombre"),
                    jsonObject.getString("descripcion"),
                    jsonObject.getString("ubicacion"),
                    image
            );
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return tienda;
    }
}
