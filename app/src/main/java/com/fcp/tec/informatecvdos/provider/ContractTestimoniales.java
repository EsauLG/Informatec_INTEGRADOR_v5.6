package com.fcp.tec.informatecvdos.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContractTestimoniales
{

    public static final String AUTORIDAD = "com.fcp.tec.testimoniales";

    /**
     * El valor de la URI Base es: content://com.fcp.tec.testimoniales
     */
    public static final Uri URI_CONTENIDO_BASE = Uri.parse("content://" + AUTORIDAD);

    public static final String RECURSO_TESTIMONIALES = "testimoniales";

    interface ColumnasSincronizacion
    {
        String MODIFICADO = "modificado";
        String INSERTADO = "insertado";
    }

    interface ColumnasTestimoniales
    {
        String ID_TESTIMONIALES = "idtestimoniales";
        String NOMBRE_PERSONA = "nombre_persona";
        String TESTIMONIAL = "testimonial";
        String IMAGEN = "img_persona";
        String ID_CARRERA = "idcarrera";
        String VERSION = "version";
    }

    public static class ControladorTestimoniales implements BaseColumns, ColumnasSincronizacion, ColumnasTestimoniales
    {
        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(RECURSO_TESTIMONIALES).build();

        public final static String MIME_RECURSO = "vnd.android.cursor.item/vnd."+AUTORIDAD+"/"+RECURSO_TESTIMONIALES;
        public final static String MIME_COLECCION = "vnd.android.cursor.dir/vnd."+AUTORIDAD+"/"+RECURSO_TESTIMONIALES;


        //Métodos de ayuda de generacion de URIS específicas.
        public static Uri construirUriTestimoniales(String idTestimonial)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idTestimonial).build();
        }

        public static String obtenerIdTestimonial(Uri uri)
        {
            return uri.getLastPathSegment();
        }

    }
}
