package com.fcp.tec.informatecvdos.utilidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import java.util.ArrayList;

public final class UBase64
{
    public static final String TAG = UBase64.class.getSimpleName();

    /**
     * Metodo que regresa un array de bitmaps leidos desde la BD en <b>base64<b/> y decodifica las
     * imagenes para ser mostrada en el recyclerview.
     *
     */
    public ArrayList<Bitmap> base64ToBitmap(@NonNull String b64)
    {
        Log.i(TAG, "**Entrando a metodo base64ToBitmap**");
        ArrayList<Bitmap> imagenes = new ArrayList<>();
        try
        {
            byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);

            imagenes.add(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

            Log.i(TAG, "¡Imagen decodificada!");

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.i(TAG, "¡Error al decodificar o no se trata de una imagen en Base64!");
        }
        return imagenes;
    }
}
