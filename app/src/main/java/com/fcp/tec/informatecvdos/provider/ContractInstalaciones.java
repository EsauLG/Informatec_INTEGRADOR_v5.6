package com.fcp.tec.informatecvdos.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContractInstalaciones
{
    public static final String AUTORIDAD = "com.fcp.tec.instalaciones";

    /**
     * El valor de la URI Base es: content://com.fcp.tec.instalaciones
     */
    public static final Uri URI_CONTENIDO_BASE = Uri.parse("content://" + AUTORIDAD);

    public static final String RECURSO_INSTALACIONES = "instalaciones";

    interface ColumnasSincronizacion
    {
        String MODIFICADO = "modificado";
        String INSERTADO = "insertado";
    }

    interface ColumnasInstalaciones
    {
        String ID_INSTALACIONES = "idinstalaciones";
        String NOMBRE = "nombre_instalacion";
        String IMAGEN = "imagen_instalacion";
        String DESCRIPCION = "descripcion_instalacion";
        String ID_UNIDAD_ACADEMICA = "idunidad_academica";
        String VERSION = "version";
    }

    public static class ControladorInstalaciones implements BaseColumns, ColumnasSincronizacion, ColumnasInstalaciones
    {
        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(RECURSO_INSTALACIONES).build();

        public final static String MIME_RECURSO = "vnd.android.cursor.item/vnd."+AUTORIDAD+"/"+RECURSO_INSTALACIONES;
        public final static String MIME_COLECCION = "vnd.android.cursor.dir/vnd."+AUTORIDAD+"/"+RECURSO_INSTALACIONES;


        //Métodos de ayuda de generacion de URIS específicas.
        public static Uri construirUriInstalaciones(String idInstalacion)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idInstalacion).build();
        }

        public static String obtenerIdInstalacion(Uri uri)
        {
            return uri.getLastPathSegment();
        }

    }

}
