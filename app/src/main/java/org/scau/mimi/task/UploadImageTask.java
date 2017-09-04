package org.scau.mimi.task;

import android.os.AsyncTask;

import org.scau.mimi.gson.ImagesInfo;
import org.scau.mimi.util.HttpUtil;
import org.scau.mimi.util.LogUtil;
import org.scau.mimi.util.ResponseUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 10313 on 2017/9/1.
 */

public class UploadImageTask extends AsyncTask<List<String>, Void, List<Integer>> {

    private static final String TAG = "UploadImageTask";

    @Override
    protected List<Integer> doInBackground(List<String>... params) {
        final List<Integer> imageIds = new ArrayList<>();
        List<String> base64 = params[0];
        for (int i = 0; i < base64.size(); i++) {
            final int[] counter = {0};
            final boolean[] flag = {true};
            while (true) {
                if (flag[0]) {
                    HttpUtil.uploadImage(base64.get(i), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //check network available:
                            if (true) {
                                flag[0] = true;
                            } else {
                                //网络不可用,toast

                            }
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
                            int id = con.imageid;
                            String path = con.webPath;
                            LogUtil.d(TAG, "imageId: " + id);
                            LogUtil.d(TAG, "image path: " + path);
                            imageIds.add(id);
                        }
                    });
                    flag[0] = false;
                }
                if (imageIds.size() == i + 1) {
                    break;
                }
            }

        }
        return imageIds;
    }
}
