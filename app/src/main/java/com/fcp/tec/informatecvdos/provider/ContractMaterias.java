package com.fcp.tec.informatecvdos.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContractMaterias
{
    public static final String AUTORIDAD = "com.fcp.tec.materias";

    public static final Uri URI_CONTENIDO_BASE = Uri.parse("content://" + AUTORIDAD);

    public static final String RECURSO_MATERIA = "materias";

    interface ColumnasSincronizacion
    {
        String MODIFICADO = "modificado";
        String INSERTADO = "insertado";
    }

    interface ColumnasMaterias
    {
        String IDMATERIAS = "idmaterias";
        String NOMBRE_MATERIA = "nombre_materia";
        String CLAVE_MATERIA = "clave_materia";
        String TIPO_MATERIA = "tipo_materia";
        String VERSION = "version";
    }

    public static class ControladorMaterias implements BaseColumns, ColumnasSincronizacion, ColumnasMaterias
    {
        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(RECURSO_MATERIA).build();

        public final static String MIME_RECURSO = "vnd.android.cursor.item/vnd."+AUTORIDAD+"/"+RECURSO_MATERIA;
        public final static String MIME_COLECCION = "vnd.android.cursor.dir/vnd."+AUTORIDAD+"/"+RECURSO_MATERIA;


        //Métodos de ayuda de generacion de URIS específicas.
        public static Uri construirUriMaterias(String idMaterias)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idMaterias).build();
        }

        public static String obtenerIdMaterias(Uri uri)
        {
            return uri.getLastPathSegment();
        }

    }
}
