package com.fcp.tec.informatecvdos.modelos;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReqInscripcion
{
    @SerializedName("idrequisitos_inscripcion")
    private String idRequisito;
    @SerializedName("requisitos_inscripcion")
    private String requisitoInscripcion;
    @SerializedName("version")
    private String version;

    private int modificado;

    public ReqInscripcion(String idRequisito, String requisitoInscripcion, String version, int modificado)
    {
        this.idRequisito = idRequisito;
        this.requisitoInscripcion = requisitoInscripcion;
        this.version = version;
        this.modificado = modificado;
    }

    //region Getters and Setters

    public String getIdRequisito()
    {
        return idRequisito;
    }

    public void setIdRequisito(String idRequisito)
    {
        this.idRequisito = idRequisito;
    }

    public String getRequisitoInscripcion()
    {
        return requisitoInscripcion;
    }

    public void setRequisitoInscripcion(String requisitoInscripcion)
    {
        this.requisitoInscripcion = requisitoInscripcion;
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
    public boolean compararReqInscripcion(ReqInscripcion otro)
    {
        return idRequisito.equals(otro.idRequisito) &&
                requisitoInscripcion.equals(otro.requisitoInscripcion);
    }

    public int esMasReciente(ReqInscripcion match)
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
