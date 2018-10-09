package com.fcp.tec.informatecvdos.activities;

import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fcp.tec.informatecvdos.DatabaseHelper;
import com.fcp.tec.informatecvdos.R;
import com.fcp.tec.informatecvdos.provider.ContractParaBecas;
import com.fcp.tec.informatecvdos.utilidades.USincronizacion;

public class OneFragmentItem_Beneficios extends AppCompatActivity
{

    //Variables globales
    //region Variables Originales

    Button btnBeca1, btnBeca2;
    Cursor cBeca1, cBeca2;
    TextView card_text;
    private BecasObserver becasObserver; //clase observer

//inicio
    @Override
    protected void onStop()
    {
        super.onStop();
        if (becasObserver != null)
        {
            getContentResolver().unregisterContentObserver(becasObserver);
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        getContentResolver().registerContentObserver(ContractParaBecas.ControladorBecas.URI_CONTENIDO, true, becasObserver);
    }

    private String TAG = OneFragmentItem_Beneficios.class.getSimpleName();

    //onStar

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ini_var_beneficios);

        //Sincronización automática
        USincronizacion sincronizacion = new USincronizacion();
        sincronizacion.sincronizarAutomaticamente(this, ContractParaBecas.AUTORIDAD, 1800);

        //region Codigo Original

        card_text = findViewById(R.id.card_text_descripcion_logros);
        card_text.setMovementMethod(new ScrollingMovementMethod());

        btnBeca1 = findViewById(R.id.btnBeca1);
        btnBeca2 = findViewById(R.id.btnBeca2);

        DatabaseHelper helper = new DatabaseHelper(this);
        final SQLiteDatabase database = helper.getReadableDatabase();


        cBeca1 = database.rawQuery("select * from becas where tipo_beca = 1", null);
        String texto = "";

        if (cBeca1.moveToFirst())
        {

            do
            {
                texto += "\u2022 " + cBeca1.getString(1) + "\n" + "\n";
            }

            while (cBeca1.moveToNext());
            card_text.setText(texto);
            btnBeca1.setBackgroundResource(R.drawable.toggle_button_left_on);
            cBeca1.close();
        }


        btnBeca1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cBeca1 = database.rawQuery("select * from becas where tipo_beca = 1", null);
                String texto = "";


                if (cBeca1.moveToFirst())
                {

                    do
                    {
                        texto += "\u2022 " + cBeca1.getString(1) + "\n" + "\n";
                    }

                    while (cBeca1.moveToNext());
                    card_text.setText(texto);
                    btnBeca1.setBackgroundResource(R.drawable.toggle_button_left_on);
                    btnBeca2.setBackgroundResource(R.drawable.toggle_button_right);
                    cBeca1.close();
                }
            }
        });


        btnBeca2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cBeca2 = database.rawQuery("select * from becas where tipo_beca = 2", null);
                String texto = "";

                if (cBeca2.moveToFirst())
                {

                    do
                    {
                        texto += "\u2022 " + cBeca2.getString(1) + "\n" + "\n";
                    }

                    while (cBeca2.moveToNext());
                    card_text.setText(texto);

                    btnBeca1.setBackgroundResource(R.drawable.toggle_button_left);
                    btnBeca2.setBackgroundResource(R.drawable.toggle_button_right_on);
                    cBeca2.close();
                }
            }
        });

        //endregion



        becasObserver = new BecasObserver();
    }

    //region Clase observador
    public class BecasObserver extends ContentObserver
    {

        /**
         * Creates a content observer.
         *
         */
        public BecasObserver()
        {
            super(new Handler(Looper.getMainLooper()));
        }

        @Override
        public void onChange(boolean selfChange, Uri uri)
        {
            refreshBecas();
        }
    }

    private void refreshBecas()
    {
        Log.i(TAG, "Haciendo nueva Query de Becas 1");

        DatabaseHelper helper = new DatabaseHelper(this);
        final SQLiteDatabase database = helper.getReadableDatabase();


        cBeca1 = database.rawQuery("select * from becas where tipo_beca = 1", null);
        String texto = "";

        if (cBeca1.moveToFirst())
        {

            do
            {
                texto += "\u2022 " + cBeca1.getString(1) + "\n" + "\n";
            }

            while (cBeca1.moveToNext());
            card_text.setText(texto);
            btnBeca1.setBackgroundResource(R.drawable.toggle_button_left_on);
            btnBeca2.setBackgroundResource(R.drawable.toggle_button_right);
            cBeca1.close();
        }


    }
    //endregion





}
