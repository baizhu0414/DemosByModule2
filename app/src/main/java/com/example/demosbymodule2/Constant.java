package com.example.demosbymodule2;

import android.os.Environment;

public class Constant {
    // (/data/user/0)/com.example.demosbymodule2/files：内部存储,但是当手机没有root的时候不能打开此文件夹。（/data/data）
    public static String APP_ROOT_PATH_INT = DemoApplication.context.getFilesDir().getPath();
    /* /storage/emulated/0/Android/data/com.example.demosbymodule2/files：
       外部储存的私有目录，一般放一些长时间保存的数据；不需要外存读写权限。*/
    public static String APP_ROOT_PATH_EXT = DemoApplication.context.getExternalFilesDir(null).getPath();
    // /storage/emulated/0：需要外存读写权限，动态申请。
    public static String APP_ROOT_EXT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    /**
     * 此处保存日志文件
     * /storage/emulated/0/Android/data/com.example.demosbymodule2/files
     */
    public static String APP_LOG_PATH = APP_ROOT_PATH_EXT;
}
