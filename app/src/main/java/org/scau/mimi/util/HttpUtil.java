package org.scau.mimi.util;

import org.scau.mimi.other.Constants;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 10313 on 2017/8/22.
 */

public class HttpUtil {

    public static void sendHttpRequest(String address, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void signUp(String account, String username, String password, String confirmPassword, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("uname", account)
                .add("nname", username)
                .add("passwd", password)
                .add("rpasswd", confirmPassword)
                .build();
        Request request = new Request.Builder()
                .url(Constants.ADDRESS + Constants.SIGN_UP)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void login(String account, String password, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("uname", account)
                .add("passwd", password)
                .build();
        Request request = new Request.Builder()
                .url(Constants.ADDRESS + Constants.LOGIN)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
