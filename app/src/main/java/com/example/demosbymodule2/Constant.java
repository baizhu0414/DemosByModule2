package com.example.demosbymodule2;

public interface Constant {
    // (/data/user/0)/com.example.demosbymodule2/files：内部存储,但是当手机没有root的时候不能打开此文件夹。（/data/data）
    String APP_ROOT_PATH_INT = DemoApplication.context.getFilesDir().getPath();
    // /storage/emulated/0/Android/data/com.example.demosbymodule2/files：外部储存的私有目录，一般放一些长时间保存的数据
    String APP_ROOT_PATH_EXT = DemoApplication.context.getExternalFilesDir(null).getPath();
}
