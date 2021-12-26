package com.example.reto2;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.reto2.datos.ApiOracle;
import com.example.reto2.datos.DBHelper;
import com.example.reto2.util.HandlingImages;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class FormActivity extends AppCompatActivity implements  View.OnClickListener {
    private final int REQUEST_CODE_GALLERY = 999;
    private final int ADD=1;
    private final int UPDATE=2;
    private final int DELETE=3;

    private TextView textView;
    private EditText field1, field2, field3, field4;
    private Button  btnInsert, btnChose, btnDelete, btnUpdate, btnSearch;
    private ImageView imageView;
    private DBHelper dbHelper;
    private ApiOracle apiOracle;
    private String name;
    String field1Insert;
    String field2Insert;
    String field3Insert;
    byte []imageInsert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new DBHelper(getApplicationContext());
        apiOracle = new ApiOracle(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        textView = findViewById(R.id.txtMensage);
        field1 = findViewById(R.id.editField1);
        field2 =  findViewById(R.id.editField2);
        field3 = findViewById(R.id.editField3);
        field4 = findViewById(R.id.txtId);
        btnInsert = findViewById(R.id.btnInsertar);
        btnChose =findViewById(R.id.btnChoose);
        btnDelete= findViewById(R.id.btnDelete);
        btnUpdate=findViewById(R.id.btnUpdate);
        btnSearch=findViewById(R.id.btnSearch);
        imageView = findViewById(R.id.imgSlected);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        textView.setText(name);
        if("PRODUCTS".equals(name)){
            field1.setHint("Nombre");
            field2.setHint("Descripcion");
            field3.setHint("Precio");
            field4.setHint("ID");

        }else if("SERVICES".equals(name)){
            field1.setHint("Nombre");
            field2.setHint("Descripcion");
            field3.setHint("Precio");
            field4.setHint("ID");

        }else if("STORES".equals(name)){
            field1.setHint("Nombre");
            field2.setHint("Descripcion");
            field3.setHint("Ubicacion");
            field3.setInputType(InputType.TYPE_CLASS_TEXT);
            field4.setHint("ID");
        }

        btnChose.setOnClickListener(this);
        btnInsert.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

    }

    public void fillFields(){

        field1Insert = field1.getText().toString().trim();
        field2Insert = field2.getText().toString().trim();
        field3Insert = field3.getText().toString().trim();
        imageInsert = new HandlingImages().imageViewToByte(imageView);
    }

    public void cleanFilds(){
        field1.setText("");
        field2.setText("");
        field3.setText("");
        field4.setText("");
        imageView.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if(requestCode == REQUEST_CODE_GALLERY){
           if(grantResults.length >0  && grantResults[0] == getPackageManager().PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
           }else{
               Toast.makeText(getApplicationContext(), "Sin Autorizacion", Toast.LENGTH_SHORT).show();
           }
       }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY && resultCode==RESULT_OK && data !=null){
            Uri uri =data.getData();
            try{
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

            }catch (FileNotFoundException e){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnInsertar:insert(view);
            break;
            case R.id.btnChoose:selectImage();
            break;
            case  R.id.btnSearch:search();
            break;
            case  R.id.btnDelete:delete(view);
            break;
            case R.id.btnUpdate:update(view);
        }


    }

    public void insert(View view){
        operation(view, "Agregar", "Seguro Desea Agregar", ADD);

    }

    public void search(){

        //HandlingImages handlingImages= new HandlingImages();

         apiOracle.getDataById(name, field4.getText().toString().trim(), field1, field2, field3, imageView);

       /* if(data==null){
            Toast toast = Toast.makeText(getApplicationContext(), "No encontrado ", Toast.LENGTH_SHORT);
            View view=toast.getView();
            TextView  view1=(TextView)view.findViewById(android.R.id.message);
            view1.setTextColor(Color.rgb(255, 255, 255));
            view.setBackgroundResource(R.color.cafe);
            toast.show();

        }*/




    }

    public void delete(View  view){
        operation(view, "Eliminar", "Seguro Desea Eliminar", DELETE);
    }

    public void update(View view){
        operation(view, "Actualizar", "Seguro Desea Actualizar", UPDATE);
    }
    public void selectImage(){
        ActivityCompat.requestPermissions(
                FormActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY
        );

    }

    public void operation(View view, String title, String mesage, int operacion){
        View views = findViewById(R.id.formApp);


        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle(title)
                    .setMessage(mesage)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String respuesta="";
                            switch (operacion){
                                //case ADD: dbHelper.insertData(field1Insert, field2Insert,field3Insert, imageInsert, name);
                                case ADD: apiOracle.insertData(field1Insert, field2Insert,field3Insert, imageView, name);
                                    respuesta="Agregado";
                                    break;
                              //case  UPDATE:dbHelper.updateDataById(field4.getText().toString().trim(),name,field1Insert, field2Insert,field3Insert, imageInsert);
                                  case  UPDATE:apiOracle.updateData(field4.getText().toString().trim(),field1Insert, field2Insert,field3Insert, imageView, name);
                                    respuesta="ACtualizado";
                                    break;
                             //   case  DELETE:   dbHelper.deteteDataById(name, field4.getText().toString().trim());
                                case  DELETE:apiOracle.deleteDataById(name, field4.getText().toString().trim());
                                    respuesta="Eliminado";
                                    break;
                            }

                            cleanFilds();
                            Toast toast =Toast.makeText(view.getContext(), respuesta, Toast.LENGTH_SHORT);
                            View view=toast.getView();
                            TextView  view1=(TextView)view.findViewById(android.R.id.message);
                            view1.setTextColor(Color.rgb(255, 255, 255));
                            view.setBackgroundResource(R.color.cafe);
                            toast.show();

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Operacion Cancelada ", Toast.LENGTH_SHORT);
                            View view=toast.getView();
                            TextView  view1=(TextView)view.findViewById(android.R.id.message);
                            view1.setTextColor(Color.rgb(255, 255, 255));
                            view.setBackgroundResource(R.color.cafe);
                            toast.show();
                        }
                    })
                    .setCancelable(false);
            AlertDialog alert = builder.create();
            alert.show();
            fillFields();

        }catch (Exception e){
            Snackbar.make(view, "Ocurrio Un error", Snackbar.LENGTH_SHORT).show();

        }
    }

}