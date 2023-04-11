package com.example.utillibrary.fileutils;

import android.text.TextUtils;

import com.example.utillibrary.logutils.LogType;
import com.example.utillibrary.logutils.LogUtil;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    /**
     * 创建目录和文件， 如果目录或文件不存在，则创建出来
     *
     * @param filePath 文件路径
     */
    public static synchronized void makeDIRAndCreateFile(String filePath, String TAG) {
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
    public static void foundFolder(String folderUrl, String TAG) {
        // 得到一个路径，内容是sdcard的文件夹路径和名字
        File file = new File(folderUrl);
        if (!file.exists()) {
            boolean dirResult = file.mkdirs();
            if (!dirResult) {
                LogUtil.log(LogType.LEVEL_E, TAG, "#foundFolder:" + folderUrl + " Fail !!!");
            }
        }
    }
}
