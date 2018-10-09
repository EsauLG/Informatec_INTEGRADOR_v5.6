package com.fcp.tec.informatecvdos.utilidades;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;

public class UFirstBoot
{
   /* private boolean esPrimeraVez(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("RanBefore", Context.MODE_PRIVATE); //getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore)
        {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();

            topLevelLayout.setVisibility(View.VISIBLE);

            topLevelLayout.setOnTouchListener(new View.OnTouchListener()
            {

                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    topLevelLayout.setVisibility(View.INVISIBLE);
                    return false;
                }

            });


        }
        return ranBefore;
    }*/
}
