package org.scau.mimi.util;

import android.util.Log;

import com.google.gson.Gson;

import org.scau.mimi.gson.ImagesInfo;
import org.scau.mimi.gson.LocationsInfo;
import org.scau.mimi.gson.MessagesInfo;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * Created by 10313 on 2017/8/22.
 */

public class ResponseUtil {

    private static final String TAG = "ResponseUtil";


    public static String getString(Response response) {
        String data = null;
        try {
            data = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static MessagesInfo getMessagesInfo(Response response) {
        String jsonData = getString(response);
        LogUtil.d(TAG, "getMessagesInfo: " + jsonData);
        Gson gson = new Gson();
        return gson.fromJson(jsonData, MessagesInfo.class);
    }

    public static List<MessagesInfo.Content.Message.Location> getLocations(Response response) {
        String jsonData = getString(response);
        Gson gson = new Gson();
        List<MessagesInfo.Content.Message.Location> locations = gson.fromJson(jsonData, LocationsInfo.class).content.locationList;
        String locale = locations.get(0).locale;
        Log.d(TAG, "getLocations: " + locale);
        return locations;
    }

    public static ImagesInfo.Content getImageIdAndWebpath(Response response) {
        String jsonData = getString(response);
        Gson gson = new Gson();
        return gson.fromJson(jsonData, ImagesInfo.class).content;
    }

    public static void getLoginInfo(Response response) {
        String jsonData = getString(response);
        LogUtil.d(TAG, jsonData);

    }
}
