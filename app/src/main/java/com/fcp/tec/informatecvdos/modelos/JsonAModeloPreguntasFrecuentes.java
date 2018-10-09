package com.fcp.tec.informatecvdos.modelos;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fcp.tec.informatecvdos.provider.ContractPreguntasFrecuentes;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonAModeloPreguntasFrecuentes
{
    private static final String TAG = JsonAModeloPreguntasFrecuentes.class.getSimpleName();

    private interface ConsultaPreguntasFrecuentes
    {
        //Proyeccion para la consulta de Preguntas Frecuentes
        String[] PROYECCION =
                {
                        ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.IDPREGUNTA,
                        ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.TITULO_PREGUNTA,
                        ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.CONTENIDO_PREGUNTA,
                        ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.VERSION,
                        ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.MODIFICADO
                };

        //Indices de columnas
        int IDPREGUNTA = 0;
        int TITULO_PREGUNTA = 1;
        int CONTENIDO_PREGUNTA = 2;
        int VERSION = 3;
        int MODIFICADO = 4;
    }

    //Se declara una variable que contendrá un "mapa" de las becas remotas (las que acabamos de leer del JSON remoto)
    private HashMap<String, PreguntasFrecuentes> preguntasRemotas = new HashMap<>();

    //Referencia al conversor JSON
    private Gson gson = new Gson();

    /**
     * permite tomar la respuesta JSON y almacenar sus datos en el objeto PreguntasFrecuentes.java
     */
    public void procesar(JSONArray arrayJson)
    {
        //Añadir elementos recibidos al modelo POJO, en este caso al objeto PreguntasFrecuentes
        //PARA becaActual de TIPO "PreguntasFrecuentes" realiza--->: gson.fromJason(JSON de ENTRADA, Array de Objetos PreguntasFrecuentes[])
        //esto realiza la conversión del JSONArray que encuentre y los convierte en objetos PreguntasFrecuentes[]
        //la cantidad de objetos de tipo PreguntasFrecuentes[] se tomará en base a lo que coincida con el  POJO en cuestión
        for (PreguntasFrecuentes preguntaActual : gson.fromJson(arrayJson.toString(), PreguntasFrecuentes[].class))
        {
            //Se añade UN id a cada beca encontrada en PreguntasFrecuentes[]
            preguntasRemotas.put(preguntaActual.getIdPregunta(), preguntaActual);
        }


    }

    public void procesarOperaciones(ArrayList<ContentProviderOperation> cpOperaciones, ContentResolver cResolver)
    {


        //Revisar la BD SQLite que tiene la app de manera local, los datos ya incluidos
        Cursor c = cResolver.query(ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.URI_CONTENIDO, ConsultaPreguntasFrecuentes.PROYECCION, ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.INSERTADO + "=?", new String[]{"0"}, null);
        //Desde este cursor ya nos estamos refiriendo a datos LOCALES en la BD LOCAL
        if (c != null)
        {
            while (c.moveToNext())
            {
                //Convertir la fila del cursor en OBJETO PreguntasFrecuentes.java
                PreguntasFrecuentes filaActual = deCursorAPregunta(c);

                //Buscar si la Beca actual se encuentra en el mapa de MAPA BECAS
                PreguntasFrecuentes match = preguntasRemotas.get(filaActual.getIdPregunta());

                if (match != null)
                {
                    //Esta entrada EXISTE por lo que se remueve del "mapeado"
                    preguntasRemotas.remove(filaActual.getIdPregunta());

                    //Crear una URI de esta Beca
                    Uri updateUri = ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.construirUriPreguntasFrecuentes(filaActual.getIdPregunta());

                    //Se aplica la resolución de conflictos, modificaciones de un mismo recurso
                    //tanto en el servidor como en la App. Quien tenga la versión más actual se
                    //toma como preponderante.
                    if (!match.compararPreguntaCon(filaActual))
                    {
                        int flag = match.esMasReciente(filaActual);

                        if (flag > 0)
                        {
                            Log.i(TAG, "Programar actualizacion de la pregunta " + updateUri);
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
                    Uri deleteUri = ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.construirUriPreguntasFrecuentes(filaActual.getIdPregunta());
                    Log.i(TAG, "Programar eliminacion de la beca");
                    cpOperaciones.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c.close();
        }

        //INSERTAR los items resultantes de manera LOCAL ya que se asume que no existen en el objeto
        //PreguntasFrecuentes[]
        for (PreguntasFrecuentes pregunta : preguntasRemotas.values())
        {
            Log.i(TAG, "Programar inserción de NUEVA pregunta con ID = " + pregunta.getIdPregunta());
            cpOperaciones.add(construirOperacionInsert(pregunta));
        }

    }

    private ContentProviderOperation construirOperacionInsert(PreguntasFrecuentes pregunta)
    {
        Log.i(TAG, "construirOperacionInsert: ");

        return ContentProviderOperation.newInsert(ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.URI_CONTENIDO)
                .withValue(ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.IDPREGUNTA, pregunta.getIdPregunta())
                .withValue(ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.TITULO_PREGUNTA, pregunta.getTituloPregunta())
                .withValue(ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.CONTENIDO_PREGUNTA, pregunta.getContenidoPregunta())
                .withValue(ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.VERSION, pregunta.getVersion())
                .withValue(ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.INSERTADO, 0)
                .build();

    }

    private ContentProviderOperation construirOperacionUpdate(PreguntasFrecuentes match, Uri updateUri)
    {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.IDPREGUNTA, match.getIdPregunta())
                .withValue(ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.TITULO_PREGUNTA, match.getTituloPregunta())
                .withValue(ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.CONTENIDO_PREGUNTA, match.getContenidoPregunta())
                .withValue(ContractPreguntasFrecuentes.ControladorPreguntasFrecuentes.MODIFICADO, match.getModificado())
                .build();
    }

    /**
     * convierte lo que encuentra en un cursor hacia un objeto PreguntasFrecuentes.java
     *
     * @param c El cursor que contiene el dato a convertir
     */
    private PreguntasFrecuentes deCursorAPregunta(Cursor c)
    {

        return new PreguntasFrecuentes
                (
                        c.getString(ConsultaPreguntasFrecuentes.IDPREGUNTA),
                        c.getString(ConsultaPreguntasFrecuentes.TITULO_PREGUNTA),
                        c.getString(ConsultaPreguntasFrecuentes.CONTENIDO_PREGUNTA),
                        c.getString(ConsultaPreguntasFrecuentes.VERSION),
                        c.getInt(ConsultaPreguntasFrecuentes.MODIFICADO)
                );

    }
}
