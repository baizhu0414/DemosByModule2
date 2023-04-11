package com.example.utillibrary.permissionutils;

import android.Manifest;
import android.app.Activity;
import android.os.Build;

public class PermissionGroup {
    /**
     * Android11需要另外调用{@link PermissionUtil#reqExternalStorage(Activity)},并注册
     * {@code Manifest.permission.MANAGE_EXTERNAL_STORAGE}
     */
    public static String[] STORAGE_GROUP;

    static {
        if (sdkAboveQ_29()) {
            STORAGE_GROUP = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE
            };
        } else {
            STORAGE_GROUP = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }
    }

    /**
     * 启动时必须的电话权限
     * <p>
     * READ_PHONE_STATE用于读设备id
     */
    public static final String[] PHONE_BOOT = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };

    public static final String[] CAMERA_GROUP = new String[]{
            Manifest.permission.CAMERA
    };

    public static final String[] CONTACT_GROUP = new String[]{
            Manifest.permission.READ_CONTACTS,
    };

    public static final String[] LOCATION_GROUP = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static final String[] BACKGROUND_LOCATION_GROUP;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            BACKGROUND_LOCATION_GROUP = new String[]{
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            };
        } else {
            BACKGROUND_LOCATION_GROUP = new String[0];
        }
    }

    public static final String[] MICROPHONE_GROUP = new String[]{
            Manifest.permission.RECORD_AUDIO
    };

    public static final String[] PHONE_GROUP = new String[]{
            Manifest.permission.CALL_PHONE
    };

    public static final String[] CALL_LOG = new String[]{
            Manifest.permission.READ_CALL_LOG
    };

    public static final String[] CALENDAR_GROUP = new String[]{
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };

    public static final String[] SMS_GROUP = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
    };

    // 获取步数权限
    public static String[] HEALTH_PERMISSION;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            HEALTH_PERMISSION = new String[]{
                    Manifest.permission.ACTIVITY_RECOGNITION
            };
        } else {
            HEALTH_PERMISSION = new String[0];
        }
    }

    /**
     * 判断sdk版本大于29，即Android版本是否大于10
     *
     * @return true if sdk version > 29, 至少android11。
     */
    public static boolean sdkAboveQ_29() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.Q;
    }
}
