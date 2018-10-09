package com.fcp.tec.informatecvdos.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticationService extends Service
{
    // Instancia del mAuthenticator
    private Authenticator mAuthenticator;

    @Override
    public void onCreate()
    {
        // Nueva instancia del mAuthenticator
        mAuthenticator = new Authenticator(this);
    }

    /**
     * Ligando el servicio al framework de Android
     */
    @Override
    public IBinder onBind(Intent intent)
    {
        return mAuthenticator.getIBinder();
    }
}
