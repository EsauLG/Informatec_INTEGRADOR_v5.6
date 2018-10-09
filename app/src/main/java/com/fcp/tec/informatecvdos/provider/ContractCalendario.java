package com.fcp.tec.informatecvdos.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContractCalendario
{
    public static final String AUTORIDAD = "com.fcp.tec.calendario";

    /**
     * El valor de la URI Base es: content://com.fcp.tec.calendario
     */
    public static final Uri URI_CONTENIDO_BASE = Uri.parse("content://" + AUTORIDAD);

    public static final String RECURSO_CALENDARIO = "calendario";

    interface ColumnasSincronizacion
    {
        String MODIFICADO = "modificado";
        String INSERTADO = "insertado";
    }

    interface ColumnasCalendario
    {
        String IDEVENTO = "idevento";
        String CONTENIDO_FECHA = "contenido_fecha";
        String VERSION = "version";
    }

    public static class ControladorCalendario implements BaseColumns, ColumnasSincronizacion, ColumnasCalendario
    {
        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(RECURSO_CALENDARIO).build();

        public final static String MIME_RECURSO = "vnd.android.cursor.item/vnd."+AUTORIDAD+"/"+RECURSO_CALENDARIO;
        public final static String MIME_COLECCION = "vnd.android.cursor.dir/vnd."+AUTORIDAD+"/"+RECURSO_CALENDARIO;


        //Métodos de ayuda de generacion de URIS específicas.
        public static Uri construirUriCalendario(String idevento)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idevento).build();
        }

        public static String obtenerIdEvento(Uri uri)
        {
            return uri.getLastPathSegment();
        }

    }

}
