package com.fcp.tec.informatecvdos.utilidades;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;


public final class USincronizacion
{

    public void sincronizarAutomaticamente(Context context, String autoridad, long frecuenciaSincronizacion)
    {
        long SYNC_FREQUENCY = frecuenciaSincronizacion;

        Account cuentaActiva = UCuentas.obtenerCuentaActiva(context);


        //verificación para evitar iniciar más de una sincronización a la vez.
        if (ContentResolver.isSyncActive(cuentaActiva, autoridad))
        {
            Log.i("Sincronizacion", "Ya existe una sincronización en curso");
            return;
        }

        if (UWeb.hayConexion(context))
        {
            ContentResolver.setSyncAutomatically(cuentaActiva, autoridad, true);
            ContentResolver.addPeriodicSync(cuentaActiva, autoridad, new Bundle(), SYNC_FREQUENCY);
        }

    }

}
