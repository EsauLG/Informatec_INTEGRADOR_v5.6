package com.fcp.tec.informatecvdos.modelos;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PreguntasFrecuentes
{
    @SerializedName("idpregunta")
    private String idPregunta;
    @SerializedName("titulo_pregunta")
    private String tituloPregunta;
    @SerializedName("contenido_pregunta")
    private String contenidoPregunta;
    @SerializedName("version")
    private String version;

    //Variable únicamente para la tabla de la BD interna en android
    private int modificado;

    public PreguntasFrecuentes(String idPregunta, String tituloPregunta, String contenidoPregunta, String version, int modificado)
    {
        this.idPregunta = idPregunta;
        this.tituloPregunta = tituloPregunta;
        this.contenidoPregunta = contenidoPregunta;
        this.version = version;
        this.modificado = modificado;
    }

    //region Getters and Setters

    public String getIdPregunta()
    {
        return idPregunta;
    }

    public void setIdPregunta(String idPregunta)
    {
        this.idPregunta = idPregunta;
    }

    public String getTituloPregunta()
    {
        return tituloPregunta;
    }

    public void setTituloPregunta(String tituloPregunta)
    {
        this.tituloPregunta = tituloPregunta;
    }

    public String getContenidoPregunta()
    {
        return contenidoPregunta;
    }

    public void setContenidoPregunta(String contenidoPregunta)
    {
        this.contenidoPregunta = contenidoPregunta;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public int getModificado()
    {
        return modificado;
    }

    public void setModificado(int modificado)
    {
        this.modificado = modificado;
    }

    //endregion

    //region Comparaciones

    public boolean compararPreguntaCon(PreguntasFrecuentes otro)
    {
        return idPregunta.equals(otro.idPregunta) &&
                tituloPregunta.equals(otro.tituloPregunta) &&
                contenidoPregunta.equals(otro.contenidoPregunta);
    }

    public int esMasReciente(PreguntasFrecuentes match)
    {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try
        {
            Date fechaA = formato.parse(version);
            Date fechaB = formato.parse(match.version);

            return fechaA.compareTo(fechaB);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return 0;
    }
    //endregion

}
