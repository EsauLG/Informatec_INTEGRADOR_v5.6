package com.fcp.tec.informatecvdos.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class SyncServiceCarreras extends Service
{

    // Instancia del sync adapter
    private static SyncAdapterCarreras syncAdapterCarreras = null;
    // Objeto para prevenir errores entre hilos
    private static final Object lock = new Object();

    @Override
    public void onCreate()
    {
        synchronized (lock)
        {
            if (syncAdapterCarreras == null)
            {
                syncAdapterCarreras = new SyncAdapterCarreras(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return syncAdapterCarreras.getSyncAdapterBinder();
    }
}
