package com.fcp.tec.informatecvdos.modelos;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Becas
{
    @SerializedName("idbecas")
    private String idBeca;
    @SerializedName("nombre_beca")
    private String nombreBeca;
    @SerializedName("descripcion_beca")
    private String descripcionBeca;
    @SerializedName("tipo_beca")
    private String tipoBeca;
    @SerializedName("idunidad_academica")
    private String idUnidadAcademica;
    @SerializedName("version")
    private String version;

    private int modificado;

    public Becas(String idBeca, String nombreBeca, String descripcionBeca, String tipoBeca, String idUnidadAcademica, String version, int modificado)
    {
        this.idBeca = idBeca;
        this.nombreBeca = nombreBeca;
        this.descripcionBeca = descripcionBeca;
        this.tipoBeca = tipoBeca;
        this.idUnidadAcademica = idUnidadAcademica;
        this.version = version;
        this.modificado = modificado;
    }

    public Becas()
    {

    }

    //region Getters and Setters

    public String getIdBeca()
    {
        return idBeca;
    }

    public void setIdBeca(String idBeca)
    {
        this.idBeca = idBeca;
    }

    public String getNombreBeca()
    {
        return nombreBeca;
    }

    public void setNombreBeca(String nombreBeca)
    {
        this.nombreBeca = nombreBeca;
    }

    public String getDescripcionBeca()
    {
        return descripcionBeca;
    }

    public void setDescripcionBeca(String descripcionBeca)
    {
        this.descripcionBeca = descripcionBeca;
    }

    public String getTipoBeca()
    {
        return tipoBeca;
    }

    public void setTipoBeca(String tipoBeca)
    {
        this.tipoBeca = tipoBeca;
    }

    public String getIdUnidadAcademica()
    {
        return idUnidadAcademica;
    }

    public void setIdUnidadAcademica(String idUnidadAcademica)
    {
        this.idUnidadAcademica = idUnidadAcademica;
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

    public boolean compararCon(Becas otro)
    {
        return idBeca.equals(otro.idBeca) &&
                nombreBeca.equals(otro.nombreBeca) &&
                descripcionBeca.equals(otro.descripcionBeca) &&
                tipoBeca.equals(otro.descripcionBeca) &&
                idUnidadAcademica.equals(otro.idUnidadAcademica);
    }

    public int esMasReciente(Becas match)
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
