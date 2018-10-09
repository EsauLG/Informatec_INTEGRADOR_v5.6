package com.fcp.tec.informatecvdos.modelos;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fcp.tec.informatecvdos.provider.ContractParaBecas;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonAModeloBecas
{
    private static final String TAG = JsonAModeloBecas.class.getSimpleName();

    private interface ConsultaBecas
    {
        //Proyeccion para la consulta de Becas
        String[] PROYECCION =
                {
                        ContractParaBecas.ControladorBecas.IDBECAS,
                        ContractParaBecas.ControladorBecas.NOMBRE_BECA,
                        ContractParaBecas.ControladorBecas.DESCRIPCION_BECA,
                        ContractParaBecas.ControladorBecas.TIPO_BECA,
                        ContractParaBecas.ControladorBecas.IDUNIDAD_ACADEMICA,
                        ContractParaBecas.ControladorBecas.VERSION,
                        ContractParaBecas.ControladorBecas.MODIFICADO
                };

        //Indices de columnas
        int IDBECAS = 0;
        int NOMBRE_BECA = 1;
        int DESCRIPCION_BECA = 2;
        int TIPO_BECA = 3;
        int IDUNIDAD_ACADEMICA = 4;
        int VERSION = 5;
        int MODIFICADO = 6;
    }

    //Se declara una variable que contendrá un "mapa" de las becas remotas (las que acabamos de leer del JSON remoto)
    private HashMap<String, Becas> becasRemotas = new HashMap<>();

    //Referencia al conversor JSON
    private Gson gson = new Gson();

    /**
     * permite tomar la respuesta JSON y almacenar sus datos en el objeto Becas.java
     */
    public void procesar(JSONArray arrayJson)
    {
        //Añadir elementos recibidos al modelo POJO, en este caso al objeto Becas
        //PARA becaActual de TIPO "Becas" realiza--->: gson.fromJason(JSON de ENTRADA, Array de Objetos Becas[])
        //esto realiza la conversión del JSONArray que encuentre y los convierte en objetos Becas[]
        //la cantidad de objetos de tipo Becas[] se tomará en base a lo que coincida con el  POJO en cuestión
        for (Becas becaActual : gson.fromJson(arrayJson.toString(), Becas[].class))
        {
            //Se añade UN id a cada beca encontrada en Becas[]
            becasRemotas.put(becaActual.getIdBeca(), becaActual);
        }

    }

    public void procesarOperaciones(ArrayList<ContentProviderOperation>
                                            cpOperaciones, ContentResolver cResolver)
    {



        //Revisar la BD SQLite que tiene la app de manera local, los datos ya incluidos
        Cursor c = cResolver.query(ContractParaBecas.ControladorBecas.URI_CONTENIDO, ConsultaBecas.PROYECCION, ContractParaBecas.ControladorBecas.INSERTADO + "=?", new String[]{"0"}, null);
        //Desde este cursor ya nos estamos refiriendo a datos LOCALES en la BD LOCAL
        if (c != null)
        {
            while (c.moveToNext())
            {
                //Convertir la fila del cursor en OBJETO Becas.java
                Becas filaActual = deCursorABecas(c);

                //Buscar si la Beca actual se encuentra en el mapa de MAPA BECAS
                Becas match = becasRemotas.get(filaActual.getIdBeca());//TODO esclarecer utilidad

                if (match != null)
                {
                    //Esta entrada EXISTE por lo que se remueve del "mapeado"
                    becasRemotas.remove(filaActual.getIdBeca());

                    //Crear una URI de esta Beca
                    Uri updateUri = ContractParaBecas.ControladorBecas.construirUriBeca(filaActual.getIdBeca());

                    //Se aplica la resolución de conflictos, modificaciones de un mismo recurso
                    //tanto en el servidor como en la App. Quien tenga la versión más actual se
                    //toma como preponderante.
                    if (!match.compararCon(filaActual))
                    {
                        int flag = match.esMasReciente(filaActual);

                        if (flag > 0)
                        {
                            Log.i(TAG, "Programar actualizacion de la Beca " + updateUri);
                            //Verificación: ¿Existe conflicto de modificación?
                            if (filaActual.getModificado() == 1)
                            {
                                match.setModificado(0);
                            }
                            cpOperaciones.add(construirOperacionUpdate(match, updateUri));

                        }
                    }

                }
                else
                {
                    //Se deduce que aquellos elementos que no coincidieron ya no existen en el servidor
                    //por lo que se eliminarán
                    Uri deleteUri = ContractParaBecas.ControladorBecas.construirUriBeca(filaActual.getIdBeca());
                    Log.i(TAG, "Programar eliminacion de la beca");
                    cpOperaciones.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c.close();
        }

        //INSERTAR los items resultantes de manera LOCAL ya que se asume que no existen en el objeto
        //Becas[]
        for (Becas becas : becasRemotas.values())
        {
            Log.i(TAG, "Programar inserción de NUEVA beca con ID = "+becas.getIdBeca());
            cpOperaciones.add(construirOperacionInsert(becas));
        }

    }

    private ContentProviderOperation construirOperacionInsert(Becas beca)
    {
        Log.i(TAG, "construirOperacionInsert: ");

        return ContentProviderOperation.newInsert(ContractParaBecas.ControladorBecas.URI_CONTENIDO)
                .withValue(ContractParaBecas.ControladorBecas.IDBECAS, beca.getIdBeca())
                .withValue(ContractParaBecas.ControladorBecas.NOMBRE_BECA, beca.getNombreBeca())
                .withValue(ContractParaBecas.ControladorBecas.DESCRIPCION_BECA, beca.getDescripcionBeca())
                .withValue(ContractParaBecas.ControladorBecas.TIPO_BECA, beca.getTipoBeca())
                .withValue(ContractParaBecas.ControladorBecas.IDUNIDAD_ACADEMICA, beca.getIdUnidadAcademica())
                .withValue(ContractParaBecas.ControladorBecas.VERSION, beca.getVersion())
                .withValue(ContractParaBecas.ControladorBecas.INSERTADO, 0)
                .build();

    }

    private ContentProviderOperation construirOperacionUpdate(Becas match, Uri updateUri)
    {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(ContractParaBecas.ControladorBecas.IDBECAS, match.getIdBeca())
                .withValue(ContractParaBecas.ControladorBecas.NOMBRE_BECA, match.getNombreBeca())
                .withValue(ContractParaBecas.ControladorBecas.DESCRIPCION_BECA, match.getDescripcionBeca())
                .withValue(ContractParaBecas.ControladorBecas.TIPO_BECA, match.getTipoBeca())
                .withValue(ContractParaBecas.ControladorBecas.IDUNIDAD_ACADEMICA, match.getIdUnidadAcademica())
                .withValue(ContractParaBecas.ControladorBecas.VERSION, match.getVersion())
                .withValue(ContractParaBecas.ControladorBecas.MODIFICADO, match.getModificado())
                .build();
    }

    /**
     * convierte lo que encuentra en un cursor hacia un objeto Becas.java
     *
     * @param c El cursor que contiene el dato a convertir
     */
    private Becas deCursorABecas(Cursor c)
    {

        return new Becas
                (
                        c.getString(ConsultaBecas.IDBECAS),
                        c.getString(ConsultaBecas.NOMBRE_BECA),
                        c.getString(ConsultaBecas.DESCRIPCION_BECA),
                        c.getString(ConsultaBecas.TIPO_BECA),
                        c.getString(ConsultaBecas.IDUNIDAD_ACADEMICA),
                        c.getString(ConsultaBecas.VERSION),
                        c.getInt(ConsultaBecas.MODIFICADO)
                );

    }
}
