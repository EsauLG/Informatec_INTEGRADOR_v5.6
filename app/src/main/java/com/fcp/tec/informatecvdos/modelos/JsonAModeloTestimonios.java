package com.fcp.tec.informatecvdos.modelos;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fcp.tec.informatecvdos.provider.ContractTestimoniales;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonAModeloTestimonios
{
    private static final String TAG = JsonAModeloTestimonios.class.getSimpleName();

    private interface ConsultaTestimonios
    {
        //Proyeccion para la consulta de Testimonios
        String[] PROYECCION =
                {
                        ContractTestimoniales.ControladorTestimoniales.ID_TESTIMONIALES,
                        ContractTestimoniales.ControladorTestimoniales.NOMBRE_PERSONA,
                        ContractTestimoniales.ControladorTestimoniales.TESTIMONIAL,
                        ContractTestimoniales.ControladorTestimoniales.IMAGEN,
                        ContractTestimoniales.ControladorTestimoniales.ID_CARRERA,
                        ContractTestimoniales.ControladorTestimoniales.VERSION,
                        ContractTestimoniales.ControladorTestimoniales.MODIFICADO
                };

        //Indices de columnas
        int ID_TESTIMONIALES = 0;
        int NOMBRE_PERSONA = 1;
        int TESTIMONIAL = 2;
        int IMAGEN = 3;
        int IDCARRERA = 4;
        int VERSION = 5;
        int MODIFICADO = 6;
    }

    //Se declara una variable que contendrá un "mapa" de los testimoniales remotos (las que acabamos de leer del JSON remoto)
    private HashMap<String, Testimoniales> testimonialRemoto = new HashMap<>();

    //Referencia al conversor JSON
    private Gson gson = new Gson();

    /**
     * permite tomar la respuesta JSON y almacenar sus datos en el objeto Testimoniales.java
     */
    public void procesar(JSONArray arrayJson)
    {
        //Añadir elementos recibidos al modelo POJO, en este caso al objeto Testimoniales
        //PARA testimonialActual de TIPO "Testimoniales" realiza--->: gson.fromJason(JSON de ENTRADA, Array de Objetos Testimoniales[])
        //esto realiza la conversión del JSONArray que encuentre y los convierte en objetos Testimoniales[]
        //la cantidad de objetos de tipo Testimoniales[] se tomará en base a lo que coincida con el  POJO en cuestión
        for (Testimoniales testimonialActual : gson.fromJson(arrayJson.toString(), Testimoniales[].class))
        {
            //Se añade UN id a cada testimonial encontrado en Testimoniales[]
            testimonialRemoto.put(testimonialActual.getIdTestimoniales(), testimonialActual);
        }


    }

    public void procesarOperaciones(ArrayList<ContentProviderOperation> cpOperaciones, ContentResolver cResolver)
    {



        //Revisar la BD SQLite que tiene la app de manera local, los datos ya incluidos
        Cursor c = cResolver.query(ContractTestimoniales.ControladorTestimoniales.URI_CONTENIDO, ConsultaTestimonios.PROYECCION, ContractTestimoniales.ControladorTestimoniales.INSERTADO + "=?", new String[]{"0"}, null);
        //Desde este cursor ya nos estamos refiriendo a datos LOCALES en la BD LOCAL
        if (c != null)
        {
            while (c.moveToNext())
            {
                //Convertir la fila del cursor en OBJETO Testimoniales.java
                Testimoniales filaActual = deCursorATestimonios(c);


                Testimoniales match = testimonialRemoto.get(filaActual.getIdTestimoniales());

                if (match != null)
                {
                    //Esta entrada EXISTE por lo que se remueve del "mapeado"
                    testimonialRemoto.remove(filaActual.getIdTestimoniales());

                    //Crear una URI de esta Beca
                    Uri updateUri = ContractTestimoniales.ControladorTestimoniales.construirUriTestimoniales(filaActual.getIdTestimoniales());

                    //Se aplica la resolución de conflictos, modificaciones de un mismo recurso
                    //tanto en el servidor como en la App. Quien tenga la versión más actual se
                    //toma como preponderante.
                    if (!match.compararCon(filaActual))
                    {
                        int flag = match.esMasReciente(filaActual);

                        if (flag > 0)
                        {
                            Log.i(TAG, "Programar actualizacion de Testimonial " + updateUri);
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
                    Uri deleteUri = ContractTestimoniales.ControladorTestimoniales.construirUriTestimoniales(filaActual.getIdTestimoniales());
                    Log.i(TAG, "Programar eliminacion de la instalacion");
                    cpOperaciones.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c.close();
        }

        //INSERTAR los items resultantes de manera LOCAL ya que se asume que no existen en el objeto
        //Testimoniales[]
        for (Testimoniales testimonial : testimonialRemoto.values())
        {
            Log.i(TAG, "Programar inserción de NUEVO Testimonio con ID = "+testimonial.getIdTestimoniales());
            cpOperaciones.add(construirOperacionInsert(testimonial));
        }

    }

    private ContentProviderOperation construirOperacionInsert(Testimoniales testimonial)
    {
        Log.i(TAG, "construirOperacionInsert: ");

        return ContentProviderOperation.newInsert(ContractTestimoniales.ControladorTestimoniales.URI_CONTENIDO)
                .withValue(ContractTestimoniales.ControladorTestimoniales.ID_TESTIMONIALES, testimonial.getIdTestimoniales())
                .withValue(ContractTestimoniales.ControladorTestimoniales.NOMBRE_PERSONA, testimonial.getNombrePersona())
                .withValue(ContractTestimoniales.ControladorTestimoniales.TESTIMONIAL, testimonial.getTestimonial())
                .withValue(ContractTestimoniales.ControladorTestimoniales.IMAGEN, testimonial.getImagenPersona())
                .withValue(ContractTestimoniales.ControladorTestimoniales.ID_CARRERA, testimonial.getIdCarrera())
                .withValue(ContractTestimoniales.ControladorTestimoniales.VERSION, testimonial.getVersion())
                .withValue(ContractTestimoniales.ControladorTestimoniales.INSERTADO, 0)
                .build();

    }

    private ContentProviderOperation construirOperacionUpdate(Testimoniales match, Uri updateUri)
    {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(ContractTestimoniales.ControladorTestimoniales.ID_TESTIMONIALES, match.getIdTestimoniales())
                .withValue(ContractTestimoniales.ControladorTestimoniales.NOMBRE_PERSONA, match.getNombrePersona())
                .withValue(ContractTestimoniales.ControladorTestimoniales.TESTIMONIAL, match.getTestimonial())
                .withValue(ContractTestimoniales.ControladorTestimoniales.IMAGEN, match.getImagenPersona())
                .withValue(ContractTestimoniales.ControladorTestimoniales.ID_CARRERA, match.getIdCarrera())
                .withValue(ContractTestimoniales.ControladorTestimoniales.VERSION, match.getVersion())
                .withValue(ContractTestimoniales.ControladorTestimoniales.INSERTADO, 0)
                .build();
    }

    /**
     * convierte lo que encuentra en un cursor hacia un objeto Testimoniales.java
     *
     * @param c El cursor que contiene el dato a convertir
     */
    private Testimoniales deCursorATestimonios(Cursor c)
    {

        return new Testimoniales
                (
                        c.getString(ConsultaTestimonios.ID_TESTIMONIALES),
                        c.getString(ConsultaTestimonios.NOMBRE_PERSONA),
                        c.getString(ConsultaTestimonios.TESTIMONIAL),
                        c.getString(ConsultaTestimonios.IMAGEN),
                        c.getString(ConsultaTestimonios.IDCARRERA),
                        c.getString(ConsultaTestimonios.VERSION),
                        c.getInt(ConsultaTestimonios.MODIFICADO)
                );

    }
}
