package com.fcp.tec.informatecvdos.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fcp.tec.informatecvdos.R;
import com.fcp.tec.informatecvdos.modelos.Testimoniales;

import java.util.ArrayList;

public class AdaptadorTestimoniales extends RecyclerView.Adapter<AdaptadorTestimoniales.ViewHolderTestimonios>
{
    ArrayList<Testimoniales> testimoniales;

    public AdaptadorTestimoniales(ArrayList<Testimoniales> testimoniales)
    {
        this.testimoniales = testimoniales;
    }

    @Override
    public ViewHolderTestimonios onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_four_item, parent, false);
        ViewHolderTestimonios vh = new ViewHolderTestimonios(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolderTestimonios holder, int position)
    {
        Testimoniales currentItem = testimoniales.get(position);
        holder.mImageViewImagen.setImageBitmap(currentItem.getImagenTestimonioDeco());
        holder.mTextViewIngenieria.setText(currentItem.getIdCarrera());
        holder.mTextViewDescripcion.setText(currentItem.getTestimonial());
        holder.mTextViewNombre.setText(currentItem.getNombrePersona());
    }

    @Override
    public int getItemCount()
    {
        return testimoniales.size();
    }

    public class ViewHolderTestimonios extends RecyclerView.ViewHolder
    {
        public ImageView mImageViewImagen;
        public TextView mTextViewIngenieria;
        public TextView mTextViewNombre;
        public TextView mTextViewDescripcion;

        public ViewHolderTestimonios(View itemView)
        {
            super(itemView);
            mImageViewImagen = itemView.findViewById(R.id.imagenTestimonio);
            mTextViewIngenieria = itemView.findViewById(R.id.carreraEgr);
            mTextViewNombre = itemView.findViewById(R.id.nombreEgr);
            mTextViewDescripcion = itemView.findViewById(R.id.desTest);
        }
    }
}
