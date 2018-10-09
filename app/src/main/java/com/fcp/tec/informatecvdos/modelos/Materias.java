package com.fcp.tec.informatecvdos.modelos;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Materias
{
    @SerializedName("idmaterias")
    private String idMaterias;
    @SerializedName("nombre_materia")
    private String nombreMateria;
    @SerializedName("clave_materia")
    private String claveMateria;
    @SerializedName("tipo_materia")
    private String tipoMateria;
    @SerializedName("version")
    private String version;

    //Variable únicamente para la tabla de la BD interna en android
    private int modificado;

    public Materias(String idMaterias, String nombreMateria, String claveMateria, String tipoMateria, String version, int modificado)
    {
        this.idMaterias = idMaterias;
        this.nombreMateria = nombreMateria;
        this.claveMateria = claveMateria;
        this.tipoMateria = tipoMateria;
        this.version = version;
        this.modificado = modificado;
    }

    //region Getters and Setters
    public String getIdMaterias()
    {
        return idMaterias;
    }

    public void setIdMaterias(String idMaterias)
    {
        this.idMaterias = idMaterias;
    }

    public String getNombreMateria()
    {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria)
    {
        this.nombreMateria = nombreMateria;
    }

    public String getClaveMateria()
    {
        return claveMateria;
    }

    public void setClaveMateria(String claveMateria)
    {
        this.claveMateria = claveMateria;
    }

    public String getTipoMateria()
    {
        return tipoMateria;
    }

    public void setTipoMateria(String tipoMateria)
    {
        this.tipoMateria = tipoMateria;
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

    public boolean compararMateriaCon(Materias otro)
    {
        return idMaterias.equals(otro.idMaterias) &&
                nombreMateria.equals(otro.nombreMateria) &&
                claveMateria.equals(otro.claveMateria) &&
                tipoMateria.equals(otro.tipoMateria);
    }

    public int esMasReciente(Materias match)
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
