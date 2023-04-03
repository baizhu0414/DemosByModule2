package com.example.demosbymodule2;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;

import com.example.utillibrary.fileutils.FileUtil;
import com.example.utillibrary.logutils.LogType;
import com.example.utillibrary.logutils.LogUtil;

import java.util.Locale;

public class DemoApplication extends Application {
    private static final String TAG = "DemoApplication";
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    @SuppressLint("StaticFieldLeak")
    private static DemoApplication instance = null;
    // 主线程刷新界面handler
    private volatile Handler handler = null;

    @SuppressWarnings("WeakerAccess") // for Unit test
    public DemoApplication() {
        super();
        instance = this;
    }

    public Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    // 程序句柄获取函数 YQH 20120706
    public static DemoApplication getInstance() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        context = this;
        // 初始化 主线程刷新界面handler YQH 20120706
        handler = new Handler();
        LogUtil.init(Constant.APP_LOG_PATH);
        // 初始化文件夹
        initFolder();
    }

    public static void initFolder() {
        try {
            FileUtil.foundFolder(Constant.APP_ROOT_PATH_INT, TAG);
            FileUtil.foundFolder(Constant.APP_ROOT_PATH_EXT, TAG);
        } catch (SecurityException e) {
            LogUtil.log(LogType.LEVEL_E, TAG, "#initFolder SecurityException: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 非主线程刷新界面调用函数 YQH 20120706
     */
    public void runOnUIThread(Runnable r) {
        if (r == null) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            r.run();
            return;
        }
        if (handler == null) {
            synchronized (DemoApplication.class) {
                if (handler == null) {
                    handler = new Handler(Looper.getMainLooper());
                }
            }
        }
        handler.post(r);
    }

    // Application.context 需要单独设置，否则Application.context.getString 语言只是跟随系统
    public void setApplicationLanguage(Locale locale) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        Configuration config = getResources().getConfiguration();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        if (Build.VERSION.SDK_INT < 17) {
            config.locale = locale;
        } else {
            config.setLocale(locale);
        }
        Configuration configApp = DemoApplication.getInstance().getApplicationContext().getResources()
                .getConfiguration();
        if (Build.VERSION.SDK_INT < 17) {
            configApp.locale = locale;
        } else {
            configApp.setLocale(config.getLocales().get(0));
        }
        DemoApplication.getInstance().getApplicationContext().getResources().updateConfiguration
                (configApp, dm);
    }
}
