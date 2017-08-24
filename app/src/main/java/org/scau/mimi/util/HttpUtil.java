package org.scau.mimi.util;

import android.content.Context;
import android.widget.ImageView;

import org.scau.mimi.other.Constants;
import org.scau.mimi.other.GlideApp;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 10313 on 2017/8/22.
 */

public class HttpUtil {

    private static final OkHttpClient sOkHttpClient = new OkHttpClient();

    public static void sendHttpRequest(String address, Callback callback) {
        Request request = new Request.Builder()
                .url(address)
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void signUp(String account, String username, String password, String confirmPassword, Callback callback) {
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
        sOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void login(String account, String password, Callback callback) {
        RequestBody body = new FormBody.Builder()
                .add("uname", account)
                .add("passwd", password)
                .build();
        Request request = new Request.Builder()
                .url(Constants.ADDRESS + Constants.LOGIN)
                .post(body)
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void getMessageList(int page, Callback callback) {
        Request request = new Request.Builder()
                .url(Constants.ADDRESS + Constants.MESSAGE_LIST + String.valueOf(page))
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void loadImageByGlide(Context context, String url, ImageView imageView) {
        GlideApp
                .with(context)
                .load(Constants.ADDRESS + url)
                .into(imageView);

    }

}
