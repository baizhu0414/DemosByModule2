package com.example.utillibrary.logutils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
        Log.w("DemoApplication", content);

        FileWriter fileWriter = null;
        File logFile = getLogFile(folderPath, LogUtil.LOG_FILE_NAME);

        try {
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

    private File getLogFile(@NonNull String folderName, @NonNull String fileName) {
        File folder = new File(folderName);
        if (!folder.exists()) {
            //TODO: What if folder is not created, what happens then?
            folder.mkdirs();
        }

        int newFileCount = 0;//1,
        File newFile;//file1(N)
        File existingFile = null;//file0(Y)

        newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
        while (newFile.exists()) {
            existingFile = newFile;
            newFileCount++;
            newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
        }

        if (existingFile != null) {//file0
            if (existingFile.length() >= LogUtil.MAX_SIZE) {
                return newFile;
            }
            return existingFile;
        }

        return newFile;
    }
}
