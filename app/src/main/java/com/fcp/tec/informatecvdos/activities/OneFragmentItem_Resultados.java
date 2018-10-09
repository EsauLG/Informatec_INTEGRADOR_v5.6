package com.fcp.tec.informatecvdos.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.fcp.tec.informatecvdos.DatabaseHelper;
import com.fcp.tec.informatecvdos.R;

public class OneFragmentItem_Resultados extends AppCompatActivity
{

    private DatabaseHelper databaseHelper;
    private TextView txtresultados;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ini_var_resultadostest);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Identificar casillas
        txtresultados = (TextView) findViewById(R.id.card_text_descripcion_logros);

        //base de datos
        databaseHelper = new DatabaseHelper(OneFragmentItem_Resultados.this);
        //
        try
        {
            //Recibe los datos
            Intent intent = getIntent();
            String id = intent.getStringExtra("idArea");
            cargarResultados(id);

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Error al mostrar Resultados: " + e, Toast.LENGTH_SHORT).show();
        }

    }//Fin OnCreate

    private void cargarResultados(String IdArea)
    {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String[] args = new String[]{IdArea};
        Cursor resultado = database.rawQuery("SELECT nombre_area,nombre_carrera " +
                                                     "FROM carrera,area_carrera,area " +
                                                     "WHERE id_area=fk_id_area AND idcarrera=id_carrera AND id_area=?;", args);
        if (resultado.moveToFirst())
        {
            // Resultado
            txtresultados.setText("");
            do
            {

                txtresultados.append("Area: " + resultado.getString(0) + "\n ------------Carreras Afines: " + resultado.getString(1) + "\n\n");
            }
            while (resultado.moveToNext());
        }
    }
}
