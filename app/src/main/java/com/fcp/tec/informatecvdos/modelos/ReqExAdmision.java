package com.fcp.tec.informatecvdos.modelos;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReqExAdmision
{
    @SerializedName("idrequisitos_obtener_ficha")
    private String idRequisitos;
    @SerializedName("requisito_ficha")
    private String requisitoFicha;
    @SerializedName("idinscripcion")
    private String idInscripcion;
    @SerializedName("version")
    private String version;

    private int modificado;

    public ReqExAdmision(String idRequisitos, String requisitoFicha, String idInscripcion, String version, int modificado)
    {
        this.idRequisitos = idRequisitos;
        this.requisitoFicha = requisitoFicha;
        this.idInscripcion = idInscripcion;
        this.version = version;
        this.modificado = modificado;
    }

    //region Getter and Setters
    public String getIdRequisitos()
    {
        return idRequisitos;
    }

    public void setIdRequisitos(String idRequisitos)
    {
        this.idRequisitos = idRequisitos;
    }

    public String getRequisitoFicha()
    {
        return requisitoFicha;
    }

    public void setRequisitoFicha(String requisitoFicha)
    {
        this.requisitoFicha = requisitoFicha;
    }

    public String getIdInscripcion()
    {
        return idInscripcion;
    }

    public void setIdInscripcion(String idInscripcion)
    {
        this.idInscripcion = idInscripcion;
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
    public boolean compararExAdmisionCon(ReqExAdmision otro)
    {
        return idRequisitos.equals(otro.idRequisitos) &&
                requisitoFicha.equals(otro.requisitoFicha)&&
                idInscripcion.equals(otro.idInscripcion);
    }

    public int esMasReciente(ReqExAdmision match)
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
