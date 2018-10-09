package com.fcp.tec.informatecvdos.modelos;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fcp.tec.informatecvdos.provider.ContractPreguntasTest;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonAModeloPreguntasTest
{
    public static final String TAG = JsonAModeloPreguntasTest.class.getSimpleName();

    //Se declara una variable que contendrá un "mapa" del calendario remoto (aqui se almacena el que acabamos de leer del JSON remoto)
    private HashMap<String, PreguntasTest> preguntaRemota = new HashMap<>();

    private interface ConsultaPregunta
    {
        //Proyeccion para la consulta del PreguntasTest (Se usará en el método Query)
        String[] PROYECCION =
                {
                        ContractPreguntasTest.ControladorPreguntasTest.ID_PREGUNTA,
                        ContractPreguntasTest.ControladorPreguntasTest.PREGUNTA,
                        ContractPreguntasTest.ControladorPreguntasTest.OPCION,
                        ContractPreguntasTest.ControladorPreguntasTest.VERSION,
                        ContractPreguntasTest.ControladorPreguntasTest.MODIFICADO
                };

        //Indices de columnas
        int ID_PREGUNTA = 0;
        int PREGUNTA = 1;
        int OPCION = 2;
        int VERSION = 3;
        int MODIFICADO = 4;
    }

    //Referencia al conversor JSON
    private Gson gson = new Gson();

    /**
     * permite tomar la respuesta JSON y almacenar sus datos en el objeto PreguntasTest.java
     */
    public void procesar(JSONArray arrayJson)
    {

        for (PreguntasTest preguntaActual : gson.fromJson(arrayJson.toString(), PreguntasTest[].class))
        {
            //Se añade UN id a cada fila del calendario encontrado en PreguntasTest[]
            preguntaRemota.put(preguntaActual.getIdPregunta(), preguntaActual);
        }


    }

    public void procesarOperaciones(ArrayList<ContentProviderOperation> cpOperaciones, ContentResolver cResolver)
    {


        //Revisar la BD SQLite que tiene la app de manera local, los datos ya incluidos
        Cursor c = cResolver.query(ContractPreguntasTest.ControladorPreguntasTest.URI_CONTENIDO, ConsultaPregunta.PROYECCION, ContractPreguntasTest.ControladorPreguntasTest.INSERTADO + "=?", new String[]{"0"}, null);
        //Desde este cursor ya nos estamos refiriendo a datos LOCALES en la BD LOCAL
        if (c != null)
        {
            while (c.moveToNext())
            {
                //Convertir la fila del cursor en OBJETO Becas.java
                PreguntasTest filaActual = deCursorACalendario(c);

                //Buscar si la Beca actual se encuentra en el mapa de MAPA BECAS
                PreguntasTest match = preguntaRemota.get(filaActual.getIdPregunta());

                if (match != null)
                {
                    //Esta entrada EXISTE por lo que se remueve del "mapeado"
                    preguntaRemota.remove(filaActual.getIdPregunta());

                    //Crear una URI de esta Beca
                    Uri updateUri = ContractPreguntasTest.ControladorPreguntasTest.construirUriPreguntas(filaActual.getIdPregunta());

                    //Se aplica la resolución de conflictos, modificaciones de un mismo recurso
                    //tanto en el servidor como en la App. Quien tenga la versión más actual se
                    //toma como preponderante.
                    if (!match.compararCon(filaActual))
                    {
                        int flag = match.esMasReciente(filaActual);

                        if (flag > 0)
                        {
                            Log.i(TAG, "Programar actualizacion de la Pregunta " + updateUri);
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
                    Uri deleteUri = ContractPreguntasTest.ControladorPreguntasTest.construirUriPreguntas(filaActual.getIdPregunta());
                    Log.i(TAG, "Programar eliminacion de la pregunta");
                    cpOperaciones.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c.close();
        }

        //INSERTAR los items resultantes de manera LOCAL ya que se asume que no existen en el objeto
        //PreguntasTest[]
        for (PreguntasTest pregunta : preguntaRemota.values())
        {
            Log.i(TAG, "Programar inserción de NUEVA pregunta con ID = " + pregunta.getIdPregunta());
            cpOperaciones.add(construirOperacionInsert(pregunta));
        }

    }

    //region Operaciones de ContenProvider


    private ContentProviderOperation construirOperacionInsert(PreguntasTest pregunta)
    {
        Log.i(TAG, "construirOperacionInsert: ");

        return ContentProviderOperation.newInsert(ContractPreguntasTest.ControladorPreguntasTest.URI_CONTENIDO)
                .withValue(ContractPreguntasTest.ControladorPreguntasTest.ID_PREGUNTA, pregunta.getIdPregunta())
                .withValue(ContractPreguntasTest.ControladorPreguntasTest.PREGUNTA, pregunta.getPregunta())
                .withValue(ContractPreguntasTest.ControladorPreguntasTest.OPCION, pregunta.getOpcion())
                .withValue(ContractPreguntasTest.ControladorPreguntasTest.INSERTADO, 0)
                .build();

    }

    private ContentProviderOperation construirOperacionUpdate(PreguntasTest match, Uri updateUri)
    {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(ContractPreguntasTest.ControladorPreguntasTest.ID_PREGUNTA, match.getIdPregunta())
                .withValue(ContractPreguntasTest.ControladorPreguntasTest.PREGUNTA, match.getPregunta())
                .withValue(ContractPreguntasTest.ControladorPreguntasTest.OPCION, match.getOpcion())
                .withValue(ContractPreguntasTest.ControladorPreguntasTest.VERSION, match.getVersion())
                .withValue(ContractPreguntasTest.ControladorPreguntasTest.MODIFICADO, match.getModificado())
                .build();
    }
    //endregion

    /**
     * convierte lo que encuentra en un cursor hacia un objeto PreguntasTest.java
     *
     * @param c El cursor que contiene el dato a convertir
     */
    private PreguntasTest deCursorACalendario(Cursor c)
    {

        return new PreguntasTest
                (
                        c.getString(ConsultaPregunta.ID_PREGUNTA),
                        c.getString(ConsultaPregunta.PREGUNTA),
                        c.getString(ConsultaPregunta.OPCION),
                        c.getString(ConsultaPregunta.VERSION),
                        c.getInt(ConsultaPregunta.MODIFICADO)
                );

    }

}
