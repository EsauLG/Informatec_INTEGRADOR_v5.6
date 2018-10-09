package com.fcp.tec.informatecvdos.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fcp.tec.informatecvdos.R;
import com.fcp.tec.informatecvdos.modelos.Logros;

import java.util.ArrayList;

public class AdaptadorLogros extends RecyclerView.Adapter<AdaptadorLogros.ViewHolderLogros>
{
    ArrayList<Logros> logros;

    public AdaptadorLogros(ArrayList<Logros> logros)
    {
        this.logros = logros;
    }

    @Override
    public ViewHolderLogros onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_five_item, parent, false);
        ViewHolderLogros vh = new ViewHolderLogros(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolderLogros holder, int position)
    {
        Logros currentItem = logros.get(position);
        holder.mImageView.setImageBitmap(currentItem.getImagenLogroDeco());
        holder.mTextView.setText(currentItem.getTituloLogro());
        holder.mTextViewDescripcion.setText(currentItem.getDescripcionLogro());
    }

    @Override
    public int getItemCount()
    {
        return logros.size();
    }

    public class ViewHolderLogros extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView mTextView;
        public TextView mTextViewDescripcion;

        public ViewHolderLogros(View itemView)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.card_image_logros);
            mTextView = itemView.findViewById(R.id.card_title_logros);
            mTextViewDescripcion = itemView.findViewById(R.id.card_text_descripcion_logros);
        }
    }
}
