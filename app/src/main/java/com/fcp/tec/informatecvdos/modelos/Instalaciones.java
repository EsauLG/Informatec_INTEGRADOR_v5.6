package com.fcp.tec.informatecvdos.modelos;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Instalaciones
{
    @SerializedName("idinstalaciones")
    private String idInstalaciones;
    @SerializedName("nombre_instalacion")
    private String nombreInstalacion;
    @SerializedName("imagen_instalacion")
    private String imagenInstalacion;
    @SerializedName("descripcion_instalacion")
    private String descripcionInstalacion;
    @SerializedName("idunidad_academica")
    private String idUnidadAcademica;
    @SerializedName("version")
    private String version;

    private int modificado;
    private Bitmap imagenInstalacionDeco;

    //Constructor para traer los datos en TEXTO desde la WEB
    public Instalaciones(String idInstalaciones, String nombreInstalacion, String imagenInstalacion, String descripcionInstalacion, String idUnidadAcademica, String version, int modificado)
    {
        this.idInstalaciones = idInstalaciones;
        this.nombreInstalacion = nombreInstalacion;
        this.imagenInstalacion = imagenInstalacion;
        this.descripcionInstalacion = descripcionInstalacion;
        this.idUnidadAcademica = idUnidadAcademica;
        this.version = version;
        this.modificado = modificado;
    }

    //Constructor para decodificar los datos dentro de la APP
    public Instalaciones(String nombreInstalacion, String descripcionInstalacion, Bitmap imagenInstalacionDeco)
    {
        this.nombreInstalacion = nombreInstalacion;
        this.descripcionInstalacion = descripcionInstalacion;
        this.imagenInstalacionDeco = imagenInstalacionDeco;
    }

    //region Getters and Setters

    //Getter de las imagenes locales almacenadas en Base 64
    public Bitmap getImagenInstalacionDeco()
    {
        return imagenInstalacionDeco;
    }

    public void setImagenInstalacionDeco(Bitmap imagenInstalacionDeco)
    {
        this.imagenInstalacionDeco = imagenInstalacionDeco;
    }
    //Getters y Setters de las imagenes del servidor de tipo String

    public String getIdInstalaciones()
    {
        return idInstalaciones;
    }

    public void setIdInstalaciones(String idInstalaciones)
    {
        this.idInstalaciones = idInstalaciones;
    }

    public String getNombreInstalacion()
    {
        return nombreInstalacion;
    }

    public void setNombreInstalacion(String nombreInstalacion)
    {
        this.nombreInstalacion = nombreInstalacion;
    }

    public String getImagenInstalacion()
    {
        return imagenInstalacion;
    }

    public void setImagenInstalacion(String imagenInstalacion)
    {
        this.imagenInstalacion = imagenInstalacion;
    }

    public String getDescripcionInstalacion()
    {
        return descripcionInstalacion;
    }

    public void setDescripcionInstalacion(String descripcionInstalacion)
    {
        this.descripcionInstalacion = descripcionInstalacion;
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

    public boolean compararCon(Instalaciones otro)
    {
        return idInstalaciones.equals(otro.idInstalaciones) &&
                nombreInstalacion.equals(otro.nombreInstalacion)&&
                imagenInstalacion.equals(otro.imagenInstalacion) &&
                descripcionInstalacion.equals(otro.descripcionInstalacion)&&
                idUnidadAcademica.equals(otro.idUnidadAcademica);
    }

    public int esMasReciente(Instalaciones match)
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