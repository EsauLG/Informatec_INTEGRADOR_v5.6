package com.fcp.tec.informatecvdos.modelos;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logros
{
    @SerializedName("idlogros")
    private String idLogro;
    @SerializedName("titulo_logro")
    private String tituloLogro;
    @SerializedName("imagen_logro")
    private String imagenLogro;
    @SerializedName("descripcion_logro")
    private String descripcionLogro;
    @SerializedName("idunidad_academica")
    private String idUnidadAcademica;

    private Bitmap imagenLogroDeco;

    private String version;
    private int modificado;

    //Constructor para traer los datos en TEXTO desde la WEB
    public Logros(String idLogro, String tituloLogro, String imagenLogro, String descripcionLogro, String idUnidadAcademica, String version, int modificado)
    {
        this.idLogro = idLogro;
        this.tituloLogro = tituloLogro;
        this.imagenLogro = imagenLogro;
        this.descripcionLogro = descripcionLogro;
        this.idUnidadAcademica = idUnidadAcademica;
        this.version = version;
        this.modificado = modificado;
    }


    //Constructor para decodificar los datos dentro de la APP
    public Logros(String tituloLogro, String descripcionLogro, Bitmap imagenLogroDeco)
    {
        this.tituloLogro = tituloLogro;
        this.descripcionLogro = descripcionLogro;
        this.imagenLogroDeco = imagenLogroDeco;
    }

    //region Getters and Setters


    public Bitmap getImagenLogroDeco()
    {
        return imagenLogroDeco;
    }

    public void setImagenLogroDeco(Bitmap imagenLogroDeco)
    {
        this.imagenLogroDeco = imagenLogroDeco;
    }

    public String getIdLogro()
    {
        return idLogro;
    }

    public void setIdLogro(String idLogro)
    {
        this.idLogro = idLogro;
    }

    public String getTituloLogro()
    {
        return tituloLogro;
    }

    public void setTituloLogro(String tituloLogro)
    {
        this.tituloLogro = tituloLogro;
    }

    public String getImagenLogro()
    {
        return imagenLogro;
    }

    public void setImagenLogro(String imagenLogro)
    {
        this.imagenLogro = imagenLogro;
    }

    public String getDescripcionLogro()
    {
        return descripcionLogro;
    }

    public void setDescripcionLogro(String descripcionLogro)
    {
        this.descripcionLogro = descripcionLogro;
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


    //endregion

    //region Comparaciones

    public boolean compararCon(Logros otro)
    {
        return idLogro.equals(otro.idLogro) &&
                tituloLogro.equals(otro.tituloLogro)&&
                imagenLogro == otro.imagenLogro &&
                descripcionLogro.equals(otro.descripcionLogro)&&
                idUnidadAcademica.equals(otro.idUnidadAcademica);
    }

    public int esMasReciente(Logros match)
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
