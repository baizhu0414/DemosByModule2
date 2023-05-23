package com.example.utillibrary.normalutil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.utillibrary.basicui.DemoApplication;
import com.example.utillibrary.logutils.LogType;
import com.example.utillibrary.logutils.LogUtil;

import java.lang.reflect.Field;

/**
 * 1.toast; 2.status bar height;
 */
public class Utils {
    private static final String TAG = "Utils";
    private static int statusBarHeight;

    public static void makeText(int textId) {
        Context context = DemoApplication.context;
        if (context != null) {
            DemoApplication.getInstance().runOnUIThread(() -> {
                Toast.makeText(context, textId, Toast.LENGTH_SHORT).show();
            });
        }
    }

    public static void makeText(String text) {
        Context context = DemoApplication.context;
        if (context != null) {
            DemoApplication.getInstance().runOnUIThread(() -> {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            });
        }
    }

    /**
     * 获取系统状态栏高度
     */
    public static int getStatusBarHeight(Activity activity, boolean refresh) {
        if (!refresh) {
            return statusBarHeight;
        }
        // 1
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        if (statusBarHeight > 0) {
            return statusBarHeight;
        }
        // 2
        try {
            @SuppressLint("PrivateApi")
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int sdp = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = activity.getResources().getDimensionPixelSize(sdp);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException |
                 InstantiationException e) {
            LogUtil.log(LogType.LEVEL_E, TAG, e.getMessage());
        }
        return statusBarHeight;
    }

    /**
     * 绘制图片时使用
     */
    public static int dp2px(float dp) {
        final float scale = DemoApplication.context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 获取dp转换px后的值
     */
    public static float getDimensPx(int dimenId) {
        return DemoApplication.context.getResources().getDimension(dimenId);
    }

    /**
     * 获取dimens里面定义的dp值
     */
    public static float getDimensDp(int dimenId) {
        float dimenPx = getDimensPx(dimenId);
        //获取屏幕密度
        final float scale = DemoApplication.context.getResources().getDisplayMetrics().density;
        //除以屏幕密度，才是真正的值
        return dimenPx / scale;
    }

    /**
     * 功能同{@link DemoApplication#getString(int)}
     */
    public static String getString(int strId) {
        return DemoApplication.getInstance().getString(strId);
    }

    public static Drawable getDrawable(int drawableId) {
        return ContextCompat.getDrawable(DemoApplication.context, drawableId);
    }

}
