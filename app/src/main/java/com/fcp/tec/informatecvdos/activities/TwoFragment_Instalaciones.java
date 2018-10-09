package com.fcp.tec.informatecvdos.activities;


import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fcp.tec.informatecvdos.DatabaseHelper;
import com.fcp.tec.informatecvdos.R;
import com.fcp.tec.informatecvdos.adapters.AdaptadorInstalaciones;
import com.fcp.tec.informatecvdos.modelos.Instalaciones;
import com.fcp.tec.informatecvdos.provider.ContractInstalaciones;
import com.fcp.tec.informatecvdos.utilidades.UBase64;
import com.fcp.tec.informatecvdos.utilidades.USincronizacion;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment_Instalaciones extends Fragment
{

    private RecyclerView mrecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLaytouManager;
    private ArrayList<Bitmap> bitmapOrigen;

    private InstalacionesObserver instalacionesObserver;

    private String TAG = TwoFragment_Instalaciones.class.getSimpleName();

    @Override
    public void onStop()
    {
        super.onStop();
        if (instalacionesObserver != null)
        {
            getActivity().getContentResolver().unregisterContentObserver(instalacionesObserver);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        getActivity().getContentResolver().registerContentObserver(ContractInstalaciones.ControladorInstalaciones.URI_CONTENIDO, true, instalacionesObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_two, container, false);

        USincronizacion uSincronizacion = new USincronizacion();
        uSincronizacion.sincronizarAutomaticamente(getContext(), ContractInstalaciones.AUTORIDAD, 1800);

        ArrayList<Instalaciones> list;

        list = inicializaDatos();



        mrecyclerView = view.findViewById(R.id.recyclerViewInstalaciones);
        mrecyclerView.setHasFixedSize(true);
        mLaytouManager = new LinearLayoutManager(view.getContext());
        mAdapter = new AdaptadorInstalaciones(list);


        mrecyclerView.setLayoutManager(mLaytouManager);
        mrecyclerView.setAdapter(mAdapter);

        instalacionesObserver = new InstalacionesObserver();
        return view;
    }

    /**
     * Inicializa las imagenes que van a ir incluidas en el <b>recyclerview</b>
     * recibiendo como entrada un array de Bitmaps y devolviendo un Array de
     * objetos de tipo <i>Instalaciones</i>
     */
    public ArrayList<Instalaciones> inicializaDatos()
    {
        final UBase64 uBase64 = new UBase64();

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        final Cursor instalacion = database.rawQuery("SELECT nombre_instalacion,imagen_instalacion,descripcion_instalacion,nombre_unidad FROM instalaciones INNER JOIN unidad_academica WHERE instalaciones.idunidad_academica = unidad_academica.idunidad_academica", null);

        ArrayList<Instalaciones> instalaciones = new ArrayList<Instalaciones>()
        {{

            if (instalacion.moveToFirst())
            {
                do
                {

                    try
                    {
                        String imagen = instalacion.getString(1);
                        bitmapOrigen = uBase64.base64ToBitmap(imagen);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmapOrigen.get(0).compress(Bitmap.CompressFormat.JPEG, 0, stream);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                    add(new Instalaciones(instalacion.getString(0), instalacion.getString(2) + "\n(Unidad " + instalacion.getString(3) + ")", bitmapOrigen.get(0)));

                }
                while (instalacion.moveToNext());
            }
        }};

        database.close();
        return instalaciones;
    }

    //region Clase observador
    public class InstalacionesObserver extends ContentObserver
    {
        public InstalacionesObserver()
        {
            super(new Handler(Looper.getMainLooper()));
        }

        @Override
        public void onChange(boolean selfChange, Uri uri)
        {
            inicializaDatos();
        }
    }


    //endregion


}

