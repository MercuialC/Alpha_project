package com.example.administrator.camera.costom_layouts;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Httpun {

    private static final MediaType Image = MediaType.parse("image/jpeg; charset=utf-8");

    private final static int CONNECT_TIMEOUT = 60;
    private final static int READ_TIMEOUT = 100;
    private final static int WRITE_TIMEOUT = 60;
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
            .build();

    public static void postPicture(String url, File file, okhttp3.Callback callback) {

        RequestBody fileBody = RequestBody.create(Image, file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("upload_file", file.getName(), fileBody)
                //.addFormDataPart("photo", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder()
                //.url("http://139.196.35.30:8080/OkHttpTest/uploadFile.do")
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
