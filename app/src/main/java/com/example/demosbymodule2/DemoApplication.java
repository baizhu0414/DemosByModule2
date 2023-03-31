package com.example.demosbymodule2;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.example.utillibrary.logutils.LogType;
import com.example.utillibrary.logutils.LogUtil;

import java.io.File;
import java.io.IOException;
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
            foundFolder(Constant.APP_ROOT_PATH_INT);
            foundFolder(Constant.APP_ROOT_PATH_EXT);
//            makeDIRAndCreateFile(Constant.APP_ROOT_PATH_INT + "/internal_log.txt");
//            makeDIRAndCreateFile(Constant.APP_ROOT_PATH_EXT + "/external_log.txt");
        } catch (SecurityException e) {
            LogUtil.log(LogType.LEVEL_E, TAG, "#initFolder SecurityException: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 创建目录和文件， 如果目录或文件不存在，则创建出来
     *
     * @param filePath 文件路径
     */
    private static synchronized void makeDIRAndCreateFile(String filePath) {
        LogUtil.log(LogType.LEVEL_W, TAG, "#makeDIRAndCreateFile:" + filePath);
        File file = new File(filePath);
        if (TextUtils.isEmpty(file.getParent())) {
            return;
        }
        File parentFile = new File(file.getParent());
        try {
            boolean result =
                    file.exists() || (parentFile.exists() && file.createNewFile()) || (parentFile.mkdirs() && file
                            .createNewFile());
            if (!result) {
                LogUtil.log(LogType.LEVEL_E, TAG, "#makeDIRAndCreateFile: create dir error--" + filePath);
            }
        } catch (IOException e) {
            LogUtil.log(LogType.LEVEL_E, TAG, "#makeDIRAndCreateFile: IOException: " + filePath + "\n" + e);
        }
    }

    /**
     * sd卡中创建自定义文件夹
     *
     * @param folderUrl :文件夹路径
     */
    public static void foundFolder(String folderUrl) {
        LogUtil.log(LogType.LEVEL_W, TAG, "#foundFolder:" + folderUrl);
        // 得到一个路径，内容是sdcard的文件夹路径和名字
        File file = new File(folderUrl);
        boolean result = file.exists() || file.mkdirs();
        if (!result) {
            LogUtil.log(LogType.LEVEL_E, TAG, "#foundFolder: create file error---" + folderUrl);
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
