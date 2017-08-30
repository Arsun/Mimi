package org.scau.mimi.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.litepal.crud.DataSupport;
import org.scau.mimi.R;
import org.scau.mimi.database.User;
import org.scau.mimi.other.Constants;
//import org.scau.mimi.other.GlideApp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 10313 on 2017/8/22.
 */

public class HttpUtil {

    private static final String TAG = "HttpUtil";

    private static final String KEY_USERNAME = "uname";
    private static final String KEY_NICKNAME = "nname";
    private static final String KEY_PASSWORD = "passwd";
    private static final String KEY_CONFIRM_PASSWORD = "rpasswd";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_LOCATION = "lid";
    private static final String KEY_IS_FAKE = "isFake";
    private static final String KEY_IMAGE_ID = "imageidList";
    private static final String KEY_HEADER = "janke-authorization";
    private static final String KEY_IMAGE = "image";


    private static final OkHttpClient sOkHttpClient = new OkHttpClient();

    public static void signUp(String account, String username, String password, String confirmPassword, Callback callback) {
        RequestBody body = new FormBody.Builder()
                .add(KEY_USERNAME, account)
                .add(KEY_NICKNAME, username)
                .add(KEY_PASSWORD, password)
                .add(KEY_CONFIRM_PASSWORD, confirmPassword)
                .build();
        Request request = new Request.Builder()
                .url(Constants.ADDRESS + Constants.SIGN_UP)
                .post(body)
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void login(String account, String password, Callback callback) {
        RequestBody body = new FormBody.Builder()
                .add(KEY_USERNAME, account)
                .add(KEY_PASSWORD, password)
                .build();
        Request request = new Request.Builder()
                .url(Constants.ADDRESS + Constants.LOGIN)
                .post(body)
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void requestMessages(int page, Callback callback) {
        Request request = new Request.Builder()
                .url(Constants.ADDRESS + Constants.MESSAGE_LIST + String.valueOf(page))
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void loadImageByGlide(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(Constants.ADDRESS + url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

    }

    public static void requestLocations(Callback callback) {
        Request request = new Request.Builder()
                .url(Constants.ADDRESS + Constants.LOCATION_LIST)
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void sendMessage(String content, Integer lid, boolean isFake, @Nullable List<Integer> imageIdList, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder()
                .add(KEY_CONTENT, content)
                .add(KEY_LOCATION, lid.toString());

        if (isFake) {
            builder.add(KEY_IS_FAKE, String.valueOf(isFake));
        }

        if (imageIdList != null) {
            for (int i = 0; i < imageIdList.size(); i++) {
                builder.add(KEY_IMAGE_ID, imageIdList.get(i).toString());
                LogUtil.d(TAG, "id: " + imageIdList.get(i));
            }
        }

        User user = DataSupport.findFirst(User.class);

        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(Constants.ADDRESS + Constants.SEND_MESSAGE)
                .addHeader(KEY_HEADER, user.getSecret())
                .post(body)
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);

    }

    public static void uploadImage(final String base64Code, final Callback callback) {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                HttpURLConnection connection = null;
//                try {
//                    URL url = new URL(Constants.ADDRESS + Constants.UPLOAD_IMAGE);
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("POST");
//                    connection.setDoOutput(true);
//                    connection.addRequestProperty("janke-authorization", Constants.SECRET);
//                    requestHeader =
//
//
//                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//                    out.writeBytes("image=" + base64Code);
//
//                    connection.setConnectTimeout(8000);
//                    connection.setReadTimeout(8000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
        RequestBody body = new FormBody.Builder()
                .add(KEY_IMAGE, base64Code)
                .build();

        User user = DataSupport.findFirst(User.class);

        Request request = new Request.Builder()
                .url(Constants.ADDRESS + Constants.UPLOAD_IMAGE)
                .addHeader(KEY_HEADER, user.getSecret())
                .post(body)
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);

    }

    public static void logOut(Callback callback) {

        User user = DataSupport.findFirst(User.class);

        Request request = new Request.Builder()
                .url(Constants.ADDRESS + Constants.LOG_OUT)
                .addHeader(KEY_HEADER, user.getSecret())
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);

    }


}
