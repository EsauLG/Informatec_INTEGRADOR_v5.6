package com.fcp.tec.informatecvdos.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contrato de becas en donde se definen todas las cosas en que se estan de acuerdo que
nunca cambiaran y NO DEBEN cambiar en el transcurso del programa, por esa razón todas
son constantes*/
public class ContractParaBecas
{
    public static final String AUTORIDAD = "com.fcp.tec.informatec";

    /**
     * El valor de la URI Base es: content://com.fcp.tec.informatec
     */
    public static final Uri URI_CONTENIDO_BASE = Uri.parse("content://" + AUTORIDAD);

    public static final String RECURSO_BECAS = "becas";


    interface ColumnasSincronizacion
    {
        String MODIFICADO = "modificado";
        String ELIMINADO = "eliminado";
        String INSERTADO = "insertado";
    }

    interface ColumnasBecas
    {
        String IDBECAS = "idbecas";
        String NOMBRE_BECA = "nombre_beca";
        String DESCRIPCION_BECA = "descripcion_beca";
        String TIPO_BECA = "tipo_beca";
        String IDUNIDAD_ACADEMICA = "idunidad_academica";
        String VERSION = "version";
    }

    //Controlador de la tabla BECAS
    public static class ControladorBecas implements BaseColumns, ColumnasBecas, ColumnasSincronizacion
    {

        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(RECURSO_BECAS).build();


        public final static String MIME_RECURSO = "vnd.android.cursor.item/vnd."+AUTORIDAD+"/"+RECURSO_BECAS;
        public final static String MIME_COLECCION = "vnd.android.cursor.dir/vnd."+AUTORIDAD+"/"+RECURSO_BECAS;

        //Métodos de ayuda de generacion de URIS específicas.
        public static Uri construirUriBeca(String idbeca)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idbeca).build();
        }

        public static String obtenerIdBeca(Uri uri)
        {
            return uri.getLastPathSegment();
        }
    }
}
