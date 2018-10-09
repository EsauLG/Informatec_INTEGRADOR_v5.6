package com.fcp.tec.informatecvdos.modelos;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Extraescolar
{
    @SerializedName("idextraescolares")
    private String idExtraescolares;
    @SerializedName("nombre_extra")
    private String nombreExtra;
    @SerializedName("tipo_extra")
    private String tipoExtra;
    @SerializedName("opcion_seleccion")
    private String opcionSeleccion;
    @SerializedName("imagen_extraescolar")
    private String imagenExtraEscolar;
    private String version;
    private int modificado;

    public Extraescolar(String idExtraescolares, String nombreExtra, String tipoExtra, String opcionSeleccion, String imagenExtraEscolar, String version, int modificado)
    {
        this.idExtraescolares = idExtraescolares;
        this.nombreExtra = nombreExtra;
        this.tipoExtra = tipoExtra;
        this.opcionSeleccion = opcionSeleccion;
        this.imagenExtraEscolar = imagenExtraEscolar;
        this.version = version;
        this.modificado = modificado;
    }

    //region Getterns and Setters
    public String getIdExtraescolares()
    {
        return idExtraescolares;
    }

    public void setIdExtraescolares(String idExtraescolares)
    {
        this.idExtraescolares = idExtraescolares;
    }

    public String getNombreExtra()
    {
        return nombreExtra;
    }

    public void setNombreExtra(String nombreExtra)
    {
        this.nombreExtra = nombreExtra;
    }

    public String getTipoExtra()
    {
        return tipoExtra;
    }

    public void setTipoExtra(String tipoExtra)
    {
        this.tipoExtra = tipoExtra;
    }

    public String getOpcionSeleccion()
    {
        return opcionSeleccion;
    }

    public void setOpcionSeleccion(String opcionSeleccion)
    {
        this.opcionSeleccion = opcionSeleccion;
    }

    public String getImagenExtraEscolar()
    {
        return imagenExtraEscolar;
    }

    public void setImagenExtraEscolar(String imagenExtraEscolar)
    {
        this.imagenExtraEscolar = imagenExtraEscolar;
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
    public boolean compararExtraescolarCon(Extraescolar otro)
    {
        return idExtraescolares.equals(otro.idExtraescolares) &&
                nombreExtra.equals(otro.nombreExtra) &&
                tipoExtra.equals(otro.tipoExtra) &&
                opcionSeleccion.equals(otro.opcionSeleccion) &&
                imagenExtraEscolar.equals(otro.imagenExtraEscolar);
    }

    public int esMasReciente(Extraescolar match)
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
