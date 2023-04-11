package com.example.utillibrary.normalutil;

import android.os.Build;

/**
 * 获取设备信息。
 */
public class DeviceUtil {
    public static StringBuilder collectDeviceInfo() {
        StringBuilder deviceStr = new StringBuilder();
        deviceStr.append("version = ").append(Build.VERSION.CODENAME).append("\n");
        deviceStr.append("brand = ").append(Build.BRAND).append("\n");
        deviceStr.append("model = ").append(Build.MODEL).append("\n");
        deviceStr.append("device = ").append(Build.DEVICE).append("\n");
        deviceStr.append("display = ").append(Build.DISPLAY).append("\n");
        deviceStr.append("hardware = ").append(Build.HARDWARE).append("\n");
        deviceStr.append("time = ").append(System.currentTimeMillis()).append("\n");
        return deviceStr;
    }
}
