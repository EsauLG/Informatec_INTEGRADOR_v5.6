package com.fcp.tec.informatecvdos.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fcp.tec.informatecvdos.DatabaseHelper;
import com.fcp.tec.informatecvdos.R;
import com.fcp.tec.informatecvdos.provider.ContractCarreras;
import com.fcp.tec.informatecvdos.provider.ContractMaterias;
import com.fcp.tec.informatecvdos.utilidades.USincronizacion;

public class OneFragmentItem_Carreras extends AppCompatActivity
{
    //region declaracionBotones

    ImageButton btnSistemas, btnAdmin, btnGestion, btnIndustrial, btnAlimen;
    Button s1, s2, s3, s4, s5, s6, s7, s8, s9;

    LinearLayout topLevelLayout;

    //endregion

    TextView txtViewMision, txtSemestre, txtSemestreActual, txtMisionActual;
    Cursor cSQL;
    int carreraActual = 0;

    private String TAG = OneFragmentItem_Carreras.class.getSimpleName();


    //region objetos para inicializar BD
    SQLiteDatabase database;
    DatabaseHelper databaseHelper;

    //Strings de almacenamiento temporal del Query
    String temp_txt_Mision = "", temp_txt_Semestre = "";


    public boolean esTablet(Context context){
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ini_var_carreras);

        if (esTablet(this))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        //Sincronizacion automatica de Carreras
        USincronizacion sincronizacion = new USincronizacion();
        sincronizacion.sincronizarAutomaticamente(this, ContractCarreras.AUTORIDAD, 1800);

        //Sincronizacion automatica de Materias
        USincronizacion uSincronizacion = new USincronizacion();
        uSincronizacion.sincronizarAutomaticamente(this, ContractMaterias.AUTORIDAD, 1800);

        //region Inicializado UI y BD

        //Inicializando objetos de la BD
        final DatabaseHelper databaseHelper = new DatabaseHelper(OneFragmentItem_Carreras.this);
        final SQLiteDatabase database = databaseHelper.getReadableDatabase();

        //Incializando TextView Mision
        txtViewMision = (TextView) findViewById(R.id.txtMision);
        txtViewMision.setMovementMethod(new ScrollingMovementMethod());

        //Inicializando TextView Semestres
        txtSemestre = (TextView) findViewById(R.id.txtSemestres);
        txtSemestre.setMovementMethod(new ScrollingMovementMethod());

        //Inicializando TextView Semestre Actual y Mision
        txtSemestreActual = (TextView) findViewById(R.id.txtSemestreActual);
        txtMisionActual = (TextView) findViewById(R.id.txtMisionCarrera);


        //Inicializando botones de Ingenierias.
        btnSistemas = (ImageButton) findViewById(R.id.btnIngSistemas);
        btnIndustrial = (ImageButton) findViewById(R.id.btnIngIndustrial);
        btnAlimen = (ImageButton) findViewById(R.id.btnIngAlimen);
        btnAdmin = (ImageButton) findViewById(R.id.btnIngAdmin);
        btnGestion = (ImageButton) findViewById(R.id.btnIngGestion);

        //Inicializando botones Semestres
        s1 = (Button) findViewById(R.id.btnSemestre1);
        s2 = (Button) findViewById(R.id.btnSemestre2);
        s3 = (Button) findViewById(R.id.btnSemestre3);
        s4 = (Button) findViewById(R.id.btnSemestre4);
        s5 = (Button) findViewById(R.id.btnSemestre5);
        s6 = (Button) findViewById(R.id.btnSemestre6);
        s7 = (Button) findViewById(R.id.btnSemestre7);
        s8 = (Button) findViewById(R.id.btnSemestre8);
        s9 = (Button) findViewById(R.id.btnSemestre9);
        //endregion

        //Iniciarlizar ayuda en pantalla
        topLevelLayout = (LinearLayout) findViewById(R.id.top_layout);


        if (esPrimeraVez())
        {
            topLevelLayout.setVisibility(View.INVISIBLE);
        }

        //Mostrar info de Sistemas al iniciar
        misionCarrera(database, 2);
        semestre(database, 2, 1);
        carreraActual = 2;
        txtSemestreActual.setText(String.valueOf(1));
        txtMisionActual.setText("Ing. En Sistemas Computacionales");


        //region Eventos de Iconos Carreras

        //SISTEMAS COMPUTACIONALES
        btnSistemas.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                temp_txt_Mision = "";
                temp_txt_Semestre = "";

                misionCarrera(database, 2);
                semestre(database, 2, 1);
                carreraActual = 2;
                txtSemestreActual.setText(String.valueOf(1));
                txtMisionActual.setText("Ing. En Sistemas Computacionales");

            }
        });


        //INDUSTRIAL
        btnIndustrial.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                temp_txt_Mision = "";
                temp_txt_Semestre = "";

                misionCarrera(database, 1);
                semestre(database, 1, 1);
                carreraActual = 1;
                txtSemestreActual.setText(String.valueOf(1));
                txtMisionActual.setText("Ing. Industrial");
            }
        });


        //ALIMENTARIAS
        btnAlimen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                temp_txt_Mision = "";
                temp_txt_Semestre = "";

                misionCarrera(database, 4);
                semestre(database, 4, 1);
                carreraActual = 4;
                txtSemestreActual.setText(String.valueOf(1));
                txtMisionActual.setText("Ing. En Industrias Alimentarias");
            }
        });

        //ADMINISTRACION
        btnAdmin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                temp_txt_Mision = "";
                temp_txt_Semestre = "";

                misionCarrera(database, 5);
                semestre(database, 5, 1);
                carreraActual = 5;
                txtSemestreActual.setText(String.valueOf(1));
                txtMisionActual.setText("Ing. En Administración");
            }
        });


        //GESTION
        btnGestion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                temp_txt_Mision = "";
                temp_txt_Semestre = "";

                misionCarrera(database, 3);
                semestre(database, 3, 1);
                carreraActual = 3;
                txtSemestreActual.setText(String.valueOf(1));
                txtMisionActual.setText("Ing. En Gestión Empresarial");

            }
        });
        //endregion


        //region Eventos botones Semestres

        //Semestre1
        s1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                temp_txt_Semestre = "";
                semestre(database, carreraActual, 1);
                txtSemestreActual.setText(String.valueOf(1));


            }
        });


        //Semestre2
        s2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                temp_txt_Semestre = "";
                semestre(database, carreraActual, 2);
                txtSemestreActual.setText(String.valueOf(2));

            }
        });


        //Semestre3
        s3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                temp_txt_Semestre = "";
                semestre(database, carreraActual, 3);
                txtSemestreActual.setText(String.valueOf(3));

            }
        });


        //Semestre4
        s4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                temp_txt_Semestre = "";
                semestre(database, carreraActual, 4);
                txtSemestreActual.setText(String.valueOf(4));

            }
        });


        //Semestre5
        s5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                temp_txt_Semestre = "";
                semestre(database, carreraActual, 5);
                txtSemestreActual.setText(String.valueOf(5));

            }
        });


        //Semestre6
        s6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                temp_txt_Semestre = "";
                semestre(database, carreraActual, 6);
                txtSemestreActual.setText(String.valueOf(6));

            }
        });


        //Semestre7
        s7.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                temp_txt_Semestre = "";
                semestre(database, carreraActual, 7);
                txtSemestreActual.setText(String.valueOf(7));

            }
        });

        //Semestre8
        s8.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                temp_txt_Semestre = "";
                semestre(database, carreraActual, 8);
                txtSemestreActual.setText(String.valueOf(8));

            }
        });


        //Semestre9
        s9.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                temp_txt_Semestre = "";
                semestre(database, carreraActual, 9);
                txtSemestreActual.setText(String.valueOf(9));

            }
        });

        //endregion


    }


    //Seleccion de carrera
    void misionCarrera(SQLiteDatabase database, int idcarrera)
    {

        cSQL = database.rawQuery("select * from carrera where idcarrera = ?", new String[]{
                String.valueOf(idcarrera)});

        if (cSQL.moveToFirst())
        {

            do
            {
                temp_txt_Mision += cSQL.getString(2) + "\n" + "\n";
            }

            while (cSQL.moveToNext());
            txtViewMision.setText(temp_txt_Mision);

        }
    }

    //Seleccion de semestre
    void semestre(SQLiteDatabase database, int idcarrera, int idsemestre)
    {

        cSQL = database.rawQuery("SELECT Nombre_materia FROM Materias " +
                                         "INNER JOIN Carrera_has_Materias " +
                                         "ON Materias.idMaterias = Carrera_has_Materias.idMaterias " +
                                         "WHERE idCarrera = ? AND idSemestre= ?", new String[]{
                String.valueOf(idcarrera), String.valueOf(idsemestre)});

        if (cSQL.moveToFirst())
        {

            do
            {
                temp_txt_Semestre += cSQL.getString(0) + "\n" + "\n";
            }

            while (cSQL.moveToNext());
            txtSemestre.setText(temp_txt_Semestre);

        }


    }

    //Hace que se ejecute solo la primera visualización despues de una instalacion nueva
    private boolean esPrimeraVez()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore)
        {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();

            topLevelLayout.setVisibility(View.VISIBLE);

            topLevelLayout.setOnTouchListener(new View.OnTouchListener()
            {

                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    topLevelLayout.setVisibility(View.INVISIBLE);
                    return false;
                }

            });


        }
        return ranBefore;
    }


}
