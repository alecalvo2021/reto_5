package com.example.reto2.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto2.R;
import com.example.reto2.ShowMapsActivity;
import com.example.reto2.modelo.Tienda;
import com.example.reto2.util.HandlingImages;

import java.util.List;

public class TiendaAdapter extends RecyclerView.Adapter<TiendaAdapter.ViewHolder>{
    private final List<Tienda> listaTiendas;
    private final LayoutInflater inflater;
    private View.OnClickListener onClickListener;
    private Context context;


    public TiendaAdapter(List<Tienda> tiendas, Context context){
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        System.out.println("estmo en el constructor");
        this.listaTiendas=tiendas;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.list_tienda, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TiendaAdapter.ViewHolder holder, final int position) {
        holder.bindData(listaTiendas.get(position));

    }

    @Override
    public int getItemCount() {
        return listaTiendas.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView nombreTienda,ubicacionTienda, descriptionTienda, idTienda;
        ViewHolder(View view){
            super(view);
            iconImage=view.findViewById(R.id.imgtienda);
            nombreTienda = view.findViewById(R.id.txtNombreTienda);
            ubicacionTienda= view.findViewById(R.id.txtUbicacionTienda);
            descriptionTienda= view.findViewById(R.id.txtDescriptionStore);
            idTienda= view.findViewById(R.id.txtidTienda);

        }

        void bindData(final  Tienda tienda){
            nombreTienda.setText(tienda.getNombreTienda());
            ubicacionTienda.setText(tienda.getUbicacion());
            descriptionTienda.setText(tienda.getDescription());
            iconImage.setImageBitmap(new HandlingImages().imagetoBitmap(tienda.getImagenTienda()));
            idTienda.setText(tienda.getId()+"");
            iconImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ShowMapsActivity.class);
                    intent.putExtra("name", tienda.getNombreTienda());
                    intent.putExtra("location", tienda.getUbicacion());
                    view.getContext().startActivity(intent);

                }
            });

        }


    }
}
