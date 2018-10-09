package com.fcp.tec.informatecvdos.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContractExtraescolar
{
    public static final String AUTORIDAD = "com.fcp.tec.extraescolares";

    public static final Uri URI_CONTENIDO_BASE = Uri.parse("content://"+AUTORIDAD);

    public static final String RECURSO_EXTRAESCOLARES = "extraescolar";

    interface ColumnasSincronizacion
    {
        String MODIFICADO = "modificado";
        String INSERTADO = "insertado";
    }

    interface ColumnasExtraescolares
    {
        String IDEXTRAESCOLARES = "idextraescolares";
        String NOMBRE_EXTRA = "nombre_extra";
        String TIPO_EXTRA = "tipo_extra";
        String OPCION_SELECCION = "opcion_seleccion";
        String IMAGEN_EXTRAESCOLAR = "imagen_extraescolar";
        String VERSION = "version";
    }

    //Controlador de la tabla Extraescolares
    public static class ControladorExtraescolares implements BaseColumns, ColumnasSincronizacion, ColumnasExtraescolares
    {
        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(RECURSO_EXTRAESCOLARES).build();
        //content://com.fcp.tec.extraescolares/extraescolares

        public final static String MIME_RECURSO = "vnd.android.cursor.item/vnd."+AUTORIDAD+"/"+RECURSO_EXTRAESCOLARES;
        public final static String MIME_COLECCION = "vnd.android.cursor.dir/vnd."+AUTORIDAD+"/"+RECURSO_EXTRAESCOLARES;


        //Métodos de ayuda de generacion de URIS específicas.
        public static Uri construirUriExtraescolar(String idExtraescolar)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idExtraescolar).build();
            //content://com.fcp.tec.extraescolares/extraescolares + idExtraescolar
        }

        public static String obtenerIdExtraescolar(Uri uri)
        {
            return uri.getLastPathSegment();
        }
    }
}
