package com.fcp.tec.informatecvdos.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContractUnidadAcademica
{
    public static final String AUTORIDAD = "com.fcp.tec.unidadacademica";
    /**
     * El valor de la URI Base es: content://com.fcp.tec.unidadacademica
     */
    public static final Uri URI_CONTENIDO_BASE = Uri.parse("content://" + AUTORIDAD);

    public static final String RECURSO_UNIDAD = "unidadacademica";

    interface ColumnasSincronizacion
    {
        String MODIFICADO = "modificado";
        String INSERTADO = "insertado";
    }

    interface ColumnasUnidadAcademica
    {
        String IDUNIDAD_ACADEMICA = "idunidad_academica";
        String NOMBRE_UNIDAD = "nombre_unidad";
        String DIRECCION_UNIDAD = "direccion_unidad";
        String CORREO_UNIDAD = "correo_unidad";
        String TEL_UNIDAD = "telefono_unidad";
        String HORARIO_UNIDAD = "horarios_unidad";
        String VIDEOGENERAL = "video_general";
        String IDINSCRIPCION = "idinscripcion";
        String VERSION = "version";
    }

    public static class ControladorUnidadAcademica implements BaseColumns, ContractUnidadAcademica.ColumnasSincronizacion, ContractUnidadAcademica.ColumnasUnidadAcademica
    {
        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(RECURSO_UNIDAD).build();

        public final static String MIME_RECURSO = "vnd.android.cursor.item/vnd."+AUTORIDAD+"/"+RECURSO_UNIDAD;
        public final static String MIME_COLECCION = "vnd.android.cursor.dir/vnd."+AUTORIDAD+"/"+RECURSO_UNIDAD;


        //Métodos de ayuda de generacion de URIS específicas.
        public static Uri construirUriUnidadAcademica(String idUnidad)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idUnidad).build();
        }

        public static String obtenerIdUnidad(Uri uri)
        {
            return uri.getLastPathSegment();
        }

    }
}
