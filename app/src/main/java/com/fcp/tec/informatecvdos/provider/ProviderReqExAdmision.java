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

public class ProviderReqExAdmision extends ContentProvider
{


    //Comparador de URIs de contenido
    private static final UriMatcher uriMatcher;

    //Identificadores de tipos
    public static final int REQUISITOS = 100;
    public static final int REQUISITOS_ID = 101;

    //Asignación de URIs (si seleccionamos toda la tabla "requisitos" o un "idRequisito")
    static
    {   //Se construye una variable vacia para asegurarnos de que no estan relacionadas con ninguna direccion especifica
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //Se asignas los IDs correspondientes a cada URI
        uriMatcher.addURI(ContractReqExaAdmision.AUTORIDAD, "reqexaadmision", REQUISITOS);
        uriMatcher.addURI(ContractReqExaAdmision.AUTORIDAD, "reqexaadmision/*", REQUISITOS_ID);
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
            case REQUISITOS:
                c = db.query(DatabaseHelper.Tablas.REQUISITOS_ADMISION, projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver, ContractReqExaAdmision.ControladorReqExaAdmision.URI_CONTENIDO);
                break;
            case REQUISITOS_ID:
                String idRequisito = ContractReqExaAdmision.ControladorReqExaAdmision.obtenerIdRequisito(uri);
                c = db.query(DatabaseHelper.Tablas.REQUISITOS_ADMISION, projection, ContractReqExaAdmision.ControladorReqExaAdmision.IDREQUISITO +
                                     "=" + "\'" + idRequisito + "\'" + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""),
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
            case REQUISITOS:
                return ContractReqExaAdmision.ControladorReqExaAdmision.MIME_COLECCION;
            case REQUISITOS_ID:
                return ContractReqExaAdmision.ControladorReqExaAdmision.MIME_RECURSO;
            default:
                throw new IllegalArgumentException("Tipo de URI desconocida " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        //Primero se valida la URI
        if (uriMatcher.match(uri) != REQUISITOS)
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

        long _id = db.insert(DatabaseHelper.Tablas.REQUISITOS_ADMISION, null, contentValues);

        if (_id > 0)
        {
            resolver.notifyChange(uri, null, false);

            String idRequisito = contentValues.getAsString(ContractReqExaAdmision.ControladorReqExaAdmision.IDREQUISITO);

            return ContractReqExaAdmision.ControladorReqExaAdmision.construirUriReqExaAdmision(idRequisito);
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
            case REQUISITOS:
                filasAfectadas = db.delete(DatabaseHelper.Tablas.REQUISITOS_ADMISION, selection, selectionArgs);
                resolver.notifyChange(uri, null, false);
                break;
            case REQUISITOS_ID:
                String idRequisito = ContractReqExaAdmision.ControladorReqExaAdmision.obtenerIdRequisito(uri);

                filasAfectadas = db.delete(DatabaseHelper.Tablas.REQUISITOS_ADMISION,
                                           ContractReqExaAdmision.ControladorReqExaAdmision.IDREQUISITO
                                                   + "=" + "\'" + idRequisito + "\'"
                                                   + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                resolver.notifyChange(uri, null, false);
                break;
            default:
                throw new IllegalArgumentException("Requisito desconocido " + uri);
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
            case REQUISITOS:
                filasAfectadas = db.update(DatabaseHelper.Tablas.REQUISITOS_ADMISION, values, selection, selectionArgs);
                resolver.notifyChange(uri, null, false);
                break;
            case REQUISITOS_ID:
                String idRequisito = ContractReqExaAdmision.ControladorReqExaAdmision.obtenerIdRequisito(uri);
                filasAfectadas = db.update(DatabaseHelper.Tablas.REQUISITOS_ADMISION, values,
                                           ContractReqExaAdmision.ControladorReqExaAdmision.IDREQUISITO + "=" + "\'" + idRequisito + "\'"
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
