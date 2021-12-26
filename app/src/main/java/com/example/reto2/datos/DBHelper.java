package com.example.reto2.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
private SQLiteDatabase sqLiteDatabase;
    public DBHelper(@Nullable Context context) {
        super(context,"Constructor.db",null, 1);
        sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE PRODUCTS(" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR," +
                "description VARCHAR," +
                "price VARCHAR," +
                "image BLOB"+
        ")");

        db.execSQL("CREATE TABLE SERVICES(" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR," +
                "description  VARCHAR," +
                "price VARCHAR," +
                "image BLOB"+
                ")");

        db.execSQL("CREATE TABLE STORES(" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR," +
                "description VARCHAR," +
                "location VARCHAR," +
                "image BLOB"+
                ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PRODUCTS");
        db.execSQL("DROP TABLE IF EXISTS SERVICES");
        db.execSQL("DROP TABLE IF EXISTS STORES");

    }
    public void insertData(String field1, String field2, String field3, byte[] image, String nameTable ){
        String sql="INSERT INTO "+ nameTable+ " VALUES(null,?,?,?,?)";
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,field1);
        statement.bindString(2,field2);
        statement.bindString(3,field3);
        statement.bindBlob(4,image);
        statement.executeInsert();

    }

    public Cursor searchDataById(String nameTable, String id){
      Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM "+nameTable+ " WHERE id = "+id,null);
        return cursor;

    }
    public Cursor getData(String nameTable){
        Cursor cursor= sqLiteDatabase.rawQuery("SELECT * FROM "+nameTable, null);
        return  cursor;
    }
    public void deteteDataById(String nameTable, String id){
        sqLiteDatabase.execSQL("DELETE FROM "+nameTable+ " WHERE id = "+id);

    }

    public void updateDataById(String id, String nameTable, String field1, String field2, String field3, byte[] image){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name ", field1);
        contentValues.put("description", field2);
        if("STORES".equals(nameTable)){
            contentValues.put("location", field3);
        }else{
            contentValues.put("price", field3);
        }

        contentValues.put("image", image);
        sqLiteDatabase.update(nameTable, contentValues, "id =?",new String[]{id});

    }
}
