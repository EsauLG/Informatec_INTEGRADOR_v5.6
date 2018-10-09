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

public class ProviderLogros extends ContentProvider
{

    //Comparador de URIs de contenido
    private static final UriMatcher uriMatcher;

    //Identificadores de tipos
    public static final int LOGROS = 100;
    public static final int ID_LOGROS = 101;


    static
    {   //Se construye una variable vacia para asegurarnos de que no estan relacionadas con ninguna direccion especifica
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //Se asignas los IDs correspondientes a cada URI
        uriMatcher.addURI(ContractLogros.AUTORIDAD, "logros", LOGROS);
        uriMatcher.addURI(ContractLogros.AUTORIDAD, "logros/*", ID_LOGROS);
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

            case LOGROS:
                c = db.query(DatabaseHelper.Tablas.LOGROS, projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver, ContractLogros.ControladorLogros.URI_CONTENIDO);
                break;
            case ID_LOGROS:
                
                String idLogro = ContractLogros.ControladorLogros.obtenerIdLogro(uri);
                c = db.query(DatabaseHelper.Tablas.LOGROS, projection, ContractLogros.ControladorLogros.ID_LOGROS +
                                     "=" + "\'" + idLogro + "\'" + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""),
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
            case LOGROS:
                return ContractLogros.ControladorLogros.MIME_COLECCION;
            case ID_LOGROS:
                return ContractLogros.ControladorLogros.MIME_RECURSO;
            default:
                throw new IllegalArgumentException("Tipo de URI desconocida " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        //Primero se valida la URI
        if (uriMatcher.match(uri) != LOGROS)
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

        long _id = db.insert(DatabaseHelper.Tablas.LOGROS, null, contentValues);

        if (_id > 0)
        {
            resolver.notifyChange(uri, null, false);

            String idLogro = contentValues.getAsString(ContractLogros.ControladorLogros.ID_LOGROS);

            return ContractLogros.ControladorLogros.construirUriLogros(idLogro);
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
            case LOGROS:
                filasAfectadas = db.delete(DatabaseHelper.Tablas.LOGROS, selection, selectionArgs);
                resolver.notifyChange(uri, null, false);
                break;
            case ID_LOGROS:
                String idLogro = ContractLogros.ControladorLogros.obtenerIdLogro(uri);

                filasAfectadas = db.delete(DatabaseHelper.Tablas.LOGROS,
                                           ContractLogros.ControladorLogros.ID_LOGROS
                                                   + "=" + "\'" + idLogro + "\'"
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
            case LOGROS:
                filasAfectadas = db.update(DatabaseHelper.Tablas.LOGROS, values, selection, selectionArgs);
                resolver.notifyChange(uri, null, false);
                break;
            case ID_LOGROS:
                String idInstalaciones = ContractLogros.ControladorLogros.obtenerIdLogro(uri);
                filasAfectadas = db.update(DatabaseHelper.Tablas.LOGROS, values,
                                           ContractLogros.ControladorLogros.ID_LOGROS + "=" + "\'" + idInstalaciones + "\'"
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
