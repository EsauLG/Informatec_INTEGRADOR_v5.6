package com.fcp.tec.informatecvdos.modelos;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fcp.tec.informatecvdos.provider.ContractCalendario;
import com.fcp.tec.informatecvdos.provider.ContractCarreras;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonAModeloCarreras
{

    public static final String TAG = JsonAModeloCarreras.class.getSimpleName();

    //Se declara una variable que contendrá un "mapa" de las carreras remotas (aqui se almacena lo que acabamos de leer del JSON remoto)
    private HashMap<String, Carreras> carrerasRemotas = new HashMap<>();

    private interface ConsultaCarreras
    {
        //Proyeccion para la consulta de las Carreras (Se usará en el método Query)
        String[] PROYECCION =
                {
                        ContractCarreras.ControladorCarreras.IDCARRERA,
                        ContractCarreras.ControladorCarreras.NOMBRE_CARRERA,
                        ContractCarreras.ControladorCarreras.OBJETIVO_CARRERA,
                        ContractCarreras.ControladorCarreras.VIDEO_CARRERA,
                        ContractCarreras.ControladorCarreras.PLAN_ESTUDIO,
                        ContractCarreras.ControladorCarreras.ESPECIALIDAD,
                        ContractCalendario.ControladorCalendario.VERSION,
                        ContractCalendario.ControladorCalendario.MODIFICADO
                };

        //Indices de columnas
        int IDCARRERA = 0;
        int NOMBRE_CARRERA = 1;
        int OBJETIVO_CARRERA = 2;
        int VIDEO_CARRERA = 3;
        int PLAN_ESTUDIO = 4;
        int ESPECIALIDAD = 5;
        int VERSION = 6;
        int MODIFICADO = 7;
    }


    //Referencia al conversor JSON
    private Gson gson = new Gson();

    /**
     * permite tomar la respuesta JSON y almacenar sus datos en el objeto Calendario.java
     */
    public void procesar(JSONArray arrayJson)
    {

        for (Carreras carreraActual : gson.fromJson(arrayJson.toString(), Carreras[].class))
        {
            //Se añade UN id a cada fila de la carrera encontrada en Carreras[]
            carrerasRemotas.put(carreraActual.getIdCarrera(), carreraActual);
        }


    }

    public void procesarOperaciones(ArrayList<ContentProviderOperation> cpOperaciones, ContentResolver cResolver)
    {


        //Revisar la BD SQLite que tiene la app de manera local, los datos ya incluidos
        Cursor c = cResolver.query(ContractCarreras.ControladorCarreras.URI_CONTENIDO, JsonAModeloCarreras.ConsultaCarreras.PROYECCION, ContractCarreras.ControladorCarreras.INSERTADO + "=?", new String[]{"0"}, null);
        //Desde este cursor ya nos estamos refiriendo a datos LOCALES en la BD LOCAL
        if (c != null)
        {
            while (c.moveToNext())
            {
                //Convertir la fila del cursor en OBJETO Carreras.java
                Carreras filaActual = deCursorACarreras(c);

                //Buscar si la Carrera actual se encuentra en el mapa de MAPA CARRERAS
                Carreras match = carrerasRemotas.get(filaActual.getIdCarrera());

                if (match != null)
                {
                    //Esta entrada EXISTE por lo que se remueve del "mapeado"
                    carrerasRemotas.remove(filaActual.getIdCarrera());

                    //Crear una URI de esta CARRERA
                    Uri updateUri = ContractCarreras.ControladorCarreras.construirUriCarrera(filaActual.getIdCarrera());

                    //Se aplica la resolución de conflictos, modificaciones de un mismo recurso
                    //tanto en el servidor como en la App. Quien tenga la versión más actual se
                    //toma como preponderante.
                    if (!match.compararCarreraCon(filaActual))
                    {
                        int flag = match.esMasReciente(filaActual);

                        if (flag > 0)
                        {
                            Log.i(TAG, "Programar actualizacion de Carrera " + updateUri);
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
                    Uri deleteUri = ContractCarreras.ControladorCarreras.construirUriCarrera(filaActual.getIdCarrera());
                    Log.i(TAG, "Programar eliminacion de la carrera");
                    cpOperaciones.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c.close();
        }

        //INSERTAR los items resultantes de manera LOCAL ya que se asume que no existen en el objeto
        //Calendario[]
        for (Carreras carreras : carrerasRemotas.values())
        {
            Log.i(TAG, "Programar inserción de NUEVA carrera con ID = " + carreras.getIdCarrera());
            cpOperaciones.add(construirOperacionInsert(carreras));
        }

    }

    //region Operaciones de ContenProvider


    private ContentProviderOperation construirOperacionInsert(Carreras carreras)
    {
        Log.i(TAG, "construirOperacionInsert: ");

        return ContentProviderOperation.newInsert(ContractCarreras.ControladorCarreras.URI_CONTENIDO)
                .withValue(ContractCarreras.ControladorCarreras.IDCARRERA, carreras.getIdCarrera())
                .withValue(ContractCarreras.ControladorCarreras.NOMBRE_CARRERA, carreras.getNombreCarrera())
                .withValue(ContractCarreras.ControladorCarreras.OBJETIVO_CARRERA, carreras.getObjetivoCarrera())
                .withValue(ContractCarreras.ControladorCarreras.VIDEO_CARRERA, carreras.getObjetivoCarrera())
                .withValue(ContractCarreras.ControladorCarreras.PLAN_ESTUDIO, carreras.getPlanEstudio())
                .withValue(ContractCarreras.ControladorCarreras.ESPECIALIDAD, carreras.getEspecialidad())
                .withValue(ContractCarreras.ControladorCarreras.VERSION, carreras.getVersion())
                .withValue(ContractCarreras.ControladorCarreras.INSERTADO, 0)
                .build();

    }

    private ContentProviderOperation construirOperacionUpdate(Carreras match, Uri updateUri)
    {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(ContractCarreras.ControladorCarreras.IDCARRERA, match.getIdCarrera())
                .withValue(ContractCarreras.ControladorCarreras.NOMBRE_CARRERA, match.getNombreCarrera())
                .withValue(ContractCarreras.ControladorCarreras.OBJETIVO_CARRERA, match.getObjetivoCarrera())
                .withValue(ContractCarreras.ControladorCarreras.VIDEO_CARRERA, match.getVideoCarrera())
                .withValue(ContractCarreras.ControladorCarreras.PLAN_ESTUDIO, match.getPlanEstudio())
                .withValue(ContractCarreras.ControladorCarreras.ESPECIALIDAD, match.getPlanEstudio())
                .withValue(ContractCarreras.ControladorCarreras.VERSION, match.getVersion())
                .withValue(ContractCalendario.ControladorCalendario.MODIFICADO, match.getModificado())
                .build();
    }
    //endregion


    /**
     * convierte lo que encuentra en un cursor hacia un objeto Calendario.java
     *
     * @param c El cursor que contiene el dato a convertir
     */
    private Carreras deCursorACarreras(Cursor c)
    {

        return new Carreras
                (
                        c.getString(ConsultaCarreras.IDCARRERA),
                        c.getString(ConsultaCarreras.NOMBRE_CARRERA),
                        c.getString(ConsultaCarreras.OBJETIVO_CARRERA),
                        c.getString(ConsultaCarreras.VIDEO_CARRERA),
                        c.getString(ConsultaCarreras.PLAN_ESTUDIO),
                        c.getString(ConsultaCarreras.ESPECIALIDAD),
                        c.getString(ConsultaCarreras.VERSION),
                        c.getInt(ConsultaCarreras.MODIFICADO)
                );

    }

}
