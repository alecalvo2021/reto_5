package com.example.reto2.util;

import android.database.Cursor;

import com.example.reto2.modelo.Producto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductsRepository {
    public List<Producto> listProduct(Cursor cursor){
        List<Producto> products = new ArrayList<>();
        if(cursor.getCount()==0){
            return products;
        }else{
            while (cursor.moveToNext()){
                Producto producto  = new Producto(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getBlob(4)

                );
                products.add(producto);
            }
            return products;
        }
    }


    public Producto JsonToProduct(JSONObject jsonObject, byte[] image){
        Producto producto=null;
        try {
            producto = new Producto(jsonObject.getInt("id"),
                    jsonObject.getString("nombre"),
                    jsonObject.getString("descripcion"),
                    jsonObject.getString("valor"),
                    image

            );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  producto;
    }
}
