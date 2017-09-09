package org.scau.mimi.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.litepal.crud.DataSupport;
import org.scau.mimi.R;
import org.scau.mimi.database.User;
//import org.scau.mimi.other.GlideApp;

import java.util.List;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 10313 on 2017/8/22.
 */

public class HttpUtil {

    //Test
    public static final String SECRET = "ca48733586d54f05afdc032dbea8e22c";
    public static final int LID = 3;
    public static final String NICKNAME = "大壳儿的正版";

    private HttpUtil() {

    }

    public static final String ADDRESS = "http://104.224.174.146:8080/Mimi-2.0";

    public static final String SIGN_UP = "/api/user/create";

    public static final String LOGIN = "/api/user/login";

    public static final String MESSAGE_LIST_BEFORE = "/api/message/tmbefore/";

    public static final String MESSAGE_LIST_AFTER = "/api/message/tmafter/";
    public static final String MESSAGE_SINGLE = "/api/message/get/";

    public static final String COMMENT_LIST_BEFORE = "/api/comment/message/";

    public static final String COMMENT_LIST_AFTER = "/api/comment/message/";

    public static final String BEFORE = "/tmbefore/";

    public static final String AFTER = "/tmafter/";

    public static final String LOCATION_LIST = "/api/locale/list";

    public static final String SEND_MESSAGE = "/api/message/create";

    public static final String UPLOAD_IMAGE = "/api/image/create";

    public static final String LOG_OUT = "/api/user/logout";

    public static final String SEND_COMMENT = "/api/comment/create";


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
    private static final String KEY_COMMENT_CONTENT = "content";
    private static final String KEY_MESSAGE_ID = "mid";
    private static final String KEY_COMMENT_ID = "rcid";


    private static final OkHttpClient sOkHttpClient = new OkHttpClient();

    public static void signUp(String account, String username, String password, String confirmPassword, Callback callback) {
        RequestBody body = new FormBody.Builder()
                .add(KEY_USERNAME, account)
                .add(KEY_NICKNAME, username)
                .add(KEY_PASSWORD, password)
                .add(KEY_CONFIRM_PASSWORD, confirmPassword)
                .build();
        Request request = new Request.Builder()
                .url(ADDRESS + SIGN_UP)
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
                .url(ADDRESS + LOGIN)
                .post(body)
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void requestMessagesBefore(long tmBefore, Callback callback) {
        Request request = new Request.Builder()
                .url(ADDRESS + MESSAGE_LIST_BEFORE + tmBefore)
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void requestMessagesAfter(long tmAfter, Callback callback) {
        Request request = new Request.Builder()
                .url(ADDRESS + MESSAGE_LIST_AFTER + tmAfter)
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void requestSingleMessage(int messageId, Callback callback) {
        Request request = new Request.Builder()
                .url(ADDRESS + MESSAGE_SINGLE + messageId)
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void loadImageByGlide(Context context, String webPath, ImageView imageView) {
        Glide.with(context)
                .load(ADDRESS + webPath)
                .asBitmap()
                .placeholder(R.drawable.zs)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);


    }

    public static void requestLocations(Callback callback) {
        Request request = new Request.Builder()
                .url(ADDRESS + LOCATION_LIST)
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
                .url(ADDRESS + SEND_MESSAGE)
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
                .url(ADDRESS + UPLOAD_IMAGE)
                .addHeader(KEY_HEADER, user.getSecret())
                .post(body)
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);

    }

    public static void logOut(Callback callback) {

        User user = DataSupport.findFirst(User.class);

        Request request = new Request.Builder()
                .url(ADDRESS + LOG_OUT)
                .addHeader(KEY_HEADER, user.getSecret())
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);

    }

    public static void requestCommentsBefore(int messageId, long timeBefore, Callback callback) {
        Request request = new Request.Builder()
                .url(ADDRESS + COMMENT_LIST_BEFORE + messageId + BEFORE + timeBefore)
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void requestCommentsAfter(int messageId, long timeBefore, Callback callback) {
        Request request = new Request.Builder()
                .url(ADDRESS + COMMENT_LIST_AFTER + messageId + AFTER + timeBefore)
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void sendComment(String comment, int messageId, @Nullable Integer commentId, Callback callback) {
        User user = DataSupport.findFirst(User.class);
        FormBody.Builder builder = new FormBody.Builder()
                .add(KEY_COMMENT_CONTENT, comment)
                .add(KEY_MESSAGE_ID, String.valueOf(messageId));
        if (commentId != null) {
            builder.add(KEY_COMMENT_ID, commentId.toString());
        }
        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(ADDRESS + SEND_COMMENT)
                .addHeader(KEY_HEADER, user.getSecret())
                .post(body)
                .build();
        sOkHttpClient.newCall(request).enqueue(callback);
    }


}
