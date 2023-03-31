package com.example.utillibrary.logutils;

import android.util.Log;

import com.example.utillibrary.BuildConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 日志逻辑参考并大量重构：<a href="https://github.com/orhanobut/logger">logger</a>项目
 */
public class LogUtil {
    // 单个文件最大字节数
    public static final int MAX_SIZE = 500 * 1024;
    public static final String LOG_FILE_NAME = "log_" + BuildConfig.BUILD_TYPE;
    private static final String NEW_LINE = System.getProperty("line.separator");
//    public static final String LOG_FILE_NAME = "" + System.currentTimeMillis() + "_log_" + BuildConfig.BUILD_TYPE;

    static LogType logType;
    private static final DateFormat dateFormat
            = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.CHINA);

    public static void log(int level, String TAG, String msg) {
        StringBuilder leveInfo = new StringBuilder("LEVEL:");
        switch (level) {
            case LogType.LEVEL_D:
                Log.d(TAG, msg);
                leveInfo.append("D");
                break;
            case LogType.LEVEL_I:
                Log.i(TAG, msg);
                leveInfo.append("I");
                break;
            case LogType.LEVEL_W:
                Log.w(TAG, msg);
                leveInfo.append("W");
                break;
            case LogType.LEVEL_E:
                Log.e(TAG, msg);
                leveInfo.append("E");
                break;
            default:
                Log.v(TAG, msg);
                leveInfo.append("V");
                break;
        }
        leveInfo.append(", ")
                .append(dateFormat.format(System.currentTimeMillis()))
                .append(", thread:").append(Thread.currentThread().getName())
                .append(", tag:").append(TAG)
                .append(", msg:").append(msg.replace(",", "，"))
                .append(NEW_LINE);
        logType.logAboveLevel(level, leveInfo.toString());
    }

    public static void init(String folder) {
        logType = new LogTypeDisk(folder);
    }
}
