package com.example.utillibrary.logutils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class LogFileHandler extends Handler {
    private final String folderPath;

    LogFileHandler(@NonNull Looper looper, String folderPath) {
        super(looper);
        this.folderPath = folderPath;
    }

    @SuppressWarnings("checkstyle:emptyblock")
    @Override
    public void handleMessage(@NonNull Message msg) {
        String content = (String) msg.obj;
        FileWriter fileWriter = null;
        File logFile = getLogFileByDate(folderPath, LogUtil.LOG_FILE_NAME);
        try {
            // logFile不存在则会创建！
            fileWriter = new FileWriter(logFile, true);
            writeLog(fileWriter, content);
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
     * This is always called on a single background thread.
     * Implementing classes must ONLY write to the fileWriter and nothing more.
     * The abstract class takes care of everything else including close the stream and catching IOException
     *
     * @param fileWriter an instance of FileWriter already initialised to the correct file
     */
    private void writeLog(@NonNull FileWriter fileWriter, @NonNull String content) throws IOException {
        fileWriter.append(content);
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
        return new File(folder, String.format("%s_%s.txt", fileName, suffix));
    }
}
