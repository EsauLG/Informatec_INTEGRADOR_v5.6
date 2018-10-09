package com.fcp.tec.informatecvdos.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContractCarreras
{
    public static final String AUTORIDAD = "com.fcp.tec.carreras";
    /**
     * El valor de la URI Base es: content://com.fcp.tec.carreras
     */
    public static final Uri URI_CONTENIDO_BASE = Uri.parse("content://" + AUTORIDAD);

    public static final String RECURSO_CARRERAS = "carreras";

    interface ColumnasSincronizacion
    {
        String MODIFICADO = "modificado";
        String INSERTADO = "insertado";
    }

    interface ColumnasCarreras
    {
        String IDCARRERA = "idcarrera";
        String NOMBRE_CARRERA = "nombre_carrera";
        String OBJETIVO_CARRERA = "objetivo_carrera";
        String VIDEO_CARRERA = "video_carrera";
        String PLAN_ESTUDIO = "plan_estudio";
        String ESPECIALIDAD = "especialidad_carrera";
        String VERSION = "version";
    }

    public static class ControladorCarreras implements BaseColumns, ContractCarreras.ColumnasSincronizacion, ContractCarreras.ColumnasCarreras
    {
        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(RECURSO_CARRERAS).build();

        public final static String MIME_RECURSO = "vnd.android.cursor.item/vnd."+AUTORIDAD+"/"+RECURSO_CARRERAS;
        public final static String MIME_COLECCION = "vnd.android.cursor.dir/vnd."+AUTORIDAD+"/"+RECURSO_CARRERAS;


        //Métodos de ayuda de generacion de URIS específicas.
        public static Uri construirUriCarrera(String idCarrera)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idCarrera).build();
        }

        public static String obtenerIdCarrera(Uri uri)
        {
            return uri.getLastPathSegment();
        }

    }
}
