package com.example.utillibrary;

import android.util.Log;

public class LogUtil {
    private static final int LEVEL_D = 1;
    private static final int LEVEL_I = 2;
    private static final int LEVEL_W = 3;
    private static final int LEVEL_E = 4;

    public static void d(String TAG, String msg) {
        Log.d(TAG, getMsg(LEVEL_D, msg));
    }

    public static void i(String TAG, String msg) {
        Log.i(TAG, getMsg(LEVEL_I, msg));
    }

    public static void w(String TAG, String msg) {
        Log.w(TAG, getMsg(LEVEL_W, msg));
    }

    public static void e(String TAG, String msg) {
        Log.e(TAG, getMsg(LEVEL_E, msg));
    }

    public static void e(String TAG, String msg, Exception e) {
        Log.e(TAG, getMsg(LEVEL_E, msg), e);
    }

    private static String getMsg(int level, String msg) {
        switch (level) {
            case LEVEL_D:
                break;
            case LEVEL_I:
                break;
            case LEVEL_W:
                break;
            case LEVEL_E:
                break;
        }
        return msg + " thread：" + Thread.currentThread().getName();
    }
}
