package com.fcp.tec.informatecvdos.modelos;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Carreras
{
    @SerializedName("idcarrera")
    private String idCarrera;
    @SerializedName("nombre_carrera")
    private String nombreCarrera;
    @SerializedName("objetivo_carrera")
    private String objetivoCarrera;
    @SerializedName("video_carrera")
    private String videoCarrera;
    @SerializedName("plan_estudio")
    private String planEstudio;
    @SerializedName("especialidad_carrera")
    private String especialidad;
    @SerializedName("version")
    private String version;

    //Variable únicamente para la tabla de la BD interna en android
    private int modificado;

    public Carreras(String idCarrera, String nombreCarrera, String objetivoCarrera, String videoCarrera, String planEstudio, String especialidad, String version, int modificado)
    {
        this.idCarrera = idCarrera;
        this.nombreCarrera = nombreCarrera;
        this.objetivoCarrera = objetivoCarrera;
        this.videoCarrera = videoCarrera;
        this.planEstudio = planEstudio;
        this.especialidad = especialidad;
        this.version = version;
        this.modificado = modificado;
    }

    //region Getters and Setters

    public String getIdCarrera()
    {
        return idCarrera;
    }

    public void setIdCarrera(String idCarrera)
    {
        this.idCarrera = idCarrera;
    }

    public String getNombreCarrera()
    {
        return nombreCarrera;
    }

    public void setNombreCarrera(String nombreCarrera)
    {
        this.nombreCarrera = nombreCarrera;
    }

    public String getObjetivoCarrera()
    {
        return objetivoCarrera;
    }

    public void setObjetivoCarrera(String objetivoCarrera)
    {
        this.objetivoCarrera = objetivoCarrera;
    }

    public String getVideoCarrera()
    {
        return videoCarrera;
    }

    public void setVideoCarrera(String videoCarrera)
    {
        this.videoCarrera = videoCarrera;
    }

    public String getPlanEstudio()
    {
        return planEstudio;
    }

    public void setPlanEstudio(String planEstudio)
    {
        this.planEstudio = planEstudio;
    }

    public String getEspecialidad()
    {
        return especialidad;
    }

    public void setEspecialidad(String especialidad)
    {
        this.especialidad = especialidad;
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

    public boolean compararCarreraCon(Carreras otro)
    {
        return idCarrera.equals(otro.idCarrera) &&
                nombreCarrera.equals(otro.nombreCarrera)&&
                objetivoCarrera.equals(otro.objetivoCarrera)&&
                videoCarrera.equals(otro.videoCarrera)&&
                planEstudio.equals(otro.planEstudio)&&
                especialidad.equals(otro.especialidad);
    }

    public int esMasReciente(Carreras match)
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
