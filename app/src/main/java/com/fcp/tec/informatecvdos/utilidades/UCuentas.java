package com.fcp.tec.informatecvdos.utilidades;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class UCuentas
{
    /**
     * <p>Agrega una cuenta nueva sin pass ni datos para poder usarla en la sincronización.</p>
     *
     * @param context necesario tener un contexto para determinar la cuenta.
     */
    public static Account obtenerCuentaActiva(final Context context)
    {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account cuenta = new Account("ITS F.C.P", "com.fcp.tec.informatec");

        //Comprobamos la existencia de la cuenta
        if (null == accountManager.getPassword(cuenta))
        {

            //añadir la cuenta del account manager sin password y sin datos de usuario
            if (!accountManager.addAccountExplicitly(cuenta, "", null))
            {
                return null;
            }

        }
        return cuenta;
    }
}
