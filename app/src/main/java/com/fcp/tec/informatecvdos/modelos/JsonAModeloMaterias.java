package com.fcp.tec.informatecvdos.modelos;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fcp.tec.informatecvdos.provider.ContractMaterias;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonAModeloMaterias
{

    public static final String TAG = JsonAModeloMaterias.class.getSimpleName();

    //Se declara una variable que contendrá un "mapa" del calendario remoto (aqui se almacena el que acabamos de leer del JSON remoto)
    private HashMap<String, Materias> materiasRemota = new HashMap<>();

    private interface ConsultaMaterias
    {
        //Proyeccion para la consulta de las Materias (Se usará en el método Query)
        String[] PROYECCION =
                {
                        ContractMaterias.ControladorMaterias.IDMATERIAS,
                        ContractMaterias.ControladorMaterias.NOMBRE_MATERIA,
                        ContractMaterias.ControladorMaterias.CLAVE_MATERIA,
                        ContractMaterias.ControladorMaterias.TIPO_MATERIA,
                        ContractMaterias.ControladorMaterias.VERSION,
                        ContractMaterias.ControladorMaterias.MODIFICADO
                };

        //Indices de columnas
        int IDMATERIAS = 0;
        int NOMBRE_MATERIAS = 1;
        int CLAVE_MATERIA = 2;
        int TIPO_MATERIA = 3;
        int VERSION = 4;
        int MODIFICADO = 5;
    }

    //Referencia al conversor JSON
    private Gson gson = new Gson();

    /**
     * permite tomar la respuesta JSON y almacenar sus datos en el objeto Materias.java
     */
    public void procesar(JSONArray arrayJson)
    {

        for (Materias materiaActual : gson.fromJson(arrayJson.toString(), Materias[].class))
        {
            //Se añade UN id a cada fila del calendario encontrado en Materias[]
            materiasRemota.put(materiaActual.getIdMaterias(), materiaActual);
        }


    }

    public void procesarOperaciones(ArrayList<ContentProviderOperation> cpOperaciones, ContentResolver cResolver)
    {


        //Revisar la BD SQLite que tiene la app de manera local, los datos ya incluidos
        Cursor c = cResolver.query(ContractMaterias.ControladorMaterias.URI_CONTENIDO, ConsultaMaterias.PROYECCION, ContractMaterias.ControladorMaterias.INSERTADO + "=?", new String[]{"0"}, null);
        //Desde este cursor ya nos estamos refiriendo a datos LOCALES en la BD LOCAL
        if (c != null)
        {
            while (c.moveToNext())
            {
                //Convertir la fila del cursor en OBJETO Becas.java
                Materias filaActual = deCursorAMaterias(c);

                //Buscar si la Beca actual se encuentra en el mapa de MAPA BECAS
                Materias match = materiasRemota.get(filaActual.getIdMaterias());

                if (match != null)
                {
                    //Esta entrada EXISTE por lo que se remueve del "mapeado"
                    materiasRemota.remove(filaActual.getIdMaterias());

                    //Crear una URI de esta Beca
                    Uri updateUri = ContractMaterias.ControladorMaterias.construirUriMaterias(filaActual.getIdMaterias());

                    //Se aplica la resolución de conflictos, modificaciones de un mismo recurso
                    //tanto en el servidor como en la App. Quien tenga la versión más actual se
                    //toma como preponderante.
                    if (!match.compararMateriaCon(filaActual))
                    {
                        int flag = match.esMasReciente(filaActual);

                        if (flag > 0)
                        {
                            Log.i(TAG, "Programar actualizacion de la Materia " + updateUri);
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
                    Uri deleteUri = ContractMaterias.ControladorMaterias.construirUriMaterias(filaActual.getIdMaterias());
                    Log.i(TAG, "Programar eliminacion de materia");
                    cpOperaciones.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c.close();
        }


        for (Materias materias : materiasRemota.values())
        {
            Log.i(TAG, "Programar inserción de NUEVA materia con ID = " + materias.getIdMaterias());
            cpOperaciones.add(construirOperacionInsert(materias));
        }

    }

    //region Operaciones de ContenProvider


    private ContentProviderOperation construirOperacionInsert(Materias materias)
    {
        Log.i(TAG, "construirOperacionInsert: ");

        return ContentProviderOperation.newInsert(ContractMaterias.ControladorMaterias.URI_CONTENIDO)
                .withValue(ContractMaterias.ControladorMaterias.IDMATERIAS, materias.getIdMaterias())
                .withValue(ContractMaterias.ControladorMaterias.NOMBRE_MATERIA, materias.getNombreMateria())
                .withValue(ContractMaterias.ControladorMaterias.CLAVE_MATERIA, materias.getClaveMateria())
                .withValue(ContractMaterias.ControladorMaterias.TIPO_MATERIA, materias.getTipoMateria())
                .withValue(ContractMaterias.ControladorMaterias.VERSION, materias.getVersion())
                .withValue(ContractMaterias.ControladorMaterias.INSERTADO, 0)
                .build();

    }

    private ContentProviderOperation construirOperacionUpdate(Materias match, Uri updateUri)
    {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(ContractMaterias.ControladorMaterias.IDMATERIAS, match.getIdMaterias())
                .withValue(ContractMaterias.ControladorMaterias.NOMBRE_MATERIA, match.getNombreMateria())
                .withValue(ContractMaterias.ControladorMaterias.CLAVE_MATERIA, match.getClaveMateria())
                .withValue(ContractMaterias.ControladorMaterias.TIPO_MATERIA, match.getTipoMateria())
                .withValue(ContractMaterias.ControladorMaterias.VERSION, match.getVersion())
                .withValue(ContractMaterias.ControladorMaterias.INSERTADO, 0)
                .build();
    }
    //endregion

    /**
     * convierte lo que encuentra en un cursor hacia un objeto Materias.java
     *
     * @param c El cursor que contiene el dato a convertir
     */
    private Materias deCursorAMaterias(Cursor c)
    {

        return new Materias
                (
                        c.getString(ConsultaMaterias.IDMATERIAS),
                        c.getString(ConsultaMaterias.NOMBRE_MATERIAS),
                        c.getString(ConsultaMaterias.CLAVE_MATERIA),
                        c.getString(ConsultaMaterias.TIPO_MATERIA),
                        c.getString(ConsultaMaterias.VERSION),
                        c.getInt(ConsultaMaterias.MODIFICADO)
                );

    }
}
