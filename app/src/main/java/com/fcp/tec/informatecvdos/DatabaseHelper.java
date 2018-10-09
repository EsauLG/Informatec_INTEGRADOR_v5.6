package com.fcp.tec.informatecvdos;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class DatabaseHelper extends SQLiteAssetHelper
{

    private static final String DATABASE_NAME = "iscitsfc_informatec.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public interface Tablas
    {
        String BECAS = "becas";
        String CALENDARIO = "calendario";
        String CARRERAS = "carrera";
        String EXTRAESCOLARES = "extraescolares";
        String MATERIAS = "materias";
        String PREGUNTA = "pregunta";
        String PREGUNTAF = "preguntas_frecuentes";
        String REQUISITOS_ADMISION = "requisitos_exa_admision";
        String REQUISITOS_INSCRIPCION = "requisitos_inscripcion";
        String UNIDAD_ACADEMICA = "unidad_academica";
        String INSTALACIONES = "instalaciones";
        String LOGROS = "logros";
        String TESTIMONIOS = "testimoniales";
    }
}
