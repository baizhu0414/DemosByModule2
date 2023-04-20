package com.example.utillibrary.httputils;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.utillibrary.logutils.LogType;
import com.example.utillibrary.logutils.LogUtil;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpUtil {
    private static final String TAG = "OkHttpUtil";

    public static class OkHttpUtilBuilder {
        private String url;
        private String userAgent, contentDisposition, mime, cookie;
        private long contentLength;
        private OkHttpClient client;
        private Request request;
        private Call call;
        private Callback listener;

        private OkHttpUtilBuilder() {
        }

        // 1
        public static OkHttpUtilBuilder init() {
            return new OkHttpUtilBuilder();
        }

        // 2
        public OkHttpUtilBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        // 3
        public OkHttpUtilBuilder withDownloadListener(DownloadReqListener listener) {
            this.listener = new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    listener.onFailure(call.toString(), e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        listener.onResponse(call.toString(), getFileNameByUrl(url), response.body().byteStream());
                    } else {
                        LogUtil.log(LogType.LEVEL_E, TAG, response.toString());
                    }
                }
            };
            return this;
        }

        // 4
        public OkHttpUtilBuilder withParams(String userAgent, String contentDisposition, String mimetype,
                                            long contentLength, String cookie) {
            this.userAgent = userAgent;
            this.contentDisposition = contentDisposition;
            this.mime = mimetype;
            this.contentLength = contentLength;
            this.cookie = cookie;
            return this;
        }

        // 5
        public void build() {
            client = new OkHttpClient();
            request = new Request.Builder()
                    .removeHeader("User-Agent")
                    .removeHeader("Referer")
                    .addHeader("User-Agent", userAgent)
                    .addHeader("Referer", url)
                    .url(url)
                    .build();
            call = client.newCall(request);
            call.enqueue(listener);
        }

        private String getFileNameByUrl(String url) {
            return Uri.parse(url).getLastPathSegment();
        }
    }

    public interface DownloadReqListener {

        void onFailure(String callMsg, @NonNull IOException e);

        void onResponse(String callMsg, @NonNull String fileName, @NonNull InputStream stream);
    }
}
