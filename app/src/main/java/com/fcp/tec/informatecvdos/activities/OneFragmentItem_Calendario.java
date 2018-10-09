package com.fcp.tec.informatecvdos.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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
import com.fcp.tec.informatecvdos.provider.ContractCalendario;
import com.fcp.tec.informatecvdos.utilidades.USincronizacion;
import com.fcp.tec.informatecvdos.utilidades.UToasts;


public class OneFragmentItem_Calendario extends AppCompatActivity
{
    Button btnActualizaciones;
    TextView txtCalendario;
    String txtTmp = "";
    Cursor calendario;
    private CalendaiosObserver calendaiosObserver;
    private String TAG = OneFragmentItem_Beneficios.class.getSimpleName();
    private static final long SYNC_FREQUENCY = 1800;

    public boolean esTablet(Context context){
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        getContentResolver().registerContentObserver(ContractCalendario.ControladorCalendario.URI_CONTENIDO,true, calendaiosObserver);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(calendaiosObserver!=null)
        {
            getContentResolver().unregisterContentObserver(calendaiosObserver);
        }

    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ini_var_calendario);

        if (esTablet(this))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        //Sincronizacion automatica
        USincronizacion sincronizacion = new USincronizacion();
        sincronizacion.sincronizarAutomaticamente(this, ContractCalendario.AUTORIDAD, 1800);



        //region Codigo original

        btnActualizaciones = (Button) findViewById(R.id.btnActualizacionesCalendario);
        txtCalendario = (TextView) findViewById(R.id.txtFechasCalendario);
        txtCalendario.setMovementMethod(new ScrollingMovementMethod());


        DatabaseHelper databaseHelper = new DatabaseHelper(OneFragmentItem_Calendario.this);
        final SQLiteDatabase database = databaseHelper.getReadableDatabase();

        calendario = database.rawQuery("SELECT * FROM calendario", null);

        if (calendario.moveToFirst())
        {

            do
            {
                txtTmp += "\u2022 " + calendario.getString(1) + "\n" + "\n";
            }

            while (calendario.moveToNext());
            txtCalendario.setText(txtTmp);

        }


        btnActualizaciones.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                UToasts.showToast(getApplicationContext(), "No hay nada para sincronizar");
            }
        });


        //endregion

        calendaiosObserver = new CalendaiosObserver();
    }


    //region Clase observador
    public class CalendaiosObserver extends ContentObserver
    {

        /**
         * Creates a content observer.
         *
         */
        public CalendaiosObserver()
        {
            super(new Handler(Looper.getMainLooper()));
        }

        @Override
        public void onChange(boolean selfChange, Uri uri)
        {
            refreshCalendario();
        }
    }

    private void refreshCalendario()
    {
        Log.i(TAG, "Haciendo nueva Qurery a Calendarios");

        DatabaseHelper databaseHelper = new DatabaseHelper(OneFragmentItem_Calendario.this);
        final SQLiteDatabase database = databaseHelper.getReadableDatabase();

        calendario = database.rawQuery("SELECT * FROM calendario", null);
        String texto = "";

        if (calendario.moveToFirst())
        {

            do
            {
                texto += "\u2022 " + calendario.getString(1) + "\n" + "\n";
            }

            while (calendario.moveToNext());
            txtCalendario.setText(texto);
            calendario.close();

        }

    }
    //endregion

}
