package com.fcp.tec.informatecvdos.modelos;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fcp.tec.informatecvdos.provider.ContractReqExaAdmision;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonAModeloReqExaAdmision
{
    public static final String TAG = JsonAModeloReqExaAdmision.class.getSimpleName();

    //Se declara una variable que contendrá un "mapa" del calendario remoto (aqui se almacena el que acabamos de leer del JSON remoto)
    private HashMap<String, ReqExAdmision> ReqExAdmisionRemoto = new HashMap<>();

    private interface ConsultaReqExAdmision
    {
        //Proyeccion para la consulta del ReqExAdmision (Se usará en el método Query)
        String[] PROYECCION =
                {
                        ContractReqExaAdmision.ControladorReqExaAdmision.IDREQUISITO,
                        ContractReqExaAdmision.ControladorReqExaAdmision.REQUISITO_FICHA,
                        ContractReqExaAdmision.ControladorReqExaAdmision.ID_INSCRIPCION,
                        ContractReqExaAdmision.ControladorReqExaAdmision.VERSION,
                        ContractReqExaAdmision.ControladorReqExaAdmision.MODIFICADO
                };

        //Indices de columnas
        int IDREQUISITO = 0;
        int REQUISITO_FICHA = 1;
        int ID_INSCRIPCION = 2;
        int VERSION = 3;
        int MODIFICADO = 4;
    }

    //Referencia al conversor JSON
    private Gson gson = new Gson();

    /**
     * permite tomar la respuesta JSON y almacenar sus datos en el objeto ReqExAdmision.java
     */
    public void procesar(JSONArray arrayJson)
    {

        for (ReqExAdmision requisitoActual : gson.fromJson(arrayJson.toString(), ReqExAdmision[].class))
        {
            //Se añade UN id a cada fila del calendario encontrado en ReqExAdmision[]
            ReqExAdmisionRemoto.put(requisitoActual.getIdInscripcion(), requisitoActual);
        }


    }

    public void procesarOperaciones(ArrayList<ContentProviderOperation> cpOperaciones, ContentResolver cResolver)
    {

        //Revisar la BD SQLite que tiene la app de manera local, los datos ya incluidos
        Cursor c = cResolver.query(ContractReqExaAdmision.ControladorReqExaAdmision.URI_CONTENIDO, ConsultaReqExAdmision.PROYECCION, ContractReqExaAdmision.ControladorReqExaAdmision.INSERTADO + "=?", new String[]{"0"}, null);
        //Desde este cursor ya nos estamos refiriendo a datos LOCALES en la BD LOCAL
        if (c != null)
        {
            while (c.moveToNext())
            {
                //Convertir la fila del cursor en OBJETO Becas.java
                ReqExAdmision filaActual = deCursorARequisito(c);

                //Buscar si la Beca actual se encuentra en el mapa de MAPA BECAS
                ReqExAdmision match = ReqExAdmisionRemoto.get(filaActual.getIdInscripcion());

                if (match != null)
                {
                    //Esta entrada EXISTE por lo que se remueve del "mapeado"
                    ReqExAdmisionRemoto.remove(filaActual.getIdInscripcion());

                    //Crear una URI de esta Beca
                    Uri updateUri = ContractReqExaAdmision.ControladorReqExaAdmision.construirUriReqExaAdmision(filaActual.getIdInscripcion());

                    //Se aplica la resolución de conflictos, modificaciones de un mismo recurso
                    //tanto en el servidor como en la App. Quien tenga la versión más actual se
                    //toma como preponderante.
                    if (!match.compararExAdmisionCon(filaActual))
                    {
                        int flag = match.esMasReciente(filaActual);

                        if (flag > 0)
                        {
                            Log.i(TAG, "Programar actualizacion de Requisito " + updateUri);
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
                    Uri deleteUri = ContractReqExaAdmision.ControladorReqExaAdmision.construirUriReqExaAdmision(filaActual.getIdInscripcion());
                    Log.i(TAG, "Programar eliminacion de requisito");
                    cpOperaciones.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c.close();
        }

        //INSERTAR los items resultantes de manera LOCAL ya que se asume que no existen en el objeto
        //ReqExAdmision[]
        for (ReqExAdmision requisito : ReqExAdmisionRemoto.values())
        {
            Log.i(TAG, "Programar inserción de NUEVA requisito con ID = " + requisito.getIdInscripcion());
            cpOperaciones.add(construirOperacionInsert(requisito));
        }

    }

    //region Operaciones de ContenProvider


    private ContentProviderOperation construirOperacionInsert(ReqExAdmision requisito)
    {
        Log.i(TAG, "construirOperacionInsert: ");

        return ContentProviderOperation.newInsert(ContractReqExaAdmision.ControladorReqExaAdmision.URI_CONTENIDO)
                .withValue(ContractReqExaAdmision.ControladorReqExaAdmision.IDREQUISITO, requisito.getIdInscripcion())
                .withValue(ContractReqExaAdmision.ControladorReqExaAdmision.REQUISITO_FICHA, requisito.getRequisitoFicha())
                .withValue(ContractReqExaAdmision.ControladorReqExaAdmision.ID_INSCRIPCION, requisito.getIdInscripcion())
                .withValue(ContractReqExaAdmision.ControladorReqExaAdmision.VERSION, requisito.getVersion())
                .withValue(ContractReqExaAdmision.ControladorReqExaAdmision.INSERTADO, 0)
                .build();

    }

    private ContentProviderOperation construirOperacionUpdate(ReqExAdmision match, Uri updateUri)
    {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(ContractReqExaAdmision.ControladorReqExaAdmision.IDREQUISITO, match.getIdInscripcion())
                .withValue(ContractReqExaAdmision.ControladorReqExaAdmision.REQUISITO_FICHA, match.getRequisitoFicha())
                .withValue(ContractReqExaAdmision.ControladorReqExaAdmision.ID_INSCRIPCION, match.getIdInscripcion())
                .withValue(ContractReqExaAdmision.ControladorReqExaAdmision.VERSION, match.getVersion())
                .withValue(ContractReqExaAdmision.ControladorReqExaAdmision.MODIFICADO, match.getModificado())
                .build();
    }
    //endregion

    /**
     * convierte lo que encuentra en un cursor hacia un objeto ReqExAdmision.java
     *
     * @param c El cursor que contiene el dato a convertir
     */
    private ReqExAdmision deCursorARequisito(Cursor c)
    {

        return new ReqExAdmision
                (
                        c.getString(ConsultaReqExAdmision.IDREQUISITO),
                        c.getString(ConsultaReqExAdmision.REQUISITO_FICHA),
                        c.getString(ConsultaReqExAdmision.ID_INSCRIPCION),
                        c.getString(ConsultaReqExAdmision.VERSION),
                        c.getInt(ConsultaReqExAdmision.MODIFICADO)
                );

    }
}
