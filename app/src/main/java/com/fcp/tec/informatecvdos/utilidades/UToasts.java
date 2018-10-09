package com.fcp.tec.informatecvdos.utilidades;

import android.content.Context;
import android.widget.Toast;


public class UToasts
{
    private static Toast m_currentToast;

    public static void showToast(Context ctx, String text)
    {
        try
        {
            if (m_currentToast != null)
            {
                m_currentToast.cancel();
            }
            m_currentToast = Toast.makeText(ctx, text, Toast.LENGTH_SHORT);
            m_currentToast.show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void dismissToast()
    {
        if (m_currentToast != null)
        {
            m_currentToast.cancel();
        }
    }
}

