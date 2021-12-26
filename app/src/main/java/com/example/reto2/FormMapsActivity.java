package com.example.reto2;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.reto2.databinding.ActivityFormMapsBinding;
import com.example.reto2.datos.ApiOracle;
import com.example.reto2.datos.DBHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class FormMapsActivity extends FragmentActivity implements OnMapReadyCallback,  View.OnClickListener {
    private final int REQUEST_CODE_GALLERY = 999;
    private final int ADD=1;
    private final int UPDATE=2;
    private final int DELETE=3;
    private GoogleMap mMap;
    private String nameTable;
    private DBHelper dbHelper;
    private ApiOracle apiOracle;
    private ActivityFormMapsBinding binding;
    private EditText id, name, description;
    private ImageView imagestore;
    private TextView location;
    private Button btnInsert, btnSearch, btnUpdate, btnDelete, btnChoose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityFormMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        nameTable = intent.getStringExtra("name");
        dbHelper = new DBHelper(getApplicationContext());
        apiOracle = new ApiOracle(getApplicationContext());
        id =(EditText) findViewById(R.id.txtId);
        name=(EditText) findViewById(R.id.edName);
        description=(EditText) findViewById(R.id.edDescription);
        location =(TextView) findViewById(R.id.txtLocation);
        btnInsert = (Button)  findViewById(R.id.btnInsert);
        btnUpdate = (Button)  findViewById(R.id.btnEdit);
        btnDelete = (Button)  findViewById(R.id.btnDel);
        btnChoose=  (Button) findViewById(R.id.btnChooseTiendas);
        btnChoose.setOnClickListener(this);
        btnSearch = (Button)  findViewById(R.id.btnSearchT);
        imagestore = (ImageView) findViewById(R.id.imgTienda);
        imagestore.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnInsert.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        byte zoom=5;
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng bogota = new LatLng(4, -74);
        mMap.addMarker(new MarkerOptions().position(bogota).title("Marker in Bogota"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bogota,zoom));
       // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom());
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                location.setText(latLng.latitude+", "+latLng.longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.clear();
                mMap.addMarker( new MarkerOptions().position(latLng));
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnInsert:insert(view);
               break;
            case R.id.btnChooseTiendas:selectImages();
                break;
            case  R.id.btnSearchT:search();
              break;
            case  R.id.btnDel:delete(view);
              break;
           case R.id.btnEdit:update(view);
             break;
            //case R.id.map:MarkMap();
        }

    }



    public void selectImages(){
        ActivityCompat.requestPermissions(
                FormMapsActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY
        );

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
                imagestore.setImageBitmap(bitmap);

            }catch (FileNotFoundException e){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void insert(View view){
        operation(view, "Agregar", "Seguro Desea Agregar", ADD);

    }

    public void search(){
        apiOracle.getDataById(nameTable, id.getText().toString().trim(), name, description, location, imagestore, mMap);

        /*Cursor cursor= dbHelper.searchDataById(nameTable, id.getText().toString().trim());

        if(cursor.getCount()<1){
            Toast toast = Toast.makeText(getApplicationContext(), "No encontrado ", Toast.LENGTH_SHORT);
            View view=toast.getView();
            TextView  view1=(TextView)view.findViewById(android.R.id.message);
            view1.setTextColor(Color.rgb(255, 255, 255));
            view.setBackgroundResource(R.color.cafe);
            toast.show();

        }
        while(cursor.moveToNext()){
            name.setText(cursor.getString(1));
            description.setText(cursor.getString(2));
            location.setText(cursor.getString(3));
            imagestore.setImageBitmap(new HandlingImages().imagetoBitmap(cursor.getBlob(4)));

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
                FormMapsActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY
        );

    }

    public void cleanFilds(){
        id.setText("");
        name.setText("");
        description.setText("");
        location.setText("");
        imagestore.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
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
                                //case ADD: dbHelper.insertData(name.getText().toString().trim(), description.getText().toString().trim(),location.getText().toString().trim(), new HandlingImages().imageViewToByte(imagestore), nameTable);
                                case ADD:apiOracle.insertData(name.getText().toString().trim(), description.getText().toString().trim(),location.getText().toString().trim(), imagestore, nameTable);
                                    respuesta="Agregado";
                                    break;
                              // case  UPDATE:dbHelper.updateDataById(id.getText().toString().trim(),nameTable,name.getText().toString().trim(), description.getText().toString().trim(),location.getText().toString().trim(), new HandlingImages().imageViewToByte(imagestore));
                                  case  UPDATE:apiOracle.updateData(id.getText().toString().trim(), name.getText().toString().trim(), description.getText().toString().trim(),location.getText().toString().trim(), imagestore, nameTable);
                                    respuesta="ACtualizado";
                                    break;
                               // case  DELETE:   dbHelper.deteteDataById(nameTable, id.getText().toString().trim());
                                case DELETE: apiOracle.deleteDataById(nameTable, id.getText().toString().trim());
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


        }catch (Exception e){
            Snackbar.make(view, "Ocurrio Un error", Snackbar.LENGTH_SHORT).show();

        }
    }

}