package com.fcp.tec.informatecvdos.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncServicePreguntasTest extends Service
{
    // Instancia del sync adapter
    private static SyncAdapterPreguntasTest syncAdapterPreguntasTest = null;
    // Objeto para prevenir errores entre hilos
    private static final Object lock = new Object();

    @Override
    public void onCreate()
    {
        synchronized (lock)
        {
            if (syncAdapterPreguntasTest == null)
            {
                syncAdapterPreguntasTest = new SyncAdapterPreguntasTest(getApplicationContext(), true);
            }
        }
    }

    /**
     * Retorna interfaz de comunicación para que el sistema llame al sync adapter
     */
    @Override
    public IBinder onBind(Intent intent)
    {
        return syncAdapterPreguntasTest.getSyncAdapterBinder();
    }
}
