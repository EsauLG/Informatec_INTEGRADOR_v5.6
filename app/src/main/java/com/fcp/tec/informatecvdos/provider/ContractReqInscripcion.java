package com.fcp.tec.informatecvdos.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContractReqInscripcion
{

    public static final String AUTORIDAD = "com.fcp.tec.reqinscripcion";
    public static final Uri URI_CONTENIDO_BASE = Uri.parse("content://" + AUTORIDAD);

    public static final String RECURSO_INSCRIPCION = "reqinscripcion";

    interface ColumnasSincronizacion
    {
        String MODIFICADO = "modificado";
        String INSERTADO = "insertado";
    }

    interface ColumnasReqInscripcion
    {
        String IDREQUISITO_INSCRIPCION = "idrequisitos_inscripcion";
        String REQUISITO_INSCRIPCION = "requisitos_inscripcion";
        String VERSION = "version";
    }

    public static class ControladorReqInscripcion implements BaseColumns, ColumnasSincronizacion, ColumnasReqInscripcion
    {
        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(RECURSO_INSCRIPCION).build();

        public final static String MIME_RECURSO = "vnd.android.cursor.item/vnd." + AUTORIDAD + "/" + RECURSO_INSCRIPCION;
        public final static String MIME_COLECCION = "vnd.android.cursor.dir/vnd." + AUTORIDAD + "/" + RECURSO_INSCRIPCION;


        //Métodos de ayuda de generacion de URIS específicas.
        public static Uri construirUriReqInscripcion(String idReqInscripcion)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idReqInscripcion).build();
        }

        public static String obtenerIdReqInscripcion(Uri uri)
        {
            return uri.getLastPathSegment();
        }

    }
}
