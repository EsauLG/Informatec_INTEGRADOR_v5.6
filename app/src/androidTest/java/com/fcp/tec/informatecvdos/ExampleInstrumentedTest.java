package com.fcp.tec.informatecvdos;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation Examen, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Examen documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext()
    {
        // Context of the app under Examen.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.fcp.tec.informatec", appContext.getPackageName());
    }
}
