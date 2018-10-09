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
import android.widget.ListView;

import com.fcp.tec.informatecvdos.DatabaseHelper;
import com.fcp.tec.informatecvdos.R;
import com.fcp.tec.informatecvdos.adapters.AdaptadorTestimoniales;
import com.fcp.tec.informatecvdos.modelos.Testimoniales;
import com.fcp.tec.informatecvdos.provider.ContractTestimoniales;
import com.fcp.tec.informatecvdos.utilidades.UBase64;
import com.fcp.tec.informatecvdos.utilidades.USincronizacion;
import com.pkmmte.view.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FourFragment_Testimoniales extends Fragment
{

    private RecyclerView mrecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLaytouManager;
    private ArrayList<Bitmap> bitmapOrigen;
    private ArrayList<Testimoniales> listaTestimoniales;
    private TestimoniosObserver testimoniosObserver;

    private static final String TAG = FourFragment_Testimoniales.class.getSimpleName();


    @Override
    public void onStart()
    {
        super.onStart();
        getActivity().getContentResolver().registerContentObserver(ContractTestimoniales.ControladorTestimoniales.URI_CONTENIDO, true, testimoniosObserver);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (testimoniosObserver != null)
        {
            getActivity().getContentResolver().unregisterContentObserver(testimoniosObserver);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        //Infla el fragment con la vista
        View view = inflater.inflate(R.layout.fragment_four, container, false);

        USincronizacion uSincronizacion = new USincronizacion();
        uSincronizacion.sincronizarAutomaticamente(getContext(), ContractTestimoniales.AUTORIDAD, 1800);


        ArrayList<Testimoniales> listaTestimoniales;

        listaTestimoniales = getTestimonios();

        mrecyclerView = view.findViewById(R.id.recyclerViewTestimonios);
        mrecyclerView.setHasFixedSize(true);
        mLaytouManager = new LinearLayoutManager(view.getContext());
        mAdapter = new AdaptadorTestimoniales(listaTestimoniales);

        mrecyclerView.setLayoutManager(mLaytouManager);
        mrecyclerView.setAdapter(mAdapter);

        testimoniosObserver = new TestimoniosObserver();
        return view;
    }


    //Obtiene los datos del modelo Testimonios
    public ArrayList<Testimoniales> getTestimonios()
    {
        final UBase64 uBase64 = new UBase64();

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        final Cursor testimonio = database.rawQuery("SELECT nombre_persona, testimonial, img_persona,nombre_carrera FROM testimoniales INNER JOIN carrera WHERE testimoniales.idcarrera = carrera.idcarrera", null);

        ArrayList<Testimoniales> testimoniales = new ArrayList<Testimoniales>()
        {{
            if (testimonio.moveToFirst())
            {

                do
                {
                    try
                    {
                        String imagen = testimonio.getString(2);
                        bitmapOrigen = uBase64.base64ToBitmap(imagen);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmapOrigen.get(0).compress(Bitmap.CompressFormat.JPEG, 0, stream);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    add(new Testimoniales(testimonio.getString(0), testimonio.getString(3), testimonio.getString(1),bitmapOrigen.get(0)));

                }
                while (testimonio.moveToNext());
            }
        }};
        database.close();
        return testimoniales;

    }


    //region Clase observador
    public class TestimoniosObserver extends ContentObserver
    {
        public TestimoniosObserver()
        {
            super(new Handler(Looper.getMainLooper()));
        }

        @Override
        public void onChange(boolean selfChange, Uri uri)
        {
            getTestimonios();
            Log.i(TAG, "Cambios Registrados en Testimonios!");
        }
    }


    //endregion
}
