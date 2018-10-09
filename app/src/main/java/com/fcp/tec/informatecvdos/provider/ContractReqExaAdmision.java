package com.fcp.tec.informatecvdos.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContractReqExaAdmision
{
    public static final String AUTORIDAD = "com.fcp.tec.reqexaadmision";
    public static final Uri URI_CONTENIDO_BASE = Uri.parse("content://"+AUTORIDAD);

    public static final String RECURSO_EXAADMISION = "reqexaadmision";

    interface ColumnasSincronizacion
    {
        String MODIFICADO = "modificado";
        String INSERTADO = "insertado";
    }

    interface ColumnasReqExaAdmision
    {
        String IDREQUISITO = "idrequisitos_obtener_ficha";
        String REQUISITO_FICHA = "requisito_ficha";
        String ID_INSCRIPCION = "idinscripcion";
        String VERSION = "version";
    }

    public static class ControladorReqExaAdmision implements BaseColumns, ColumnasSincronizacion, ColumnasReqExaAdmision
    {
        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(RECURSO_EXAADMISION).build();

        public final static String MIME_RECURSO = "vnd.android.cursor.item/vnd."+AUTORIDAD+"/"+RECURSO_EXAADMISION;
        public final static String MIME_COLECCION = "vnd.android.cursor.dir/vnd."+AUTORIDAD+"/"+RECURSO_EXAADMISION;


        //Métodos de ayuda de generacion de URIS específicas.
        public static Uri construirUriReqExaAdmision(String idRequisito)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idRequisito).build();
        }

        public static String obtenerIdRequisito(Uri uri)
        {
            return uri.getLastPathSegment();
        }

    }


}
