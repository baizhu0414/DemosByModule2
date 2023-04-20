package com.example.utillibrary.basicui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.utillibrary.database.sqlutil.DatabaseUtil;
import com.example.utillibrary.fileutils.FileUtil;
import com.example.utillibrary.normalutil.Utils;
import com.example.utillibrary.permissionutils.PermissionUtil;

import java.util.Stack;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final Stack<BaseActivity> activities = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(getLayoutId());
        initView();
        initParam();
        initListener(this);
        // 事件总线相关
//        UIEvent.getInstance().register(initHandler());
    }

    @Override
    protected void onDestroy() {
//        UIEvent.getInstance().remove(initHandler());
        removeActivity(this);
        super.onDestroy();
        DatabaseUtil.closeAllDatabases();
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initParam();

    public abstract void initListener(Context context);

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (PermissionUtil.isExternalManager()) {
            Utils.makeText("授权外存管理权限成功。");
            FileUtil.foundFolder(Constant.APP_DOWNLOAD_PATH, getClass().getSimpleName());
        }
    }

    private static void addActivity(BaseActivity activity) {
        synchronized (BaseActivity.class) {
            activities.push(activity);
        }
    }

    public static BaseActivity getTopActivity() {
        synchronized (BaseActivity.class) {
            if (activities.empty()) {
                return null;
            } else {
                return activities.peek();
            }
        }
    }

    public static void removeActivity(BaseActivity activity) {
        synchronized (BaseActivity.class) {
            activities.remove(activity);
        }
    }
}