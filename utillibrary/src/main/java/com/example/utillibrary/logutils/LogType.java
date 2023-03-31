package com.example.utillibrary.logutils;

public interface LogType {
    int LEVEL_D = 1;
    int LEVEL_I = 2;
    int LEVEL_W = 3;
    int LEVEL_E = 4;
    // 打印到文件中的日志的最低等级，低于此等级在文件中记录。
    int LOWEST_LEVEL = LEVEL_I;
    String logThreadName = "logFileThread";

    /**
     * @param level 高于这个等级的日志才进行文件输出。
     */
    void logAboveLevel(int level, String msg);
}
