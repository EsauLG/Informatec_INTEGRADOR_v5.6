package com.fcp.tec.informatecvdos.modelos;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PreguntasTest
{
    @SerializedName("id_pregunta")
    private String idPregunta;
    @SerializedName("pregunta")
    private String pregunta;
    @SerializedName("opcion")
    private String opcion;
    @SerializedName("version")
    private String version;

    private int modificado;

    public PreguntasTest(String idPregunta, String pregunta, String opcion, String version, int modificado)
    {
        this.idPregunta = idPregunta;
        this.pregunta = pregunta;
        this.opcion = opcion;
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

    public String getPregunta()
    {
        return pregunta;
    }

    public void setPregunta(String pregunta)
    {
        this.pregunta = pregunta;
    }

    public String getOpcion()
    {
        return opcion;
    }

    public void setOpcion(String opcion)
    {
        this.opcion = opcion;
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


    public boolean compararCon(PreguntasTest otro)
    {
        return idPregunta.equals(otro.idPregunta) &&
                pregunta.equals(otro.pregunta) &&
                opcion.equals(otro.opcion);
    }

    public int esMasReciente(PreguntasTest match)
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
