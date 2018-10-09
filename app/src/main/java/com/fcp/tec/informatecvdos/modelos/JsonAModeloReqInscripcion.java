package com.fcp.tec.informatecvdos.modelos;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fcp.tec.informatecvdos.provider.ContractReqInscripcion;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonAModeloReqInscripcion
{

    public static final String TAG = JsonAModeloReqInscripcion.class.getSimpleName();

    //Se declara una variable que contendrá un "mapa" del requisito remoto (aqui se almacena el que acabamos de leer del JSON remoto)
    private HashMap<String, ReqInscripcion> ReqInscripcionRemoto = new HashMap<>();

    private interface ConsultaReqInscripcion
    {
        //Proyeccion para la consulta del ReqInscripcion (Se usará en el método Query)
        String[] PROYECCION =
                {
                        ContractReqInscripcion.ControladorReqInscripcion.IDREQUISITO_INSCRIPCION,
                        ContractReqInscripcion.ControladorReqInscripcion.REQUISITO_INSCRIPCION,
                        ContractReqInscripcion.ControladorReqInscripcion.VERSION,
                        ContractReqInscripcion.ControladorReqInscripcion.MODIFICADO
                };

        //Indices de columnas
        int IDREQUISITO_INSCRIPCION = 0;
        int REQUISITO_INSCRIPCION = 1;
        int VERSION = 2;
        int MODIFICADO = 3;
    }

    //Referencia al conversor JSON
    private Gson gson = new Gson();

    /**
     * permite tomar la respuesta JSON y almacenar sus datos en el objeto ReqInscripcion.java
     */
    public void procesar(JSONArray arrayJson)
    {

        for (ReqInscripcion requisitoActual : gson.fromJson(arrayJson.toString(), ReqInscripcion[].class))
        {
            //Se añade UN id a cada fila del requisito encontrado en ReqInscripcion[]
            ReqInscripcionRemoto.put(requisitoActual.getIdRequisito(), requisitoActual);
        }


    }

    public void procesarOperaciones(ArrayList<ContentProviderOperation> cpOperaciones, ContentResolver cResolver)
    {

        //Revisar la BD SQLite que tiene la app de manera local, los datos ya incluidos
        Cursor c = cResolver.query(ContractReqInscripcion.ControladorReqInscripcion.URI_CONTENIDO, ConsultaReqInscripcion.PROYECCION, ContractReqInscripcion.ControladorReqInscripcion.INSERTADO + "=?", new String[]{"0"}, null);
        //Desde este cursor ya nos estamos refiriendo a datos LOCALES en la BD LOCAL
        if (c != null)
        {
            while (c.moveToNext())
            {
                //Convertir la fila del cursor en OBJETO ResInscripcion.java
                ReqInscripcion filaActual = deCursorARequisito(c);

                //Buscar si la Beca actual se encuentra en el mapa de MAPA BECAS
                ReqInscripcion match = ReqInscripcionRemoto.get(filaActual.getIdRequisito());

                if (match != null)
                {
                    //Esta entrada EXISTE por lo que se remueve del "mapeado"
                    ReqInscripcionRemoto.remove(filaActual.getIdRequisito());

                    //Crear una URI de esta Beca
                    Uri updateUri = ContractReqInscripcion.ControladorReqInscripcion.construirUriReqInscripcion(filaActual.getIdRequisito());

                    //Se aplica la resolución de conflictos, modificaciones de un mismo recurso
                    //tanto en el servidor como en la App. Quien tenga la versión más actual se
                    //toma como preponderante.
                    if (!match.compararReqInscripcion(filaActual))
                    {
                        int flag = match.esMasReciente(filaActual);

                        if (flag > 0)
                        {
                            Log.i(TAG, "Programar actualizacion de Requisito de Inscripcion " + updateUri);
                            //Verificación: ¿Existe conflicto de modificación?
                            if (filaActual.getModificado() == 1)
                            {
                                match.setModificado(0);
                            }
                            cpOperaciones.add(construirOperacionUpdate(match, updateUri));

                        }
                    }

                } else
                {
                    //Se deduce que aquellos elementos que no coincidieron ya no existen en el servidor
                    //por lo que se eliminarán
                    Uri deleteUri = ContractReqInscripcion.ControladorReqInscripcion.construirUriReqInscripcion(filaActual.getIdRequisito());
                    Log.i(TAG, "Programar eliminacion de requisito");
                    cpOperaciones.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c.close();
        }

        //INSERTAR los items resultantes de manera LOCAL ya que se asume que no existen en el objeto
        //ReqInscripcion[]
        for (ReqInscripcion requisito : ReqInscripcionRemoto.values())
        {
            Log.i(TAG, "Programar inserción de NUEVO requisito con ID = " + requisito.getIdRequisito());
            cpOperaciones.add(construirOperacionInsert(requisito));
        }

    }

    //region Operaciones de ContenProvider


    private ContentProviderOperation construirOperacionInsert(ReqInscripcion requisito)
    {
        Log.i(TAG, "construirOperacionInsert: ");

        return ContentProviderOperation.newInsert(ContractReqInscripcion.ControladorReqInscripcion.URI_CONTENIDO)
                .withValue(ContractReqInscripcion.ControladorReqInscripcion.IDREQUISITO_INSCRIPCION, requisito.getIdRequisito())
                .withValue(ContractReqInscripcion.ControladorReqInscripcion.REQUISITO_INSCRIPCION, requisito.getRequisitoInscripcion())
                .withValue(ContractReqInscripcion.ControladorReqInscripcion.VERSION, requisito.getVersion())
                .withValue(ContractReqInscripcion.ControladorReqInscripcion.INSERTADO, 0)
                .build();

    }

    private ContentProviderOperation construirOperacionUpdate(ReqInscripcion match, Uri updateUri)
    {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(ContractReqInscripcion.ControladorReqInscripcion.IDREQUISITO_INSCRIPCION, match.getIdRequisito())
                .withValue(ContractReqInscripcion.ControladorReqInscripcion.REQUISITO_INSCRIPCION, match.getRequisitoInscripcion())
                .withValue(ContractReqInscripcion.ControladorReqInscripcion.VERSION, match.getVersion())
                .withValue(ContractReqInscripcion.ControladorReqInscripcion.MODIFICADO, match.getModificado())
                .build();
    }
    //endregion

    /**
     * convierte lo que encuentra en un cursor hacia un objeto ReqInscripcion.java
     *
     * @param c El cursor que contiene el dato a convertir
     */
    private ReqInscripcion deCursorARequisito(Cursor c)
    {

        return new ReqInscripcion
                (
                        c.getString(ConsultaReqInscripcion.IDREQUISITO_INSCRIPCION),
                        c.getString(ConsultaReqInscripcion.REQUISITO_INSCRIPCION),
                        c.getString(ConsultaReqInscripcion.VERSION),
                        c.getInt(ConsultaReqInscripcion.MODIFICADO)
                );

    }
}
