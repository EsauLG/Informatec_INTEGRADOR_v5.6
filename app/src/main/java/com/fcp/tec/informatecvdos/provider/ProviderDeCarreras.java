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

public class ProviderDeCarreras extends ContentProvider
{

    //Comparador de URIs de contenido
    private static final UriMatcher uriMatcher;

    //Identificadores de tipos
    public static final int CARRERAS = 100;
    public static final int CARRERAS_ID = 101;

    //Asignación de URIs (si seleccionamos toda la tabla "carrera" o un "idcarreras)
    static
    {   //Se construye una variable vacia para asegurarnos de que no estan relacionadas con ninguna direccion especifica
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //Se asignas los IDs correspondientes a cada URI
        uriMatcher.addURI(ContractCarreras.AUTORIDAD, "carreras", CARRERAS);
        uriMatcher.addURI(ContractCarreras.AUTORIDAD, "carreras/*", CARRERAS_ID);
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
        return false;
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
            case CARRERAS:
                c = db.query(DatabaseHelper.Tablas.CARRERAS, projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver, ContractCarreras.ControladorCarreras.URI_CONTENIDO);
                break;
            case CARRERAS_ID:
                String idCarrera = ContractCarreras.ControladorCarreras.obtenerIdCarrera(uri);
                c = db.query(DatabaseHelper.Tablas.CARRERAS, projection, ContractCarreras.ControladorCarreras.IDCARRERA +
                                     "=" + "\'" + idCarrera + "\'" + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""),
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
            case CARRERAS:
                return ContractCarreras.ControladorCarreras.MIME_COLECCION;
            case CARRERAS_ID:
                return ContractCarreras.ControladorCarreras.MIME_RECURSO;
            default:
                throw new IllegalArgumentException("Tipo de URI desconocida " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        //Primero se valida la URI
        if (uriMatcher.match(uri) != CARRERAS)
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

        long _id = db.insert(DatabaseHelper.Tablas.CARRERAS, null, contentValues);

        if (_id > 0)
        {
            resolver.notifyChange(uri, null, false);

            String idCarrera = contentValues.getAsString(ContractCarreras.ControladorCarreras.IDCARRERA);

            return ContractCarreras.ControladorCarreras.construirUriCarrera(idCarrera);
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
            case CARRERAS:
                filasAfectadas = db.delete(DatabaseHelper.Tablas.CARRERAS, selection, selectionArgs);
                resolver.notifyChange(uri, null, false);
                break;
            case CARRERAS_ID:
                String idCarrera = ContractCarreras.ControladorCarreras.obtenerIdCarrera(uri);

                filasAfectadas = db.delete(DatabaseHelper.Tablas.CARRERAS,
                                           ContractCarreras.ControladorCarreras.IDCARRERA
                                                   + "=" + "\'" + idCarrera + "\'"
                                                   + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                resolver.notifyChange(uri, null, false);
                break;
            default:
                throw new IllegalArgumentException("Carrera desconocida " + uri);
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
            case CARRERAS:
                filasAfectadas = db.update(DatabaseHelper.Tablas.CARRERAS, values, selection, selectionArgs);
                resolver.notifyChange(uri, null, false);
                break;
            case CARRERAS_ID:
                String idCarreras = ContractCarreras.ControladorCarreras.obtenerIdCarrera(uri);
                filasAfectadas = db.update(DatabaseHelper.Tablas.CARRERAS, values,
                                           ContractCarreras.ControladorCarreras.IDCARRERA + "=" + "\'" + idCarreras + "\'"
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
