package com.fcp.tec.informatecvdos.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fcp.tec.informatecvdos.R;
import com.fcp.tec.informatecvdos.modelos.Instalaciones;

import java.util.ArrayList;

public class AdaptadorInstalaciones extends RecyclerView.Adapter<AdaptadorInstalaciones.ViewHolderInstalaciones>
{

    private static final String TAG = "AdaptadorInstalaciones";
    private ArrayList<Instalaciones> listaInstalaciones;

    public AdaptadorInstalaciones(ArrayList<Instalaciones> listaInstalaciones)
    {
        this.listaInstalaciones = listaInstalaciones;
    }

    @Override
    public AdaptadorInstalaciones.ViewHolderInstalaciones onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_two_item, parent, false);
        ViewHolderInstalaciones vh = new ViewHolderInstalaciones(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(AdaptadorInstalaciones.ViewHolderInstalaciones holder, int position)
    {
        Instalaciones currentItem = listaInstalaciones.get(position);

        holder.mImageView.setImageBitmap(currentItem.getImagenInstalacionDeco());
        holder.mTextView.setText(currentItem.getNombreInstalacion());
        holder.mTextViewDescripcion.setText(currentItem.getDescripcionInstalacion());

        Log.i(TAG, "onBindViewHolder: " + currentItem.getImagenInstalacion());
    }

    @Override
    public int getItemCount()
    {
        return listaInstalaciones.size();
    }

    public class ViewHolderInstalaciones extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView mTextView;
        public TextView mTextViewDescripcion;

        public ViewHolderInstalaciones(View itemView)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.card_image_logros);
            mTextView = itemView.findViewById(R.id.card_title_logros);
            mTextViewDescripcion = itemView.findViewById(R.id.card_text_descripcion_logros);
        }
    }
}
