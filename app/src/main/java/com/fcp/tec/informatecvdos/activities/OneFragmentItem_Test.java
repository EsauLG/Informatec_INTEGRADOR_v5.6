package com.fcp.tec.informatecvdos.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fcp.tec.informatecvdos.DatabaseHelper;
import com.fcp.tec.informatecvdos.R;
import com.fcp.tec.informatecvdos.provider.ContractPreguntasTest;
import com.fcp.tec.informatecvdos.utilidades.USincronizacion;


public class OneFragmentItem_Test extends AppCompatActivity
{
    private TextView numPreg, pregunta, msgResultado, instResultado;
    private Button boton_si, boton_no;
    private DatabaseHelper databaseHelper;
    int contador = 1, sumArea1 = 0, sumArea2 = 0, sumArea3 = 0, sumArea4 = 0, sumArea5 = 0;
    Cursor resultado, resultadoMayor;
    final Context context = this;

    public boolean esTablet(Context context){
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }

    @Override
    public void onBackPressed()
    {
        if (contador > 80)
        {
            super.onBackPressed(); //Solo cierra por defecto
            restablecerBD();
        }
        else
        {
            cancelarTest();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_test);
        USincronizacion uSincronizacion = new USincronizacion();
        uSincronizacion.sincronizarAutomaticamente(this, ContractPreguntasTest.AUTORIDAD, 1);

        if (esTablet(this))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        //Botones de respuesta
        boton_si = (Button) findViewById(R.id.button_test_si);
        boton_no = (Button) findViewById(R.id.button_test_no);

        //Cajas de text
        msgResultado = (TextView) findViewById(R.id.card_text_descripcion_logros);
        instResultado = (TextView) findViewById(R.id.textView1);
        msgResultado.setText("Este es un test vocacional que te orienta a elegir una carrera, " +
                "para mayor precisión: \n"+
                "\n► Contesta con la mayor honestidad posible " +
                "\n► Selecciona la respuesta que mas se adapte a tu persona");

        numPreg = (TextView) findViewById(R.id.textView2);
        pregunta = (TextView) findViewById(R.id.textview_test_pregunta);
        pregunta.setMovementMethod(new ScrollingMovementMethod());

        //base de datos
        databaseHelper = new DatabaseHelper(OneFragmentItem_Test.this);


        boton_si.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                contador++;
                if (contador <= 80)
                {
                    cargarRegs(Integer.toString(contador));
                }
                else
                {
                    //mostrarResultados();
                    mostrarResultados();
                    Toast toast = Toast.makeText(getApplicationContext(), "Finalizaste el examen", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        boton_no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                contador++;
                if (contador <= 80)
                {
                    cargarPreg(Integer.toString(contador));
                }
                else
                {
                    mostrarResultados();
                    Toast toast = Toast.makeText(getApplicationContext(), "Finalizaste el examen", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        try
        {
            restablecerBD();
            cargarPreg(Integer.toString(contador));
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Error al cargar: " + e, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    public void cargarRegs(String valor)
    {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String[] args = new String[]{valor};
        Cursor reg = database.rawQuery("SELECT pregunta,nombre_area,fk_id_area FROM area,pregunta WHERE id_area = fk_id_area AND id_pregunta=?", args);
        Cursor total = database.rawQuery("SELECT * FROM pregunta", null);


        if (reg.moveToFirst())
        {
            // Adding child data
            do
            {
                //sumamos num. de pregs por cada area
                try
                {
                    numPreg.setText("");
                    numPreg.setText("Pregunta " + contador + " de " + total.getCount());
                    pregunta.setText("");
                    pregunta.setText(reg.getString(0));
                    seleccionarIDPreg(reg.getInt(2));

                }
                catch (Exception e)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error id: " + e.getMessage(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
            while (reg.moveToNext());
        }
        database.close();
        reg.close();
        total.close(); //Cerrar Cursores
    }

    public void cargarPreg(String valor)
    {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String[] args = new String[]{valor};
        Cursor reg = database.rawQuery("SELECT pregunta,nombre_area,fk_id_area FROM area,pregunta WHERE id_area = fk_id_area AND id_pregunta=?", args);
        Cursor total = database.rawQuery("SELECT * FROM pregunta", null);


        if (reg.moveToFirst())
        {
            // Adding child data
            do
            {
                //sumamos num. de pregs por cada area
                try
                {
                    numPreg.setText("");
                    numPreg.setText("Pregunta " + contador + " de " + total.getCount());
                    pregunta.setText("");
                    pregunta.setText(reg.getString(0));

                }
                catch (Exception e)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error id: " + e.getMessage(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
            while (reg.moveToNext());
        }
        database.close();
        reg.close();
        total.close(); //Cerrar Cursores
    }

    public void seleccionarIDPreg(int idArea)
    {
        switch (idArea)
        {
            case 1:
                sumArea1++;
                if (contador <= 80)
                {
                    actualizarResultados(sumArea1, idArea);
                }
                break;
            case 2:
                sumArea2++;
                if (contador <= 80)
                {
                    actualizarResultados(sumArea2, idArea);
                }
                break;
            case 3:
                sumArea3++;
                if (contador <= 80)
                {
                    actualizarResultados(sumArea3, idArea);
                }
                break;
            case 4:
                sumArea4++;
                if (contador <= 80)
                {
                    actualizarResultados(sumArea4, idArea);
                }
                break;
            case 5:
                sumArea5++;
                if (contador <= 80)
                {
                    actualizarResultados(sumArea5, idArea);
                }
                break;
            default:
                break;
        }

    }

    public void actualizarResultados(int suma, int idArea)
    {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        database.execSQL("UPDATE area SET total=" + suma + " WHERE id_area=" + idArea);
    }

    public void mostrarResultados()
    {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        resultado = database.rawQuery("SELECT * FROM area", null);

        if (resultado.moveToFirst())
        {
            // Resultado
            pregunta.setText("");
            do
            {
                //numPreg.setText("");
                //numPreg.setText("Resultados Obtenidos");
                //pregunta.append("Area: " + resultado.getString(1) + "\n------------Resultado Total: " + resultado.getString(2) + "\n\n");
                boton_si.setVisibility(View.INVISIBLE);
                boton_no.setVisibility(View.INVISIBLE);
            }
            while (resultado.moveToNext());
        }
        database.close();
        resultado.close();
        mostrarRecomendacion();
    }

    private void mostrarRecomendacion()
    {
        try
        {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Cursor resultadoMayor = database.rawQuery("SELECT id_area,max(total) FROM area", null);
            if (resultadoMayor.moveToFirst())
            {
                String ide = resultadoMayor.getString(0);
                //Intent intent = new Intent(context, OneFragmentItem_Resultados.class);
                //intent.putExtra("idArea", ide);
                //startActivity(intent);
                mostrarResultadoFinal(ide, resultadoMayor.getInt(1));
            }
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Error Recomendacion: " + e.getMessage(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void mostrarResultadoFinal(String ide, int mayor)
    {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String[] args = new String[]{ide};
        Cursor resultado = database.rawQuery("SELECT nombre_area,nombre_carrera " +
                "FROM carrera,area_carrera,area " +
                "WHERE id_area=fk_id_area AND idcarrera=id_carrera AND id_area=?;", args);
        if (resultado.moveToFirst())
        {
            // Resultado
            instResultado.setText("");
            instResultado.setText("Resultados");
            msgResultado.setText("");
            msgResultado.setText("De acuerdo a tus respuestas el Instituto Tecnologico de Felipe Carrillo " +
                    "Puerto te ofrece las carreras en: \n");
            pregunta.setText("");
            numPreg.setText("");

            double porcentaje = 0;
            porcentaje = (mayor * 100) / 80;


            //Condicion para validar el resultado mayor
            if (mayor == 0)
            {
                pregunta.setText("Seleccionaste 'NO' en todas las preguntas, lo que implica que no haya resultados.");
            }
            else
            {
                numPreg.setText(porcentaje + "%  " + resultado.getString(0));
                boton_si.setVisibility(View.INVISIBLE);
                boton_no.setVisibility(View.INVISIBLE);
                do
                {
                    //res.setText("Resultados Obtenidos");
                    pregunta.append("►  " + resultado.getString(1) + "\n\n");
                }
                while (resultado.moveToNext());
            }
        }
    }

    private void cancelarTest()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title
        alertDialogBuilder.setTitle("¿Abandonar la prueba?");

        // set dialog message
        alertDialogBuilder
                .setMessage("Tu progreso actual no se guardará")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // if this button is clicked, close
                        // current activity
                        OneFragmentItem_Test.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void restablecerBD()
    {
        for (int i = 1; i <= 5; i++)
        {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            database.execSQL("UPDATE area SET total=0 WHERE id_area=" + i);
        }

    }
}
