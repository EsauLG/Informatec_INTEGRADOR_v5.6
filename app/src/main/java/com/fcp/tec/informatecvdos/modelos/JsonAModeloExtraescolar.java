package com.fcp.tec.informatecvdos.modelos;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fcp.tec.informatecvdos.provider.ContractExtraescolar;
import com.fcp.tec.informatecvdos.provider.ContractParaBecas;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonAModeloExtraescolar
{
    private static final String TAG = JsonAModeloExtraescolar.class.getSimpleName();

    private interface ConsultaExtraescolar
    {
        //Proyeccion para la consulta de Extraescolares
        String[] PROYECCION =
                {
                        ContractExtraescolar.ControladorExtraescolares.IDEXTRAESCOLARES,
                        ContractExtraescolar.ControladorExtraescolares.NOMBRE_EXTRA,
                        ContractExtraescolar.ControladorExtraescolares.TIPO_EXTRA,
                        ContractExtraescolar.ControladorExtraescolares.OPCION_SELECCION,
                        ContractExtraescolar.ControladorExtraescolares.IMAGEN_EXTRAESCOLAR,
                        ContractParaBecas.ControladorBecas.VERSION,
                        ContractParaBecas.ControladorBecas.MODIFICADO
                };

        //Indices de columnas
        int IDEXTRAESCOLARES = 0;
        int NOMBRE_EXTRA = 1;
        int TIPO_EXTRA = 2;
        int OPCION_SELECCION = 3;
        int IMAGEN_EXTRAESCOLAR = 4;
        int VERSION = 5;
        int MODIFICADO = 6;
    }

    //Se declara una variable que contendrá un "mapa" de los extraescolares remotos (las que acabamos de leer del JSON remoto)
    private HashMap<String, Extraescolar> extraRemotos = new HashMap<>();

    //Referencia al conversor JSON
    private Gson gson = new Gson();

    public void procesar(JSONArray arrayJson)
    {
        //Añadir elementos recibidos al modelo POJO, en este caso al objeto Becas
        //PARA becaActual de TIPO "Becas" realiza--->: gson.fromJason(JSON de ENTRADA, Array de Objetos Becas[])
        //esto realiza la conversión del JSONArray que encuentre y los convierte en objetos Becas[]
        //la cantidad de objetos de tipo Becas[] se tomará en base a lo que coincida con el  POJO en cuestión
        for (Extraescolar extraActual : gson.fromJson(arrayJson.toString(), Extraescolar[].class))
        {
            //Se añade UN id a cada extraescolar encontrado en Extraescolares[]
            extraRemotos.put(extraActual.getIdExtraescolares(), extraActual);
        }


    }

    public void procesarOperaciones(ArrayList<ContentProviderOperation> cpOperaciones, ContentResolver cResolver)
    {

        //Revisar la BD SQLite que tiene la app de manera local, los datos ya incluidos
        Cursor c = cResolver.query(ContractExtraescolar.ControladorExtraescolares.URI_CONTENIDO, JsonAModeloExtraescolar.ConsultaExtraescolar.PROYECCION, ContractExtraescolar.ControladorExtraescolares.INSERTADO + "=?", new String[]{"0"}, null);
        //Desde este cursor ya nos estamos refiriendo a datos LOCALES en la BD LOCAL
        if (c != null)
        {
            while (c.moveToNext())
            {
                //Convertir la fila del cursor en OBJETO Becas.java
                Extraescolar filaActual = deCursorAExtraescolar(c);

                //Buscar si el Extraescolar actual se encuentra en el mapa de MAPA EXTRAESCOLARES
                Extraescolar match = extraRemotos.get(filaActual.getIdExtraescolares());

                if (match != null)
                {
                    //Esta entrada EXISTE por lo que se remueve del "mapeado"
                    extraRemotos.remove(filaActual.getIdExtraescolares());

                    //Crear una URI de este Extraescolar
                    Uri updateUri = ContractExtraescolar.ControladorExtraescolares.construirUriExtraescolar(filaActual.getIdExtraescolares());

                    //Se aplica la resolución de conflictos, modificaciones de un mismo recurso
                    //tanto en el servidor como en la App. Quien tenga la versión más actual se
                    //toma como preponderante.
                    if (!match.compararExtraescolarCon(filaActual))
                    {
                        int flag = match.esMasReciente(filaActual);

                        if (flag > 0)
                        {
                            Log.i(TAG, "Programar actualizacion del Extraescolar " + updateUri);
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
                    Uri deleteUri = ContractExtraescolar.ControladorExtraescolares.construirUriExtraescolar(filaActual.getIdExtraescolares());
                    Log.i(TAG, "Programar eliminacion del extraescolar");
                    cpOperaciones.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c.close();
        }

        //INSERTAR los items resultantes de manera LOCAL ya que se asume que no existen en el objeto
        //Extraescolares[]
        for (Extraescolar extraescolar : extraRemotos.values())
        {
            Log.i(TAG, "Programar inserción de NUEVO extraescolar con ID = "+extraescolar.getIdExtraescolares());
            cpOperaciones.add(construirOperacionInsert(extraescolar));
        }

    }

    private ContentProviderOperation construirOperacionInsert(Extraescolar extraescolar)
    {
        Log.i(TAG, "construirOperacionInsert: ");

        return ContentProviderOperation.newInsert(ContractParaBecas.ControladorBecas.URI_CONTENIDO)
                .withValue(ContractExtraescolar.ControladorExtraescolares.IDEXTRAESCOLARES, extraescolar.getIdExtraescolares())
                .withValue(ContractExtraescolar.ControladorExtraescolares.NOMBRE_EXTRA, extraescolar.getNombreExtra())
                .withValue(ContractExtraescolar.ControladorExtraescolares.TIPO_EXTRA, extraescolar.getTipoExtra())
                .withValue(ContractExtraescolar.ControladorExtraescolares.OPCION_SELECCION, extraescolar.getOpcionSeleccion())
                .withValue(ContractExtraescolar.ControladorExtraescolares.IMAGEN_EXTRAESCOLAR, extraescolar.getImagenExtraEscolar())
                .withValue(ContractExtraescolar.ControladorExtraescolares.VERSION, extraescolar.getVersion())
                .withValue(ContractExtraescolar.ControladorExtraescolares.INSERTADO, 0)
                .build();

    }

    private ContentProviderOperation construirOperacionUpdate(Extraescolar match, Uri updateUri)
    {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(ContractExtraescolar.ControladorExtraescolares.IDEXTRAESCOLARES, match.getIdExtraescolares())
                .withValue(ContractExtraescolar.ControladorExtraescolares.NOMBRE_EXTRA, match.getNombreExtra())
                .withValue(ContractExtraescolar.ControladorExtraescolares.TIPO_EXTRA, match.getTipoExtra())
                .withValue(ContractExtraescolar.ControladorExtraescolares.OPCION_SELECCION, match.getOpcionSeleccion())
                .withValue(ContractExtraescolar.ControladorExtraescolares.IMAGEN_EXTRAESCOLAR, match.getImagenExtraEscolar())
                .withValue(ContractExtraescolar.ControladorExtraescolares.VERSION, match.getVersion())
                .withValue(ContractExtraescolar.ControladorExtraescolares.INSERTADO, 0)
                .build();
    }

    /**
     * convierte lo que encuentra en un cursor hacia un objeto Becas.java
     *
     * @param c El cursor que contiene el dato a convertir
     */
    private Extraescolar deCursorAExtraescolar(Cursor c)
    {

        return new Extraescolar
                (
                        c.getString(ConsultaExtraescolar.IDEXTRAESCOLARES),
                        c.getString(ConsultaExtraescolar.NOMBRE_EXTRA),
                        c.getString(ConsultaExtraescolar.TIPO_EXTRA),
                        c.getString(ConsultaExtraescolar.OPCION_SELECCION),
                        c.getString(ConsultaExtraescolar.IMAGEN_EXTRAESCOLAR),
                        c.getString(JsonAModeloExtraescolar.ConsultaExtraescolar.VERSION),
                        c.getInt(JsonAModeloExtraescolar.ConsultaExtraescolar.MODIFICADO)
                );

    }
}
