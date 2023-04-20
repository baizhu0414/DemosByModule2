package com.example.imageprocessmodule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.utillibrary.basicui.BaseActivity;
import com.example.utillibrary.basicui.Constant;
import com.example.utillibrary.httputils.OkHttpUtil;
import com.example.utillibrary.logutils.LogType;
import com.example.utillibrary.logutils.LogUtil;
import com.example.utillibrary.permissionutils.IPermissionListener;
import com.example.utillibrary.permissionutils.PermissionGroup;
import com.example.utillibrary.permissionutils.PermissionUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    WebView webView;
    Button btnRefresh;
    String URL = "https://bz.zzzmh.cn/index";
    String cookie;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    public void initView() {
        webView = findViewById(R.id.web_show);
        btnRefresh = findViewById(R.id.btn_refresh);
    }

    @Override
    public void initParam() {
        requestPer();
        initWebView();
    }

    @Override
    public void initListener(Context context) {
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(URL);
                Toast.makeText(MainActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        // Android chromium: [INFO:CONSOLE(1)] "Uncaught TypeError: read property ...
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                if (!PermissionUtil.isExternalReadWriteManager(MainActivity.this)) {
                    requestPer();
                    return;
                }
                LogUtil.log(LogType.LEVEL_W, TAG, "Download:url:" + url + " agent:" + userAgent + " disposition:"
                        + contentDisposition + " mime:" + mimetype + " length:" + contentLength + "cookie:" + cookie);
                OkHttpUtil.OkHttpUtilBuilder.init().withUrl(url)
                        .withParams(userAgent, contentDisposition, mimetype, contentLength, cookie)
                        .withDownloadListener(new OkHttpUtil.DownloadReqListener() {
                            @Override
                            public void onFailure(String callMsg, @NonNull IOException e) {
                                LogUtil.log(LogType.LEVEL_E, TAG, "req:" + callMsg + " e:" + e);
                            }

                            @Override
                            public void onResponse(String callMsg, @NonNull String fileName, @NonNull InputStream inputStream) {
                                LogUtil.log(LogType.LEVEL_W, TAG, "req:" + callMsg);
                                File downloadPicFile = getOutPutFile(fileName);
                                try {
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(downloadPicFile));
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
                                    inputStream.close();
                                    bos.flush();
                                    bos.close();
                                    MediaScannerConnection.scanFile(MainActivity.this, new String[]{Constant.APP_DOWNLOAD_PATH},
                                            new String[]{"image/*", "video/*", mimetype}, null);
                                    LogUtil.log(LogType.LEVEL_W, TAG, "下载成功：" + fileName);
                                } catch (IOException e) {
                                    LogUtil.log(LogType.LEVEL_E, TAG, "下载失败：" + fileName);
                                    throw new RuntimeException(e);
                                }
                            }
                        }).build();
            }
        });
        webView.loadUrl(URL);
    }

    private File getOutPutFile(String fileName) {
        return new File(Constant.APP_DOWNLOAD_PATH, fileName);
    }

    void requestPer() {
        PermissionUtil.PermissionReqBuilder.withActivity(this)
                .withPermission(PermissionGroup.STORAGE_GROUP)
                .withPermissionListener(new IPermissionListener() {
                    @Override
                    public void onPermissionGranted(int reqCode) {
                    }

                    @Override
                    public void onPermissionDenied(int reqCode, List<String> deniedPer) {
                        PermissionUtil.reqExternalManager(MainActivity.this);
                    }
                })
                .callbackOnUiThread(false)
                .withReqCode(1122)
                .startRequest();
    }

    public static class MyWebViewClient extends WebViewClient {

    }

}