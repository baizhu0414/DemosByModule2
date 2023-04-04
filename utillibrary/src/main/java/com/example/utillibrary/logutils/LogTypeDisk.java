package com.example.utillibrary.logutils;

import android.os.Handler;
import android.os.HandlerThread;

public class LogTypeDisk implements LogType {
    Handler logFileHandler;

    public LogTypeDisk(String folderPath) {
        // 初始化子线程
        HandlerThread handlerThread = new HandlerThread(logThreadName, Thread.NORM_PRIORITY);
        handlerThread.start();
        this.logFileHandler = new LogFileHandler(handlerThread.getLooper(), folderPath);
    }

    @Override
    public void logAboveLevel(int level, String msg) {
        if (level >= LOWEST_LEVEL) {
            // print to file
            logFileHandler.sendMessage(logFileHandler.obtainMessage(level, msg));
        }
    }

}
