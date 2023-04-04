package com.example.utillibrary;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.utillibrary.logutils.LogType;
import com.example.utillibrary.logutils.LogTypeDisk;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    String TAG = ExampleInstrumentedTest.class.getSimpleName();

    @Test
    public void useAppContext() {
        LogTypeDisk logTypeDisk = new LogTypeDisk("/storage/emulated/0/Android/data/com.example.demosbymodule2/files");
        logTypeDisk.logAboveLevel(LogType.LEVEL_E, TAG + " Test log util:#useAppContext");
    }
}