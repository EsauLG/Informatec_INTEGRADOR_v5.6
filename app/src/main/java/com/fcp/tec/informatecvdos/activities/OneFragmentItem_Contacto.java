package com.fcp.tec.informatecvdos.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fcp.tec.informatecvdos.DatabaseHelper;
import com.fcp.tec.informatecvdos.R;
import com.fcp.tec.informatecvdos.provider.ContractUnidadAcademica;
import com.fcp.tec.informatecvdos.utilidades.USincronizacion;

public class OneFragmentItem_Contacto extends AppCompatActivity
{
    TextView txtContactoFcp, txtContactoTul, txtContactoTih, txtContactoChun;
    Button btnLlamarFCP, btnLlamarTul, btnLlamarTih;


    Cursor cSQL;

    public boolean esTablet(Context context){
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ini_var_contacto);

        //definimos orientación dependiendo del dispositivo
        if (esTablet(this))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        USincronizacion uSincronizacion = new USincronizacion();
        uSincronizacion.sincronizarAutomaticamente(this, ContractUnidadAcademica.AUTORIDAD, 1800);


        txtContactoFcp = (TextView) findViewById(R.id.txtContactoFcp);
        txtContactoTul = (TextView) findViewById(R.id.txtContactoTul);
        txtContactoTih = (TextView) findViewById(R.id.txtContactoTih);
        txtContactoChun = (TextView) findViewById(R.id.txtContactoChun);


        btnLlamarFCP = (Button) findViewById(R.id.btnLlamarFCP);
        btnLlamarTul = (Button) findViewById(R.id.btnLlamarTul);
        btnLlamarTih = (Button) findViewById(R.id.btnLlamarTih);

        TextView TextViewFCP;
        TextViewFCP = (TextView) findViewById(R.id.txtContactoFcp);

        TextView TextViewTul;
        TextViewTul = (TextView) findViewById(R.id.txtContactoTul);


        TextView TextViewTih;
        TextViewTih = (TextView) findViewById(R.id.txtContactoTih);


        TextView TextViewChun;
        TextViewChun = (TextView) findViewById(R.id.txtContactoChun);


        DatabaseHelper databaseHelper = new DatabaseHelper(OneFragmentItem_Contacto.this);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor txtContactoFcp = database.rawQuery("SELECT nombre_unidad, direccion_unidad, telefono_unidad, correo_unidad FROM unidad_academica WHERE idunidad_academica = 1", null);

        if (txtContactoFcp.moveToNext())
        {

            TextViewFCP.setText("Dirección: " + txtContactoFcp.getString(1) + "\n" + "Teléfonos: " + txtContactoFcp.getString(2) + "\n" + "E-mail: " + txtContactoFcp.getString(3) + "\n");
            TextViewFCP.setTextIsSelectable(true);
        }


        Cursor txtContactoTul = database.rawQuery("SELECT nombre_unidad, direccion_unidad, telefono_unidad, correo_unidad FROM unidad_academica WHERE idunidad_academica = 2", null);
        if (txtContactoTul.moveToNext())
        {

            TextViewTul.setText("Dirección: " + txtContactoTul.getString(1) + "\n" + "Teléfonos: " + txtContactoTul.getString(2) + "\n" + "E-mail: " + txtContactoTul.getString(3) + "\n");
            TextViewTul.setTextIsSelectable(true);
        }


        Cursor txtContactoTih = database.rawQuery("SELECT nombre_unidad, direccion_unidad, telefono_unidad, correo_unidad FROM unidad_academica WHERE idunidad_academica = 3", null);
        if (txtContactoTih.moveToNext())
        {

            TextViewTih.setText("Dirección: " + txtContactoTih.getString(1) + "\n" + "Teléfonos: " + txtContactoTih.getString(2) + "\n" + "E-mail: " + txtContactoTih.getString(3) + "\n");
            TextViewTih.setTextIsSelectable(true);
        }


        Cursor txtContactoChun = database.rawQuery("SELECT nombre_unidad, direccion_unidad, telefono_unidad, correo_unidad FROM unidad_academica WHERE idunidad_academica = 4", null);
        if (txtContactoChun.moveToNext())
        {

            TextViewChun.setText("Dirección: " + txtContactoChun.getString(1) + "\n" + "Teléfonos: " + txtContactoChun.getString(2) + "\n" + "E-mail: " + txtContactoChun.getString(3) + "\n");
            TextViewChun.setTextIsSelectable(true);
        }


        btnLlamarFCP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LlamarUnidad("9838340051");
            }
        });

        btnLlamarTul.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LlamarUnidad("9831093956");
            }
        });

        btnLlamarTih.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LlamarUnidad("9831067684");
            }
        });

    }

    void LlamarUnidad(String telefono)
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+" + telefono));
        startActivity(callIntent);
    }


    void datosUnidad(SQLiteDatabase database, int idUnidad)
    {

        cSQL = database.rawQuery("SELECT nombre_unidad, direccion_unidad, telefono_unidad, correo_unidad FROM unidad_academica WHERE idunidad_academica = ?", new String[]{String.valueOf(idUnidad)});


    }
}
