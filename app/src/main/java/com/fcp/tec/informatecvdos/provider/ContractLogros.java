package com.fcp.tec.informatecvdos.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContractLogros
{
    public static final String AUTORIDAD = "com.fcp.tec.logros";

    /**
     * El valor de la URI Base es: content://com.fcp.tec.logros
     */
    public static final Uri URI_CONTENIDO_BASE = Uri.parse("content://" + AUTORIDAD);

    public static final String RECURSO_LOGROS = "logros";

    interface ColumnasSincronizacion
    {
        String MODIFICADO = "modificado";
        String INSERTADO = "insertado";
    }

    interface ColumnasLogros
    {
        String ID_LOGROS = "idlogros";
        String TITULO = "titulo_logro";
        String IMAGEN = "imagen_logro";
        String DESCRIPCION = "descripcion_logro";
        String ID_UNIDAD_ACADEMICA = "idunidad_academica";
        String VERSION = "version";
    }

    public static class ControladorLogros implements BaseColumns, ColumnasSincronizacion, ColumnasLogros
    {
        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(RECURSO_LOGROS).build();

        public final static String MIME_RECURSO = "vnd.android.cursor.item/vnd."+AUTORIDAD+"/"+RECURSO_LOGROS;
        public final static String MIME_COLECCION = "vnd.android.cursor.dir/vnd."+AUTORIDAD+"/"+RECURSO_LOGROS;


        //Métodos de ayuda de generacion de URIS específicas.
        public static Uri construirUriLogros(String idLogro)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idLogro).build();
        }

        public static String obtenerIdLogro(Uri uri)
        {
            return uri.getLastPathSegment();
        }

    }
}
