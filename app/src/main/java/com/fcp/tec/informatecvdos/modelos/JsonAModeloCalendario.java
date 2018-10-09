package com.fcp.tec.informatecvdos.modelos;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fcp.tec.informatecvdos.provider.ContractCalendario;
import com.fcp.tec.informatecvdos.provider.ContractParaBecas;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonAModeloCalendario
{
    public static final String TAG = JsonAModeloCalendario.class.getSimpleName();

    //Se declara una variable que contendrá un "mapa" del calendario remoto (aqui se almacena el que acabamos de leer del JSON remoto)
    private HashMap<String, Calendario> calendarioRemoto = new HashMap<>();

    private interface ConsultaCalendario
    {
        //Proyeccion para la consulta del Calendario (Se usará en el método Query)
        String[] PROYECCION =
                {
                        ContractCalendario.ControladorCalendario.IDEVENTO,
                        ContractCalendario.ControladorCalendario.CONTENIDO_FECHA,
                        ContractCalendario.ControladorCalendario.VERSION,
                        ContractCalendario.ControladorCalendario.MODIFICADO
                };

        //Indices de columnas
        int IDEVENTO = 0;
        int CONTENIDO_FECHA = 1;
        int VERSION = 2;
        int MODIFICADO = 3;
    }

    //Referencia al conversor JSON
    private Gson gson = new Gson();

    /**
     * permite tomar la respuesta JSON y almacenar sus datos en el objeto Calendario.java
     */
    public void procesar(JSONArray arrayJson)
    {

        for (Calendario calendarioActual : gson.fromJson(arrayJson.toString(), Calendario[].class))
        {
            //Se añade UN id a cada fila del calendario encontrado en Calendario[]
            calendarioRemoto.put(calendarioActual.getIdEvento(), calendarioActual);
        }


    }

    public void procesarOperaciones(ArrayList<ContentProviderOperation> cpOperaciones, ContentResolver cResolver)
    {


        //Revisar la BD SQLite que tiene la app de manera local, los datos ya incluidos
        Cursor c = cResolver.query(ContractCalendario.ControladorCalendario.URI_CONTENIDO, ConsultaCalendario.PROYECCION, ContractCalendario.ControladorCalendario.INSERTADO + "=?", new String[]{"0"}, null);
        //Desde este cursor ya nos estamos refiriendo a datos LOCALES en la BD LOCAL
        if (c != null)
        {
            while (c.moveToNext())
            {
                //Convertir la fila del cursor en OBJETO Becas.java
                Calendario filaActual = deCursorACalendario(c);

                //Buscar si la Beca actual se encuentra en el mapa de MAPA BECAS
                Calendario match = calendarioRemoto.get(filaActual.getIdEvento());

                if (match != null)
                {
                    //Esta entrada EXISTE por lo que se remueve del "mapeado"
                    calendarioRemoto.remove(filaActual.getIdEvento());

                    //Crear una URI de esta Beca
                    Uri updateUri = ContractCalendario.ControladorCalendario.construirUriCalendario(filaActual.getIdEvento());

                    //Se aplica la resolución de conflictos, modificaciones de un mismo recurso
                    //tanto en el servidor como en la App. Quien tenga la versión más actual se
                    //toma como preponderante.
                    if (!match.compararCalendarioCon(filaActual))
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

                } else
                {
                    //Se deduce que aquellos elementos que no coincidieron ya no existen en el servidor
                    //por lo que se eliminarán
                    Uri deleteUri = ContractCalendario.ControladorCalendario.construirUriCalendario(filaActual.getIdEvento());
                    Log.i(TAG, "Programar eliminacion de la beca");
                    cpOperaciones.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c.close();
        }

        //INSERTAR los items resultantes de manera LOCAL ya que se asume que no existen en el objeto
        //Calendario[]
        for (Calendario calendario : calendarioRemoto.values())
        {
            Log.i(TAG, "Programar inserción de NUEVA calendario con ID = " + calendario.getIdEvento());
            cpOperaciones.add(construirOperacionInsert(calendario));
        }

    }

    //region Operaciones de ContenProvider


    private ContentProviderOperation construirOperacionInsert(Calendario calendario)
    {
        Log.i(TAG, "construirOperacionInsert: ");

        return ContentProviderOperation.newInsert(ContractCalendario.ControladorCalendario.URI_CONTENIDO)
                .withValue(ContractCalendario.ControladorCalendario.IDEVENTO, calendario.getIdEvento())
                .withValue(ContractCalendario.ControladorCalendario.CONTENIDO_FECHA, calendario.getContenidoFecha())
                .withValue(ContractCalendario.ControladorCalendario.VERSION, calendario.getVersion())
                .withValue(ContractCalendario.ControladorCalendario.INSERTADO, 0)
                .build();

    }

    private ContentProviderOperation construirOperacionUpdate(Calendario match, Uri updateUri)
    {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(ContractCalendario.ControladorCalendario.IDEVENTO, match.getIdEvento())
                .withValue(ContractCalendario.ControladorCalendario.CONTENIDO_FECHA, match.getContenidoFecha())
                .withValue(ContractCalendario.ControladorCalendario.VERSION, match.getVersion())
                .withValue(ContractCalendario.ControladorCalendario.MODIFICADO, match.getModificado())
                .build();
    }
    //endregion

    /**
     * convierte lo que encuentra en un cursor hacia un objeto Calendario.java
     *
     * @param c El cursor que contiene el dato a convertir
     */
    private Calendario deCursorACalendario(Cursor c)
    {

        return new Calendario
                (
                        c.getString(ConsultaCalendario.IDEVENTO),
                        c.getString(ConsultaCalendario.CONTENIDO_FECHA),
                        c.getString(ConsultaCalendario.VERSION),
                        c.getInt(ConsultaCalendario.MODIFICADO)
                );

    }

}
