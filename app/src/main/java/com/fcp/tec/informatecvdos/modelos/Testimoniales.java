package com.fcp.tec.informatecvdos.modelos;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;
import com.pkmmte.view.CircularImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Testimoniales
{
    @SerializedName("idtestimoniales")
    private String idTestimoniales;
    @SerializedName("nombre_persona")
    private String nombrePersona;
    @SerializedName("testimonial")
    private String testimonial;
    @SerializedName("img_persona")
    private String imagenPersona;
    @SerializedName("idcarrera")
    private String idCarrera;
    @SerializedName("version")
    private String version;

    private int modificado;
    private Bitmap imagenTestimonioDeco;

    //Constructor para traer los datos en TEXTO desde la WEB
    public Testimoniales(String idTestimoniales, String nombrePersona, String testimonial, String imagenPersona, String idCarrera, String version, int modificado)
    {
        this.idTestimoniales = idTestimoniales;
        this.nombrePersona = nombrePersona;
        this.testimonial = testimonial;
        this.imagenPersona = imagenPersona;
        this.idCarrera = idCarrera;
        this.version = version;
        this.modificado = modificado;
    }

    //Constructor para decodificar los datos dentro de la APP
    public Testimoniales(String nombrePersona, String idCarrera, String testimonial, Bitmap imagenTestimonioDeco)
    {
        this.nombrePersona = nombrePersona;
        this.idCarrera = idCarrera;
        this.testimonial = testimonial;
        this.imagenTestimonioDeco = imagenTestimonioDeco;
    }

    //region Getters and Setters

    //Getter de las imagenes locales almacenadas en Base 64
    public Bitmap getImagenTestimonioDeco()
    {
        return imagenTestimonioDeco;
    }

    public String getIdTestimoniales()
    {
        return idTestimoniales;
    }

    public void setIdTestimoniales(String idTestimoniales)
    {
        this.idTestimoniales = idTestimoniales;
    }

    public String getNombrePersona()
    {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona)
    {
        this.nombrePersona = nombrePersona;
    }

    public String getTestimonial()
    {
        return testimonial;
    }

    public void setTestimonial(String testimonial)
    {
        this.testimonial = testimonial;
    }

    public String getImagenPersona()
    {
        return imagenPersona;
    }

    public void setImagenPersona(String imagenPersona)
    {
        this.imagenPersona = imagenPersona;
    }

    public String getIdCarrera()
    {
        return idCarrera;
    }

    public void setIdCarrera(String idCarrera)
    {
        this.idCarrera = idCarrera;
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

    public boolean compararCon(Testimoniales otro)
    {
        return idTestimoniales.equals(otro.idTestimoniales) &&
                nombrePersona == otro.nombrePersona &&
                testimonial == otro.testimonial &&
                imagenPersona == otro.imagenPersona &&
                idCarrera.equals(otro.idCarrera);
    }

    public int esMasReciente(Testimoniales match)
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
