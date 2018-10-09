package com.fcp.tec.informatecvdos.modelos;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Calendario
{
    @SerializedName("idevento")
    private String idEvento;
    @SerializedName("contenido_fecha")
    private String contenidoFecha;
    @SerializedName("version")
    private String version;

    //Variable únicamente para la tabla de la BD interna en android
    private int modificado;

    //Constructor
    public Calendario(String idEvento, String contenidoFecha, String version, int modificado)
    {
        this.idEvento = idEvento;
        this.contenidoFecha = contenidoFecha;
        this.version = version;
        this.modificado = modificado;
    }


    //region Gettters and Setters

    public String getIdEvento()
    {
        return idEvento;
    }

    public void setIdEvento(String idEvento)
    {
        this.idEvento = idEvento;
    }

    public String getContenidoFecha()
    {
        return contenidoFecha;
    }

    public void setContenidoFecha(String contenidoFecha)
    {
        this.contenidoFecha = contenidoFecha;
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
    public boolean compararCalendarioCon(Calendario otro)
    {
        return idEvento.equals(otro.idEvento) &&
                contenidoFecha.equals(otro.contenidoFecha);
    }

    public int esMasReciente(Calendario match)
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
