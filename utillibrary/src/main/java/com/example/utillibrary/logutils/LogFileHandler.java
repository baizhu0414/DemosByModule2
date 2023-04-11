package com.example.utillibrary.logutils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.utillibrary.normalutil.DeviceUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class LogFileHandler extends Handler implements Thread.UncaughtExceptionHandler {
    private final String folderPath;

    LogFileHandler(@NonNull Looper looper, String folderPath) {
        super(looper);
        this.folderPath = folderPath;
    }

    /**
     * 处理普通日志
     */
    @Override
    public void handleMessage(@NonNull Message msg) {
        String content = (String) msg.obj;
        File logFile = getLogFileByDate(folderPath, LogUtil.LOG_FILE_NAME);
        writeLog(logFile, content);
    }

    /**
     * 处理崩溃日志,必须继承{@link Thread.UncaughtExceptionHandler}
     */
    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        // 处理异常--start
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String errorStr = writer.toString();

        File logFile = getLogFileByDate(folderPath, LogUtil.CRASH_FILE_NAME);
        String exceptionStr = DeviceUtil.collectDeviceInfo()
                .append(LogUtil.getCurrentThreadName()).append("\n")
                .append(errorStr).toString();
        writeLog(logFile, exceptionStr);
    }

    /**
     * 核心写日志函数
     */
    private void writeLog(File logFile, String content) {
        FileWriter fileWriter = null;
        try {
            // logFile不存在则会创建！
            fileWriter = new FileWriter(logFile, true);
            fileWriter.append(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            if (fileWriter != null) {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e1) { /* fail silently */ }
            }
        }
    }

    /**
     * 获取日志文件，如果超过一定大小则重新创建文件。
     * log_debug_0.csv : 表格文件，显示方面不太好用。
     */
    private File getLogFileBySize(@NonNull String folderName, @NonNull String fileName) {
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        int fileCount = 0;
        // 新建文件(不一定存在)
        File curFile;
        // 一定存在的文件
        File existingFile = null;

        // curFile: file0(Y),count=1;file1(Y),count=2;file2(N),finish; /exitingFile:file1;
        curFile = new File(folder, String.format("%s_%s.csv", fileName, fileCount));
        while (curFile.exists()) {
            existingFile = curFile;
            fileCount++;
            curFile = new File(folder, String.format("%s_%s.csv", fileName, fileCount));
        }

        // 如果已存在的最后一个文件过大，则返回新建的文件。
        if (existingFile != null) {
            if (existingFile.length() >= LogUtil.MAX_SIZE) {
                return curFile;
            }
            return existingFile;
        }

        return curFile;
    }

    /**
     * 获取日志文件，每天生成一个新的日志文件。
     * log_debug_0404.txt : Good～
     */
    private File getLogFileByDate(@NonNull String folderName, @NonNull String fileName) {
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        DateFormat dateFormat
                = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        String suffix = dateFormat.format(System.currentTimeMillis());
        // 新建文件(不一定存在)
        return new File(folder, String.format("%s_%s.log", fileName, suffix));
    }
}
