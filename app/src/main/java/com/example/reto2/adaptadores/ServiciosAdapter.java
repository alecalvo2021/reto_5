package com.example.reto2.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto2.R;
import com.example.reto2.modelo.Servicio;
import com.example.reto2.util.HandlingImages;

import java.util.List;

public class ServiciosAdapter extends RecyclerView.Adapter<ServiciosAdapter.ViewHolder> {
    private final List<Servicio> listaServicios;
    private final LayoutInflater inflater;


    public ServiciosAdapter(List<Servicio> servicios, Context context){
        this.inflater=LayoutInflater.from(context);
        this.listaServicios=servicios;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.list_servicio, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ServiciosAdapter.ViewHolder holder, final int position) {
        holder.bindData(listaServicios.get(position));
    }

    @Override
    public int getItemCount() {
        return listaServicios.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView nombreServicio, descripcionServicio, valor, idServicio;
        ViewHolder(View view){
            super(view);
            iconImage=view.findViewById(R.id.imgServicio);
            nombreServicio = view.findViewById(R.id.txtNombreServicio);
            descripcionServicio= view.findViewById(R.id.txtDescripcionServicio);
            valor=view.findViewById(R.id.txtValorServicio);
            idServicio = view.findViewById(R.id.txtidServicio);

        }

        void bindData(final Servicio servicios){
            nombreServicio.setText(servicios.getNombreServicio());
            descripcionServicio.setText(servicios.getDescripcionServicio());
            iconImage.setImageBitmap(new HandlingImages().imagetoBitmap(servicios.getImageService()));
            valor.setText(servicios.getValor());
            idServicio.setText(servicios.getId()+"");
        }


    }
}
