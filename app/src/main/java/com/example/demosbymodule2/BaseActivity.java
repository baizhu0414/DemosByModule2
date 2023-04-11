package com.example.demosbymodule2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demosbymodule2.database.sqlutil.DatabaseUtil;
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