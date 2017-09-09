package org.scau.mimi.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import org.scau.mimi.other.MyApplication;

/**
 * Created by 10313 on 2017/9/8.
 */

public class ToastUtil {

    private ToastUtil() {

    }

    public static void toastWhenOnUiThread(String text) {
        Toast.makeText(
                MyApplication.getContext(),
                text,
                Toast.LENGTH_SHORT)
                .show();
    }

    public static void toastWhenOnSubThread(final Activity activity, final String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(
                        activity,
                        text,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
