package com.fcp.tec.informatecvdos.modelos;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnidadAcademica
{
    @SerializedName("idunidad_academica")
    private String idUnidadAcademica;
    @SerializedName("nombre_unidad")
    private String nombreUnidad;
    @SerializedName("direccion_unidad")
    private String direccionUnidad;
    @SerializedName("correo_unidad")
    private String correoUnidad;
    @SerializedName("telefono_unidad")
    private String telUnidad;
    @SerializedName("horarios_unidad")
    private String horarioUnidad;
    @SerializedName("video_general")
    private String videoGeneral;
    @SerializedName("idinscripcion")
    private String idInscripcion;
    @SerializedName("version")
    private String version;
    private int modificado;

    public UnidadAcademica(String idUnidadAcademica, String nombreUnidad, String direccionUnidad, String correoUnidad, String telUnidad, String horarioUnidad, String videoGeneral, String idInscripcion, String version, int modificado)
    {
        this.idUnidadAcademica = idUnidadAcademica;
        this.nombreUnidad = nombreUnidad;
        this.direccionUnidad = direccionUnidad;
        this.correoUnidad = correoUnidad;
        this.telUnidad = telUnidad;
        this.horarioUnidad = horarioUnidad;
        this.videoGeneral = videoGeneral;
        this.idInscripcion = idInscripcion;
        this.version = version;
        this.modificado = modificado;
    }

    //region Getters and Setters

    public String getIdUnidadAcademica()
    {
        return idUnidadAcademica;
    }

    public void setIdUnidadAcademica(String idUnidadAcademica)
    {
        this.idUnidadAcademica = idUnidadAcademica;
    }

    public String getNombreUnidad()
    {
        return nombreUnidad;
    }

    public void setNombreUnidad(String nombreUnidad)
    {
        this.nombreUnidad = nombreUnidad;
    }

    public String getDireccionUnidad()
    {
        return direccionUnidad;
    }

    public void setDireccionUnidad(String direccionUnidad)
    {
        this.direccionUnidad = direccionUnidad;
    }

    public String getCorreoUnidad()
    {
        return correoUnidad;
    }

    public void setCorreoUnidad(String correoUnidad)
    {
        this.correoUnidad = correoUnidad;
    }

    public String getTelUnidad()
    {
        return telUnidad;
    }

    public void setTelUnidad(String telUnidad)
    {
        this.telUnidad = telUnidad;
    }

    public String getHorarioUnidad()
    {
        return horarioUnidad;
    }

    public void setHorarioUnidad(String horarioUnidad)
    {
        this.horarioUnidad = horarioUnidad;
    }

    public String getVideoGeneral()
    {
        return videoGeneral;
    }

    public void setVideoGeneral(String videoGeneral)
    {
        this.videoGeneral = videoGeneral;
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
    public boolean compararCon(UnidadAcademica otro)
    {
        return idUnidadAcademica.equals(otro.idUnidadAcademica) &&
                nombreUnidad.equals(otro.nombreUnidad)&&
                direccionUnidad.equals(otro.direccionUnidad)&&
                correoUnidad.equals(otro.correoUnidad)&&
                telUnidad.equals(otro.telUnidad)&&
                horarioUnidad.equals(otro.horarioUnidad)&&
                videoGeneral.equals(otro.videoGeneral)&&
                idInscripcion.equals(otro.idInscripcion);
    }

    public int esMasReciente(UnidadAcademica match)
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
