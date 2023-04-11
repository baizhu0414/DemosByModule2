package com.example.utillibrary.permissionutils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 请求权限。
 */
public class PermissionUtil {
    private static final String TAG = "PermissionUtil";
    // 申请权限的代码段数，每次调用申请权限则增加一个。
    static final Queue<PermissionReqBuilder> permissionRequestQueue = new LinkedList<>();

    /**
     * 请求权限的结果回调
     */
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionReqBuilder permissionReq = permissionRequestQueue.poll();
        if (permissionReq == null) {
            return;
        }
        // 1. 分拣授权/拒绝的权限
        final List<String> grantedPermissions = new ArrayList<>();
        final List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            switch (grantResults[i]) {
                case PermissionChecker.PERMISSION_DENIED:
                case PermissionChecker.PERMISSION_DENIED_APP_OP:
                    deniedPermissions.add(permission);
                    break;
                case PermissionChecker.PERMISSION_GRANTED:
                    grantedPermissions.add(permission);
                    break;
                default:
            }
        }
        // 2. 回调申请结果给IPermissionListener
        if (permissionReq.isUiThread
                && permissionReq.handler.getLooper() != null
                && permissionReq.handler.getLooper().getThread().isAlive()) {
            permissionReq.handler.post(new Runnable() {
                @Override
                public void run() {
                    callRequestResultBack(permissionReq, grantedPermissions, deniedPermissions);
                }
            });
        } else {
            callRequestResultBack(permissionReq, grantedPermissions, deniedPermissions);
        }
        // 3. 处理队列里剩余的请求, 从队列里找第一个合理请求
        PermissionReqBuilder reqNext;
        while (!permissionRequestQueue.isEmpty()) {
            reqNext = permissionRequestQueue.peek();
            if (reqNext != null && reqNext.reqPerActivity != null
                    && !reqNext.reqPerActivity.isFinishing()) {
                reqNext.startRequest();
            } else {
                permissionRequestQueue.poll();
            }
        }
    }

    private static void callRequestResultBack(PermissionReqBuilder req, List<String> grantedPer, List<String> deniedPer) {
        if (req.listener != null) {
            if (!grantedPer.isEmpty()) {
                req.listener.onPermissionGranted(req.reqCode);
            }
            if (!deniedPer.isEmpty()) {
                // 此处可以引导到手动授权页面
                req.listener.onPermissionDenied(req.reqCode);
            }
        }

    }

    public static class PermissionReqBuilder {
        Activity reqPerActivity;
        String[] permissions;
        IPermissionListener listener;
        boolean isUiThread = false; // true表示需要在UI线程回调处理
        int reqCode;
        Handler handler;
        boolean isHandlerLoop = false; // 创建Handler时是否重新Looper.prepare了

        private PermissionReqBuilder(Activity activity) {
            this.reqPerActivity = activity;
        }

        // 1
        public PermissionReqBuilder withActivity(Activity activity) {
            return new PermissionReqBuilder(activity);
        }

        /**
         * 2.一次传入全部需要的权限
         */
        public PermissionReqBuilder withPermission(String[]... permissions) {
            this.permissions = concatPermissions(permissions);
            return this;
        }

        // 3
        public PermissionReqBuilder withPermissionListener(IPermissionListener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * 4.是否需要在UI线程进行回调处理。
         */
        public PermissionReqBuilder callbackOnUiThread(boolean callbackOnUi) {
            if (callbackOnUi) {
                // 如果当前不是UI线程，则记录一下，后面需要转到UI线程。
                this.isUiThread =
                        (Looper.getMainLooper() == Looper.myLooper());
            }
            return this;
        }

        // 5
        public PermissionReqBuilder withReqCode(int reqCode) {
            this.reqCode = reqCode;
            return this;
        }

        // 6
        public void startRequest() {
            if (this.listener == null) {
                return;
            }
            boolean isAnyPerDenied = false;
            for (String per : permissions) {
                if (ContextCompat.checkSelfPermission(reqPerActivity, per)
                        == PackageManager.PERMISSION_DENIED) {
                    isAnyPerDenied = true;
                    break;
                }
            }
            if (isAnyPerDenied) { // 有权限未授予，则申请
                if (!permissionRequestQueue.contains(this)) {
                    permissionRequestQueue.add(this);
                }
                requestPermissions();
                if (!isUiThread && handler == null) {
                    if (Looper.myLooper() == null) {
                        Looper.prepare();
                        handler = new Handler(Looper.myLooper());
                        isHandlerLoop = true;
                        Looper.loop();
                    } else {
                        handler = new Handler(Looper.myLooper());
                        isHandlerLoop = false;
                    }
                }
            } else {
                permissionRequestQueue.remove(this);
                if (listener != null) {
                    listener.onPermissionGranted(reqCode);
                }
            }
        }

        private void requestPermissions() {
            // 暂时不知道此段代码的用途，后面遇到BUG再看。
//            if (isRequesting.compareAndSet(false, true)) {
//                LogUtil.log(LogType.LEVEL_D, TAG, "#requestPermissions: Request now.");
//            } else {
//                LogUtil.log(LogType.LEVEL_D, TAG, "#requestPermissions: Quit request.");
//                permissionRequestQueue.poll();
//            }
            ActivityCompat.requestPermissions(reqPerActivity, permissions, reqCode);
        }

        /**
         * List->String[]
         */
        private String[] concatPermissions(String[]... permissions) {
            List<String> result = new ArrayList<>();
            for (String[] per : permissions) {
                result.addAll(Arrays.asList(per));
            }
            return result.toArray(new String[0]);
        }
    }
}
