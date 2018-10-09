package com.fcp.tec.informatecvdos.modelos;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fcp.tec.informatecvdos.provider.ContractLogros;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonAModeloLogros
{

    private static final String TAG = JsonAModeloLogros.class.getSimpleName();

    private interface ConsultaLogros
    {
        //Proyeccion para la consulta de Logros
        String[] PROYECCION =
                {
                        ContractLogros.ControladorLogros.ID_LOGROS,
                        ContractLogros.ControladorLogros.TITULO,
                        ContractLogros.ControladorLogros.IMAGEN,
                        ContractLogros.ControladorLogros.DESCRIPCION,
                        ContractLogros.ControladorLogros.ID_UNIDAD_ACADEMICA,
                        ContractLogros.ControladorLogros.VERSION,
                        ContractLogros.ControladorLogros.MODIFICADO
                };

        //Indices de columnas
        int ID_LOGROS = 0;
        int TITULO = 1;
        int IMAGEN = 2;
        int DESCRIPCION = 3;
        int ID_UNIDAD_ACADEMICA = 4;
        int VERSION = 5;
        int MODIFICADO = 6;
    }

    //Se declara una variable que contendrá un "mapa" de las becas remotas (las que acabamos de leer del JSON remoto)
    private HashMap<String, Logros> logroRemoto = new HashMap<>();

    //Referencia al conversor JSON
    private Gson gson = new Gson();

    /**
     * permite tomar la respuesta JSON y almacenar sus datos en el objeto Logros.java
     */
    public void procesar(JSONArray arrayJson)
    {
        //Añadir elementos recibidos al modelo POJO, en este caso al objeto Logros
        //PARA logroActual de TIPO "Logros" realiza--->: gson.fromJason(JSON de ENTRADA, Array de Objetos Logros[])
        //esto realiza la conversión del JSONArray que encuentre y los convierte en objetos Logros[]
        //la cantidad de objetos de tipo Logros[] se tomará en base a lo que coincida con el  POJO en cuestión
        for (Logros logroActual : gson.fromJson(arrayJson.toString(), Logros[].class))
        {
            //Se añade UN id a cada beca encontrada en Becass[]
            logroRemoto.put(logroActual.getIdLogro(), logroActual);
        }


    }

    public void procesarOperaciones(ArrayList<ContentProviderOperation> cpOperaciones, ContentResolver cResolver)
    {


        //Revisar la BD SQLite que tiene la app de manera local, los datos ya incluidos
        Cursor c = cResolver.query(ContractLogros.ControladorLogros.URI_CONTENIDO, ConsultaLogros.PROYECCION, ContractLogros.ControladorLogros.INSERTADO + "=?", new String[]{"0"}, null);
        //Desde este cursor ya nos estamos refiriendo a datos LOCALES en la BD LOCAL
        if (c != null)
        {
            while (c.moveToNext())
            {
                //Convertir la fila del cursor en OBJETO Logros.java
                Logros filaActual = deCursorALogro(c);


                Logros match = logroRemoto.get(filaActual.getIdLogro());

                if (match != null)
                {
                    //Esta entrada EXISTE por lo que se remueve del "mapeado"
                    logroRemoto.remove(filaActual.getIdLogro());

                    //Crear una URI de esta Beca
                    Uri updateUri = ContractLogros.ControladorLogros.construirUriLogros(filaActual.getIdLogro());

                    //Se aplica la resolución de conflictos, modificaciones de un mismo recurso
                    //tanto en el servidor como en la App. Quien tenga la versión más actual se
                    //toma como preponderante.
                    if (!match.compararCon(filaActual))
                    {
                        int flag = match.esMasReciente(filaActual);

                        if (flag > 0)
                        {
                            Log.i(TAG, "Programar actualizacion de Logro " + updateUri);
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
                    Uri deleteUri = ContractLogros.ControladorLogros.construirUriLogros(filaActual.getIdLogro());
                    Log.i(TAG, "Programar eliminacion de Logro");
                    cpOperaciones.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c.close();
        }

        //INSERTAR los items resultantes de manera LOCAL ya que se asume que no existen en el objeto
        //Logros[]
        for (Logros logros : logroRemoto.values())
        {
            Log.i(TAG, "Programar inserción de NUEVA instalacion con ID = " + logros.getIdLogro());
            cpOperaciones.add(construirOperacionInsert(logros));
        }

    }

    private ContentProviderOperation construirOperacionInsert(Logros logro)
    {
        Log.i(TAG, "construirOperacionInsert: ");

        return ContentProviderOperation.newInsert(ContractLogros.ControladorLogros.URI_CONTENIDO)
                .withValue(ContractLogros.ControladorLogros.ID_LOGROS, logro.getIdLogro())
                .withValue(ContractLogros.ControladorLogros.TITULO, logro.getTituloLogro())
                .withValue(ContractLogros.ControladorLogros.IMAGEN, logro.getImagenLogro())
                .withValue(ContractLogros.ControladorLogros.DESCRIPCION, logro.getDescripcionLogro())
                .withValue(ContractLogros.ControladorLogros.ID_UNIDAD_ACADEMICA, logro.getIdUnidadAcademica())
                .withValue(ContractLogros.ControladorLogros.VERSION, logro.getVersion())
                .withValue(ContractLogros.ControladorLogros.INSERTADO, 0)
                .build();

    }

    private ContentProviderOperation construirOperacionUpdate(Logros match, Uri updateUri)
    {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(ContractLogros.ControladorLogros.ID_LOGROS, match.getIdLogro())
                .withValue(ContractLogros.ControladorLogros.TITULO, match.getTituloLogro())
                .withValue(ContractLogros.ControladorLogros.IMAGEN, match.getImagenLogro())
                .withValue(ContractLogros.ControladorLogros.DESCRIPCION, match.getDescripcionLogro())
                .withValue(ContractLogros.ControladorLogros.ID_UNIDAD_ACADEMICA, match.getIdUnidadAcademica())
                .withValue(ContractLogros.ControladorLogros.VERSION, match.getVersion())
                .withValue(ContractLogros.ControladorLogros.INSERTADO, 0)
                .build();
    }

    /**
     * convierte lo que encuentra en un cursor hacia un objeto Logros.java
     *
     * @param c El cursor que contiene el dato a convertir
     */
    private Logros deCursorALogro(Cursor c)
    {

        return new Logros
                (
                        c.getString(ConsultaLogros.ID_LOGROS),
                        c.getString(ConsultaLogros.TITULO),
                        c.getString(ConsultaLogros.IMAGEN),
                        c.getString(ConsultaLogros.DESCRIPCION),
                        c.getString(ConsultaLogros.ID_UNIDAD_ACADEMICA),
                        c.getString(ConsultaLogros.VERSION),
                        c.getInt(ConsultaLogros.MODIFICADO)
                );

    }
}
