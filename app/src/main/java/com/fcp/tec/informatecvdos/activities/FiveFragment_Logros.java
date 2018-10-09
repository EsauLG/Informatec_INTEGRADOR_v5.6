package com.fcp.tec.informatecvdos.activities;

import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fcp.tec.informatecvdos.DatabaseHelper;
import com.fcp.tec.informatecvdos.R;
import com.fcp.tec.informatecvdos.adapters.AdaptadorLogros;
import com.fcp.tec.informatecvdos.modelos.Logros;
import com.fcp.tec.informatecvdos.provider.ContractLogros;
import com.fcp.tec.informatecvdos.utilidades.UBase64;
import com.fcp.tec.informatecvdos.utilidades.USincronizacion;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FiveFragment_Logros extends Fragment
{

    private RecyclerView mrecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLaytouManager;
    private ArrayList<Bitmap> bitmapOrigen;
    private LogrosObserver logrosObserver;
    private static final String TAG = FiveFragment_Logros.class.getSimpleName();


    @Override
    public void onStart()
    {
        super.onStart();
        getActivity().getContentResolver().registerContentObserver(ContractLogros.ControladorLogros.URI_CONTENIDO, true, logrosObserver);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (logrosObserver != null)
        {
            getActivity().getContentResolver().unregisterContentObserver(logrosObserver);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        //Infla el fragment con la vista
        View view = inflater.inflate(R.layout.fragment_five, container, false);

        USincronizacion uSincronizacion = new USincronizacion();
        uSincronizacion.sincronizarAutomaticamente(getContext(), ContractLogros.AUTORIDAD, 1800);

        ArrayList<Logros> list;
        list = getLogros();

        mrecyclerView = view.findViewById(R.id.recyclerViewLogros);
        mrecyclerView.setHasFixedSize(true);
        mLaytouManager = new LinearLayoutManager(view.getContext());
        mAdapter = new AdaptadorLogros(list);

        mrecyclerView.setLayoutManager(mLaytouManager);
        mrecyclerView.setAdapter(mAdapter);

        logrosObserver = new LogrosObserver();

        return view;
    }


    //Obtiene los datos del modelo Logros
    public ArrayList<Logros> getLogros()
    {
        final UBase64 uBase64 = new UBase64();

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        final Cursor regLogros = database.rawQuery("SELECT titulo_logro,imagen_logro,descripcion_logro,nombre_unidad FROM logros INNER JOIN unidad_academica WHERE logros.idunidad_academica = unidad_academica.idunidad_academica", null);

        ArrayList<Logros> list = new ArrayList<Logros>()
        {{
            if (regLogros.moveToFirst())
            {
                do
                {
                    String imagen = regLogros.getString(1);
                    Log.i(TAG, "Valor del String imagen: "+imagen.toString());

                    try
                    {
                        bitmapOrigen = uBase64.base64ToBitmap(imagen);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmapOrigen.get(0).compress(Bitmap.CompressFormat.PNG, 0, stream);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    add(new Logros(regLogros.getString(0), regLogros.getString(2) + "\n(Unidad " + regLogros.getString(3) + ")", bitmapOrigen.get(0)));
                }
                while (regLogros.moveToNext());
            }
        }};
        database.close();
        return list;

    }


    //region Clase observador
    public class LogrosObserver extends ContentObserver
    {
        public LogrosObserver()
        {
            super(new Handler(Looper.getMainLooper()));
        }

        @Override
        public void onChange(boolean selfChange, Uri uri)
        {
            getLogros();
            Log.i(TAG, "Cambios Registrados en Logros!");
        }
    }


    //endregion

}
