package com.example.demosbymodule2;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demosbymodule2.database.sqlutil.DatabaseUtil;
import com.example.utillibrary.fileutils.FileUtil;
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
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q
                && requestCode == PermissionUtil.REQUEST_STORAGE_PERMISSION_CODE
                && Environment.isExternalStorageManager()) {
            Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
            FileUtil.foundFolder(Constant.APP_ROOT_EXT_DIR + "/DemosByModule2",
                    getClass().getSimpleName());
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