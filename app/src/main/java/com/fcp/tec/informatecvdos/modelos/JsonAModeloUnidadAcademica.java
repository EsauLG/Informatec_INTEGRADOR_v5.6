package com.fcp.tec.informatecvdos.modelos;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fcp.tec.informatecvdos.provider.ContractUnidadAcademica;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonAModeloUnidadAcademica
{

    public static final String TAG = JsonAModeloUnidadAcademica.class.getSimpleName();

    private HashMap<String, UnidadAcademica> unidadRemota = new HashMap<>();

    private interface ConsultaUnidadAcademica
    {
        //Proyeccion para la consulta de las UnidadAcademica (Se usará en el método Query)
        String[] PROYECCION =
                {
                        ContractUnidadAcademica.ControladorUnidadAcademica.IDUNIDAD_ACADEMICA,
                        ContractUnidadAcademica.ControladorUnidadAcademica.NOMBRE_UNIDAD,
                        ContractUnidadAcademica.ControladorUnidadAcademica.DIRECCION_UNIDAD,
                        ContractUnidadAcademica.ControladorUnidadAcademica.CORREO_UNIDAD,
                        ContractUnidadAcademica.ControladorUnidadAcademica.TEL_UNIDAD,
                        ContractUnidadAcademica.ControladorUnidadAcademica.HORARIO_UNIDAD,
                        ContractUnidadAcademica.ControladorUnidadAcademica.VIDEOGENERAL,
                        ContractUnidadAcademica.ControladorUnidadAcademica.IDINSCRIPCION,
                        ContractUnidadAcademica.ControladorUnidadAcademica.VERSION,
                        ContractUnidadAcademica.ControladorUnidadAcademica.MODIFICADO
                };

        //Indices de columnas
        int IDUNIDAD_ACADEMICA = 0;
        int NOMBRE_UNIDAD = 1;
        int DIRECCION_UNIDAD = 2;
        int CORREO_UNIDAD = 3;
        int TEL_UNIDAD = 4;
        int HORARIO_UNIDAD = 5;
        int VIDEOGENERAL = 6;
        int IDINSCRIPCION = 7;
        int VERSION = 8;
        int MODIFICADO = 9;
    }


    //Referencia al conversor JSON
    private Gson gson = new Gson();

    /**
     * permite tomar la respuesta JSON y almacenar sus datos en el objeto Calendario.java
     */
    public void procesar(JSONArray arrayJson)
    {

        for (UnidadAcademica carreraActual : gson.fromJson(arrayJson.toString(), UnidadAcademica[].class))
        {
            //Se añade UN id a cada fila de la carrera encontrada en UnidadAcademica[]
            unidadRemota.put(carreraActual.getIdUnidadAcademica(), carreraActual);
        }


    }

    public void procesarOperaciones(ArrayList<ContentProviderOperation> cpOperaciones, ContentResolver cResolver)
    {

        //Revisar la BD SQLite que tiene la app de manera local, los datos ya incluidos
        Cursor c = cResolver.query(ContractUnidadAcademica.ControladorUnidadAcademica.URI_CONTENIDO, JsonAModeloUnidadAcademica.ConsultaUnidadAcademica.PROYECCION, ContractUnidadAcademica.ControladorUnidadAcademica.INSERTADO + "=?", new String[]{"0"}, null);
        //Desde este cursor ya nos estamos refiriendo a datos LOCALES en la BD LOCAL
        if (c != null)
        {
            while (c.moveToNext())
            {
                //Convertir la fila del cursor en OBJETO UnidadAcademica.java
                UnidadAcademica filaActual = deCursorAUnidad(c);

                //Buscar si la Carrera actual se encuentra en el mapa de MAPA CARRERAS
                UnidadAcademica match = unidadRemota.get(filaActual.getIdUnidadAcademica());

                if (match != null)
                {
                    //Esta entrada EXISTE por lo que se remueve del "mapeado"
                    unidadRemota.remove(filaActual.getIdUnidadAcademica());

                    //Crear una URI de esta CARRERA
                    Uri updateUri = ContractUnidadAcademica.ControladorUnidadAcademica.construirUriUnidadAcademica(filaActual.getIdUnidadAcademica());

                    //Se aplica la resolución de conflictos, modificaciones de un mismo recurso
                    //tanto en el servidor como en la App. Quien tenga la versión más actual se
                    //toma como preponderante.
                    if (!match.compararCon(filaActual))
                    {
                        int flag = match.esMasReciente(filaActual);

                        if (flag > 0)
                        {
                            Log.i(TAG, "Programar actualizacion de Unidad " + updateUri);
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
                    Uri deleteUri = ContractUnidadAcademica.ControladorUnidadAcademica.construirUriUnidadAcademica(filaActual.getIdUnidadAcademica());
                    Log.i(TAG, "Programar eliminacion de la Unidad Academica");
                    cpOperaciones.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c.close();
        }

        //INSERTAR los items resultantes de manera LOCAL ya que se asume que no existen en el objeto
        //UnidadAcademica[]
        for (UnidadAcademica unidadAcademica : unidadRemota.values())
        {
            Log.i(TAG, "Programar inserción de NUEVA Unidad academica con ID = " + unidadAcademica.getIdUnidadAcademica());
            cpOperaciones.add(construirOperacionInsert(unidadAcademica));
        }

    }

    //region Operaciones de ContenProvider


    private ContentProviderOperation construirOperacionInsert(UnidadAcademica unidadAcademica)
    {
        Log.i(TAG, "construirOperacionInsert: ");

        return ContentProviderOperation.newInsert(ContractUnidadAcademica.ControladorUnidadAcademica.URI_CONTENIDO)
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.IDUNIDAD_ACADEMICA, unidadAcademica.getIdUnidadAcademica())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.NOMBRE_UNIDAD, unidadAcademica.getNombreUnidad())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.DIRECCION_UNIDAD, unidadAcademica.getDireccionUnidad())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.CORREO_UNIDAD, unidadAcademica.getCorreoUnidad())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.TEL_UNIDAD, unidadAcademica.getTelUnidad())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.HORARIO_UNIDAD, unidadAcademica.getHorarioUnidad())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.VIDEOGENERAL, unidadAcademica.getVideoGeneral())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.IDINSCRIPCION, unidadAcademica.getIdInscripcion())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.VERSION, unidadAcademica.getVersion())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.INSERTADO, 0)
                .build();

    }

    private ContentProviderOperation construirOperacionUpdate(UnidadAcademica match, Uri updateUri)
    {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.NOMBRE_UNIDAD, match.getNombreUnidad())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.DIRECCION_UNIDAD, match.getDireccionUnidad())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.CORREO_UNIDAD, match.getCorreoUnidad())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.TEL_UNIDAD, match.getTelUnidad())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.HORARIO_UNIDAD, match.getHorarioUnidad())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.VIDEOGENERAL, match.getVideoGeneral())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.IDINSCRIPCION, match.getIdInscripcion())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.VERSION, match.getVersion())
                .withValue(ContractUnidadAcademica.ControladorUnidadAcademica.MODIFICADO, match.getModificado())
                .build();
    }
    //endregion


    /**
     * convierte lo que encuentra en un cursor hacia un objeto Calendario.java
     *
     * @param c El cursor que contiene el dato a convertir
     */
    private UnidadAcademica deCursorAUnidad(Cursor c)
    {

        return new UnidadAcademica
                (
                        c.getString(ConsultaUnidadAcademica.IDUNIDAD_ACADEMICA),
                        c.getString(ConsultaUnidadAcademica.NOMBRE_UNIDAD),
                        c.getString(ConsultaUnidadAcademica.DIRECCION_UNIDAD),
                        c.getString(ConsultaUnidadAcademica.CORREO_UNIDAD),
                        c.getString(ConsultaUnidadAcademica.TEL_UNIDAD),
                        c.getString(ConsultaUnidadAcademica.HORARIO_UNIDAD),
                        c.getString(ConsultaUnidadAcademica.VIDEOGENERAL),
                        c.getString(ConsultaUnidadAcademica.IDINSCRIPCION),
                        c.getString(ConsultaUnidadAcademica.VERSION),
                        c.getInt(ConsultaUnidadAcademica.MODIFICADO)
                );

    }
}
