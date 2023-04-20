package com.example.utillibrary.normalutil;

import android.content.Context;
import android.widget.Toast;

import com.example.utillibrary.basicui.DemoApplication;

/**
 * 1.toast; 2.getString
 */
public class Utils {
    public static void makeText(int textId) {
        Context context = DemoApplication.context;
        if (context != null) {
            DemoApplication.getInstance().runOnUIThread(() -> {
                Toast.makeText(context, textId, Toast.LENGTH_SHORT).show();
            });
        }
    }

    public static void makeText(String text) {
        Context context = DemoApplication.context;
        if (context != null) {
            DemoApplication.getInstance().runOnUIThread(() -> {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            });
        }
    }
}
