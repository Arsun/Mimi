package org.scau.mimi.other;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by 10313 on 2017/8/29.
 */

public class MyApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        LitePal.initialize(this);

        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
