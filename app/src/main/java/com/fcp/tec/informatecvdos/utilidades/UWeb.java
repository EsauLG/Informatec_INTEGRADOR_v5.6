package com.fcp.tec.informatecvdos.utilidades;

import android.content.Context;
import android.net.ConnectivityManager;

public class UWeb
{
    public static boolean hayConexion(Context contexto)
    {
        ConnectivityManager cm = (ConnectivityManager) contexto.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
