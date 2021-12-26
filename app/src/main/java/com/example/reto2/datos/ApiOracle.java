package com.example.reto2.datos;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.reto2.R;
import com.example.reto2.adaptadores.ProductosAdapter;
import com.example.reto2.adaptadores.ServiciosAdapter;
import com.example.reto2.adaptadores.TiendaAdapter;
import com.example.reto2.modelo.Producto;
import com.example.reto2.modelo.Servicio;
import com.example.reto2.modelo.Tienda;
import com.example.reto2.util.HandlingImages;
import com.example.reto2.util.ProductsRepository;
import com.example.reto2.util.ServiceRepository;
import com.example.reto2.util.TiendaRepository;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiOracle {
    private final String TABLA_PRODUCTOS="PRODUCTS";
    private final String TABLA_SERVICIOS="SERVICES";
    private final String TABLA_TIENDAS="STORES";
    private final RequestQueue requestQueue;
    private HandlingImages handlingImages;
    private final Context context;
    private  Producto producto;
    private  Servicio servicio;
    private  Tienda tienda;
    public ApiOracle(Context context) {
        this.context=context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public  void  insertData(String campo1, String campo2, String camp3, ImageView imagen, String nombreTabla){

        handlingImages = new HandlingImages();
        String image = handlingImages.imaViewToString(imagen);
        JSONObject  jsonObject = new JSONObject();
        try{
            jsonObject.put("nombre", campo1);
            jsonObject.put("descripcion", campo2);
            if (nombreTabla.equals("STORES")){
                jsonObject.put("ubicacion", camp3);
            }else{
                jsonObject.put("valor", camp3);
            }
            jsonObject.put("image",image );
        }catch (Exception ignored){

        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getUrl(nombreTabla),
                jsonObject, response -> {

                }, error -> {

                });
        requestQueue.add(jsonObjectRequest);
    }

    public  void  updateData(String id, String campo1, String campo2, String camp3, ImageView imagen, String nombreTabla){
        handlingImages = new HandlingImages();
        String image = handlingImages.imaViewToString(imagen);
        System.out.println(getUrl(nombreTabla));
        JSONObject  jsonObject = new JSONObject();
        try{
            jsonObject.put("id", id);
            jsonObject.put("nombre", campo1);
            jsonObject.put("descripcion", campo2);
            if (nombreTabla.equals("STORES")){
                jsonObject.put("ubicacion", camp3);
            }else{
                jsonObject.put("valor", camp3);
            }
            jsonObject.put("image",image );
        }catch (Exception ignored){

        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, getUrl(nombreTabla),
                jsonObject, response -> {

                }, error -> {

                });
        requestQueue.add(jsonObjectRequest);
    }

    public void geAllData(String nombreTabla, RecyclerView recyclerView, ProgressBar progressBar){
        handlingImages = new HandlingImages();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUrl(nombreTabla),
                null, response -> {
                    List<Producto> productos = new ArrayList<>();
                    List<Servicio> servicios = new ArrayList<>();
                    List<Tienda> tiendas =new ArrayList<>();
                    try {
                        JSONArray jsonArray = response.getJSONArray("items");
                        for(int indice=0; indice<jsonArray.length(); indice++){
                            JSONObject jsonObject = jsonArray.getJSONObject(indice);

                            switch (nombreTabla) {
                                case TABLA_PRODUCTOS:
                                    Producto producto = new Producto(
                                            jsonObject.getInt("id"),
                                            jsonObject.getString("nombre"),
                                            jsonObject.getString("descripcion"),
                                            jsonObject.getString("valor"),
                                            handlingImages.strigToByte(jsonObject.getString("image"))
                                    );
                                    productos.add(producto);
                                    break;
                                case TABLA_SERVICIOS:
                                    Servicio servicio = new Servicio(
                                            jsonObject.getInt("id"),
                                            jsonObject.getString("nombre"),
                                            jsonObject.getString("descripcion"),
                                            jsonObject.getString("valor"),
                                            handlingImages.strigToByte(jsonObject.getString("image"))
                                    );
                                    servicios.add(servicio);
                                    break;
                                case TABLA_TIENDAS:
                                    Tienda tienda = new Tienda(
                                            jsonObject.getInt("id"),
                                            jsonObject.getString("nombre"),
                                            jsonObject.getString("descripcion"),
                                            jsonObject.getString("ubicacion"),
                                            handlingImages.strigToByte(jsonObject.getString("image"))
                                    );
                                    tiendas.add(tienda);
                                    break;
                            }

                        }
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                        switch (nombreTabla) {
                            case TABLA_PRODUCTOS:
                                adaptador(nombreTabla, recyclerView, productos);
                                break;
                            case TABLA_SERVICIOS:
                                adaptador(nombreTabla, recyclerView, servicios);
                                break;
                            case TABLA_TIENDAS:
                                adaptador(nombreTabla, recyclerView, tiendas);
                                break;
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {


                });
        requestQueue.add(jsonObjectRequest);

    }

    public void deleteDataById(String nombreTabla, String id){

        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.DELETE, getUrl(nombreTabla) + "/" + id,
                null, response -> {

                }, error -> {

        });
        requestQueue.add(jsonRequest);
    }
    public void getDataById(String nombreTabla, String id, EditText campo1, EditText campo2, TextView campo3, ImageView imageView ){

        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.GET, getUrl(nombreTabla) + id,null
                , response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("items");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        byte[]  image =new HandlingImages().strigToByte(jsonObject.getString("image"));

                        if(TABLA_PRODUCTOS.equals(nombreTabla)){
                            ProductsRepository productsRepository = new ProductsRepository();
                            producto =  productsRepository.JsonToProduct(jsonObject, image);
                            campo1.setText(producto.getNombre());
                            campo2.setText(producto.getDescripcion());
                            campo3.setText(producto.getValor());
                            imageView.setImageBitmap(new HandlingImages().imagetoBitmap(producto.getImage()));
                        }else if (TABLA_SERVICIOS.equals(nombreTabla)){
                            ServiceRepository serviceRepository = new ServiceRepository();
                            servicio = serviceRepository.JsonToServicio(jsonObject, image);
                            campo1.setText(servicio.getNombreServicio());
                            campo2.setText(servicio.getDescripcionServicio());
                            campo3.setText(servicio.getValor());
                            imageView.setImageBitmap(new HandlingImages().imagetoBitmap(servicio.getImageService()));
                        }


                    } catch (JSONException e) {
                        Toast toast =Toast.makeText(context, "No Encontrado", Toast.LENGTH_SHORT);
                        View view=toast.getView();
                        TextView  view1=view.findViewById(android.R.id.message);
                        view1.setTextColor(Color.rgb(255, 255, 255));
                        view.setBackgroundResource(R.color.cafe);
                        toast.show();
                    }
                }, error -> System.out.println("esta en el error"));
        requestQueue.add(jsonRequest);


    }


    public void getDataById(String nombreTabla, String id, EditText campo1, EditText campo2, TextView campo3, ImageView imageView, GoogleMap googleMap){

        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.GET, getUrl(nombreTabla) + id,null
                , response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("items");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        byte[]  image =new HandlingImages().strigToByte(jsonObject.getString("image"));
                            TiendaRepository tiendaRepository = new TiendaRepository();
                            tienda = tiendaRepository.JsonToTienda(jsonObject, image);
                            campo1.setText(tienda.getNombreTienda());
                            campo2.setText(tienda.getDescription());
                            campo3.setText(tienda.getUbicacion());
                            imageView.setImageBitmap(new HandlingImages().imagetoBitmap(tienda.getImagenTienda()));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(tienda.locationToCoord());
                        markerOptions.title(tienda.getNombreTienda());
                        googleMap.clear();
                        googleMap.addMarker(markerOptions);



                    } catch (JSONException e) {
                        Toast toast =Toast.makeText(context, "No Encontrado", Toast.LENGTH_SHORT);
                        View view=toast.getView();
                        TextView  view1=view.findViewById(android.R.id.message);
                        view1.setTextColor(Color.rgb(255, 255, 255));
                        view.setBackgroundResource(R.color.cafe);
                        toast.show();
                    }
                }, error -> System.out.println("esta en el error"));
        requestQueue.add(jsonRequest);


    }

    public void adaptador(String nombreTabla, RecyclerView recyclerView, List list ){
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if (TABLA_PRODUCTOS.equals(nombreTabla)){
            ProductosAdapter adapter = new ProductosAdapter(list, context);
            recyclerView.setAdapter(adapter);
        }else if(TABLA_SERVICIOS.equals(nombreTabla)){
            ServiciosAdapter adapter = new ServiciosAdapter(list, context);
            recyclerView.setAdapter(adapter);
        }else if(TABLA_TIENDAS.equals(nombreTabla)){
            TiendaAdapter adapter = new TiendaAdapter(list, context);
            recyclerView.setAdapter(adapter);
        }

    }



    public  String  getUrl(String nombreTabla){
        String url="";
        String URL_PRODUTOS = "https://ge679d6c40046b0-db202112241645.adb.us-sanjose-1.oraclecloudapps.com/ords/admin/productos/productos/";
        String URL_SERVICIOS = "https://ge679d6c40046b0-db202112241645.adb.us-sanjose-1.oraclecloudapps.com/ords/admin/services/servicios/";
        String URL_TIENDAS = "https://ge679d6c40046b0-db202112241645.adb.us-sanjose-1.oraclecloudapps.com/ords/admin/tiendas/tienda/";
        switch (nombreTabla){
            case TABLA_PRODUCTOS: url= URL_PRODUTOS;
                break;
            case TABLA_SERVICIOS: url= URL_SERVICIOS;
                break;
            case TABLA_TIENDAS: url= URL_TIENDAS;
                break;
        }
        return url;
    }


}
