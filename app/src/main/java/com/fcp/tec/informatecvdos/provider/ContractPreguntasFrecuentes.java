package com.fcp.tec.informatecvdos.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContractPreguntasFrecuentes
{
    public static final String AUTORIDAD = "com.fcp.tec.preguntasfrecuentes";

    public static final Uri URI_CONTENIDO_BASE = Uri.parse("content://" + AUTORIDAD);

    public static final String RECURSO_PREGUNTA = "preguntas_frecuentes";

    interface ColumnasSincronizacion
    {
        String MODIFICADO = "modificado";
        String INSERTADO = "insertado";
    }

    interface ColumnasPreguntas
    {
        String IDPREGUNTA = "idpregunta";
        String TITULO_PREGUNTA = "titulo_pregunta";
        String CONTENIDO_PREGUNTA = "contenido_pregunta";
        String VERSION = "version";
    }

    public static class ControladorPreguntasFrecuentes implements BaseColumns, ColumnasSincronizacion, ColumnasPreguntas
    {
        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(RECURSO_PREGUNTA).build();

        public final static String MIME_RECURSO = "vnd.android.cursor.item/vnd."+AUTORIDAD+"/"+RECURSO_PREGUNTA;
        public final static String MIME_COLECCION = "vnd.android.cursor.dir/vnd."+AUTORIDAD+"/"+RECURSO_PREGUNTA;


        //Métodos de ayuda de generacion de URIS específicas.
        public static Uri construirUriPreguntasFrecuentes(String idPregunta)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idPregunta).build();
        }

        public static String obtenerIdPregunta(Uri uri)
        {
            return uri.getLastPathSegment();
        }

    }
}
