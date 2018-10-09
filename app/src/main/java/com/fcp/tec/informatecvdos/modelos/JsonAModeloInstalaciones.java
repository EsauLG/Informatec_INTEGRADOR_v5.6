package com.fcp.tec.informatecvdos.modelos;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fcp.tec.informatecvdos.provider.ContractInstalaciones;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonAModeloInstalaciones
{
    private static final String TAG = JsonAModeloInstalaciones.class.getSimpleName();

    private interface ConsultaInstalaciones
    {
        //Proyeccion para la consulta de Instalaciones
        String[] PROYECCION =
                {
                        ContractInstalaciones.ControladorInstalaciones.ID_INSTALACIONES,
                        ContractInstalaciones.ControladorInstalaciones.NOMBRE,
                        ContractInstalaciones.ControladorInstalaciones.IMAGEN,
                        ContractInstalaciones.ControladorInstalaciones.DESCRIPCION,
                        ContractInstalaciones.ControladorInstalaciones.ID_UNIDAD_ACADEMICA,
                        ContractInstalaciones.ControladorInstalaciones.VERSION,
                        ContractInstalaciones.ControladorInstalaciones.MODIFICADO
                };

        //Indices de columnas
        int ID_INSTALACIONES = 0;
        int NOMBRE = 1;
        int IMAGEN = 2;
        int DESCRIPCION = 3;
        int ID_UNIDAD_ACADEMICA = 4;
        int VERSION = 5;
        int MODIFICADO = 6;
    }

    //Se declara una variable que contendrá un "mapa" de las Instalaciones remotas (las que acabamos de leer del JSON remoto)
    private HashMap<String, Instalaciones> instalacioRemota = new HashMap<>();

    //Referencia al conversor JSON
    private Gson gson = new Gson();

    /**
     * permite tomar la respuesta JSON y almacenar sus datos en el objeto Instalaciones.java
     */
    public void procesar(JSONArray arrayJson)
    {
        //Añadir elementos recibidos al modelo POJO, en este caso al objeto Instalaciones
        //PARA instalacionActual de TIPO "Instalaciones" realiza--->: gson.fromJason(JSON de ENTRADA, Array de Objetos Instalaciones[])
        //esto realiza la conversión del JSONArray que encuentre y los convierte en objetos Instalaciones[]
        //la cantidad de objetos de tipo Instalaciones[] se tomará en base a lo que coincida con el  POJO en cuestión
        for (Instalaciones instalacionActual : gson.fromJson(arrayJson.toString(), Instalaciones[].class))
        {
            //Se añade UN id a cada instalacion encontrada en Instalaciones[]
            instalacioRemota.put(instalacionActual.getIdInstalaciones(), instalacionActual);
        }


    }

    public void procesarOperaciones(ArrayList<ContentProviderOperation> cpOperaciones, ContentResolver cResolver)
    {



        //Revisar la BD SQLite que tiene la app de manera local, los datos ya incluidos
        Cursor c = cResolver.query(ContractInstalaciones.ControladorInstalaciones.URI_CONTENIDO, ConsultaInstalaciones.PROYECCION, ContractInstalaciones.ControladorInstalaciones.INSERTADO + "=?", new String[]{"0"}, null);
        //Desde este cursor ya nos estamos refiriendo a datos LOCALES en la BD LOCAL
        if (c != null)
        {
            while (c.moveToNext())
            {
                //Convertir la fila del cursor en OBJETO Instalaciones.java
                Instalaciones filaActual = deCursorAInstalaciones(c);


                Instalaciones match = instalacioRemota.get(filaActual.getIdInstalaciones());

                if (match != null)
                {
                    //Esta entrada EXISTE por lo que se remueve del "mapeado"
                    instalacioRemota.remove(filaActual.getIdInstalaciones());

                    //Crear una URI de esta Beca
                    Uri updateUri = ContractInstalaciones.ControladorInstalaciones.construirUriInstalaciones(filaActual.getIdInstalaciones());

                    //Se aplica la resolución de conflictos, modificaciones de un mismo recurso
                    //tanto en el servidor como en la App. Quien tenga la versión más actual se
                    //toma como preponderante.
                    if (!match.compararCon(filaActual))
                    {
                        int flag = match.esMasReciente(filaActual);

                        if (flag > 0)
                        {
                            Log.i(TAG, "Programar actualizacion de Instalacion " + updateUri);
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
                    Uri deleteUri = ContractInstalaciones.ControladorInstalaciones.construirUriInstalaciones(filaActual.getIdInstalaciones());
                    Log.i(TAG, "Programar eliminacion de la instalacion");
                    cpOperaciones.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c.close();
        }

        //INSERTAR los items resultantes de manera LOCAL ya que se asume que no existen en el objeto
        //Instalaciones[]
        for (Instalaciones instalaciones : instalacioRemota.values())
        {
            Log.i(TAG, "Programar inserción de NUEVA instalacion con ID = "+instalaciones.getIdInstalaciones());
            cpOperaciones.add(construirOperacionInsert(instalaciones));
        }

    }

    private ContentProviderOperation construirOperacionInsert(Instalaciones instalaciones)
    {
        Log.i(TAG, "construirOperacionInsert: ");

        return ContentProviderOperation.newInsert(ContractInstalaciones.ControladorInstalaciones.URI_CONTENIDO)
                .withValue(ContractInstalaciones.ControladorInstalaciones.ID_INSTALACIONES, instalaciones.getIdInstalaciones())
                .withValue(ContractInstalaciones.ControladorInstalaciones.NOMBRE, instalaciones.getNombreInstalacion())
                .withValue(ContractInstalaciones.ControladorInstalaciones.IMAGEN, instalaciones.getImagenInstalacion())
                .withValue(ContractInstalaciones.ControladorInstalaciones.DESCRIPCION, instalaciones.getDescripcionInstalacion())
                .withValue(ContractInstalaciones.ControladorInstalaciones.ID_UNIDAD_ACADEMICA, instalaciones.getIdUnidadAcademica())
                .withValue(ContractInstalaciones.ControladorInstalaciones.VERSION, instalaciones.getVersion())
                .withValue(ContractInstalaciones.ControladorInstalaciones.INSERTADO, 0)
                .build();

    }

    private ContentProviderOperation construirOperacionUpdate(Instalaciones match, Uri updateUri)
    {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(ContractInstalaciones.ControladorInstalaciones.ID_INSTALACIONES, match.getIdInstalaciones())
                .withValue(ContractInstalaciones.ControladorInstalaciones.NOMBRE, match.getNombreInstalacion())
                .withValue(ContractInstalaciones.ControladorInstalaciones.IMAGEN, match.getImagenInstalacion())
                .withValue(ContractInstalaciones.ControladorInstalaciones.DESCRIPCION, match.getDescripcionInstalacion())
                .withValue(ContractInstalaciones.ControladorInstalaciones.ID_UNIDAD_ACADEMICA, match.getIdUnidadAcademica())
                .withValue(ContractInstalaciones.ControladorInstalaciones.VERSION, match.getVersion())
                .withValue(ContractInstalaciones.ControladorInstalaciones.INSERTADO, 0)
                .build();
    }

    /**
     * convierte lo que encuentra en un cursor hacia un objeto Instalaciones.java
     *
     * @param c El cursor que contiene el dato a convertir
     */
    private Instalaciones deCursorAInstalaciones(Cursor c)
    {

        return new Instalaciones
                (
                        c.getString(ConsultaInstalaciones.ID_INSTALACIONES),
                        c.getString(ConsultaInstalaciones.NOMBRE),
                        c.getString(ConsultaInstalaciones.IMAGEN),
                        c.getString(ConsultaInstalaciones.DESCRIPCION),
                        c.getString(ConsultaInstalaciones.ID_UNIDAD_ACADEMICA),
                        c.getString(ConsultaInstalaciones.VERSION),
                        c.getInt(ConsultaInstalaciones.MODIFICADO)
                );

    }
}
