package com.fcp.tec.informatecvdos.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.fcp.tec.informatecvdos.DatabaseHelper;

public class ProviderPreguntasTest extends ContentProvider
{
    //Comparador de URIs de contenido
    private static final UriMatcher uriMatcher;

    //Identificadores de tipos
    public static final int PREGUNTA = 100;
    public static final int PREGUNTA_ID = 101;

    //Asignación de URIs (si seleccionamos toda la tabla "preguntastest" o un "idPregunta")
    static
    {   //Se construye una variable vacia para asegurarnos de que no estan relacionadas con ninguna direccion especifica
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //Se asignas los IDs correspondientes a cada URI
        uriMatcher.addURI(ContractPreguntasTest.AUTORIDAD, "pregunta", PREGUNTA);
        uriMatcher.addURI(ContractPreguntasTest.AUTORIDAD, "pregunta/*", PREGUNTA_ID);
    }


    //Objetos de referencia
    private ContentResolver resolver;
    private DatabaseHelper databaseHelper;


    //region @Overrides query(), insert(), delete(), update(), getType(), onCreate()

    @Override
    public boolean onCreate()
    {
        databaseHelper = new DatabaseHelper(getContext());
        resolver = getContext().getContentResolver();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();


        int match = uriMatcher.match(uri);

        Cursor c;

        switch (match)
        {
            case PREGUNTA:
                c = db.query(DatabaseHelper.Tablas.PREGUNTA, projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver, ContractPreguntasTest.ControladorPreguntasTest.URI_CONTENIDO);
                break;
            case PREGUNTA_ID:
                String idPregunta = ContractPreguntasTest.ControladorPreguntasTest.obtenerIdPregunta(uri);
                c = db.query(DatabaseHelper.Tablas.PREGUNTA, projection, ContractPreguntasTest.ControladorPreguntasTest.ID_PREGUNTA +
                                     "=" + "\'" + idPregunta + "\'" + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""),
                             selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver, uri);
                break;
            default:
                throw new IllegalArgumentException("URI no soportada " + uri);

        }

        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    {
        switch (uriMatcher.match(uri))
        {
            case PREGUNTA:
                return ContractPreguntasTest.ControladorPreguntasTest.MIME_COLECCION;
            case PREGUNTA_ID:
                return ContractPreguntasTest.ControladorPreguntasTest.MIME_RECURSO;
            default:
                throw new IllegalArgumentException("Tipo de URI desconocida " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        //Primero se valida la URI
        if (uriMatcher.match(uri) != PREGUNTA)
        {
            throw new IllegalArgumentException("URI desconocida " + uri);
        }

        ContentValues contentValues;
        if (values != null)
        {
            contentValues = new ContentValues(values);
        } else
        {
            contentValues = new ContentValues();
        }

        //Inserción de NUEVA fila
        //Se AVISA a la base de datos que vamos a insertar nuevos datos con el método getWritableDatabase
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        long _id = db.insert(DatabaseHelper.Tablas.PREGUNTA, null, contentValues);

        if (_id > 0)
        {
            resolver.notifyChange(uri, null, false);

            String idPregunta = contentValues.getAsString(ContractPreguntasTest.ControladorPreguntasTest.ID_PREGUNTA);

            return ContractPreguntasTest.ControladorPreguntasTest.construirUriPreguntas(idPregunta);
        }
        throw new SQLException("Falla al insertar la fila en: " + uri);


    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int filasAfectadas;

        switch (match)
        {
            case PREGUNTA:
                filasAfectadas = db.delete(DatabaseHelper.Tablas.PREGUNTA, selection, selectionArgs);
                resolver.notifyChange(uri, null, false);
                break;
            case PREGUNTA_ID:
                String idPregunta = ContractPreguntasTest.ControladorPreguntasTest.obtenerIdPregunta(uri);

                filasAfectadas = db.delete(DatabaseHelper.Tablas.PREGUNTA,
                                           ContractPreguntasTest.ControladorPreguntasTest.ID_PREGUNTA
                                                   + "=" + "\'" + idPregunta + "\'"
                                                   + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                resolver.notifyChange(uri, null, false);
                break;
            default:
                throw new IllegalArgumentException("Calendario desconocido " + uri);
        }


        return filasAfectadas;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int filasAfectadas;

        switch (uriMatcher.match(uri))
        {
            case PREGUNTA:
                filasAfectadas = db.update(DatabaseHelper.Tablas.PREGUNTA, values, selection, selectionArgs);
                resolver.notifyChange(uri, null, false);
                break;
            case PREGUNTA_ID:
                String idPregunta = ContractPreguntasTest.ControladorPreguntasTest.obtenerIdPregunta(uri);
                filasAfectadas = db.update(DatabaseHelper.Tablas.PREGUNTA, values,
                                           ContractPreguntasTest.ControladorPreguntasTest.ID_PREGUNTA + "=" + "\'" + idPregunta + "\'"
                                                   + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                                           selectionArgs);
                resolver.notifyChange(uri, null, false);
                break;
            default:
                throw new IllegalArgumentException("URI desconocida " + uri);
        }

        return filasAfectadas;
    }

    //endregion
}
