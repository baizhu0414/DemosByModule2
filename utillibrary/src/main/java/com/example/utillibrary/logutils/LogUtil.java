package com.example.utillibrary.logutils;

import android.util.Log;

import com.example.utillibrary.BuildConfig;
import com.example.utillibrary.fileutils.FileUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 日志逻辑参考并大量重构：<a href="https://github.com/orhanobut/logger">logger</a>项目
 */
public class LogUtil {
    public static final String TAG = "LogUtil";
    // 单个文件最大字节数500KB
    public static final int MAX_SIZE = 500 * 1024 * 8;
    public static final String LOG_FILE_NAME = "log_" + BuildConfig.BUILD_TYPE;
    private static final String NEW_LINE = System.getProperty("line.separator");
    public static final String CRASH_FILE_NAME = "crash_log_" + BuildConfig.BUILD_TYPE;

    static LogType logType;
    private static final DateFormat dateFormat
            = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.CHINA);

    public static void log(int level, String TAG, String msg) {
        StringBuilder leveInfoStr = new StringBuilder("LEVEL:");
        switch (level) {
            case LogType.LEVEL_D:
                Log.d(TAG, msg);
                leveInfoStr.append("D");
                break;
            case LogType.LEVEL_I:
                Log.i(TAG, msg);
                leveInfoStr.append("I");
                break;
            case LogType.LEVEL_W:
                Log.w(TAG, msg);
                leveInfoStr.append("W");
                break;
            case LogType.LEVEL_E:
                Log.e(TAG, msg);
                leveInfoStr.append("E");
                break;
            default:
                Log.v(TAG, msg);
                leveInfoStr.append("V");
                break;
        }
        leveInfoStr.append(", ")
                .append(dateFormat.format(System.currentTimeMillis()))
                .append(", thread:").append(getCurrentThreadName())
                .append(", tag:").append(TAG)
                .append(", msg:").append(msg.replace(",", "，"))
                .append(NEW_LINE).append(NEW_LINE);
        logType.logAboveLevel(level, leveInfoStr.toString());
    }

    public static String getCurrentThreadName() {
        return Thread.currentThread().getName();
    }

    public static void init(String folder) {
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            FileUtil.foundFolder(folder, TAG);
        }
        logType = new LogTypeDisk(folder);
    }
}
