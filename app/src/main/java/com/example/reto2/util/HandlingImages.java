package com.example.reto2.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class HandlingImages {

    public Bitmap imagetoBitmap(byte[] image){

        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
        return bitmap;
    }

    public byte[] imageViewToByte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public String imaViewToString(ImageView imageView){
       byte [] byteArray = imageViewToByte(imageView);
       String result = java.util.Base64.getEncoder().encodeToString(byteArray);
       return  result;
    }

    public byte[] strigToByte(String cadena){
        byte[] result = java.util.Base64.getDecoder().decode(cadena);
        return  result;
    }
}

