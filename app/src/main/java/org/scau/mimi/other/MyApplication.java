package org.scau.mimi.other;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import org.litepal.LitePal;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 10313 on 2017/8/29.
 */

public class MyApplication extends Application {

    private static Context sContext;

    private static List<Activity> sActivities = Collections.synchronizedList(new LinkedList<Activity>());

    @Override
    public void onCreate() {
        super.onCreate();

        LitePal.initialize(this);
        sContext = getApplicationContext();

        registerActivityListener();
    }

    private void registerActivityListener() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    addActivity(activity);
                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    removeActivity(activity);
                }
            });

        }

    }

    public void addActivity(Activity activity) {
        sActivities.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (sActivities == null || sActivities.isEmpty()) {
            return;
        }
        if (sActivities.contains(activity)) {
            sActivities.remove(activity);
        }
    }

    public static void finishAllActivities() {
        if (sActivities == null) {
            return;
        }
        for (Activity activity :
                sActivities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
            sActivities.clear();
        }
    }

    public static Context getContext() {
        return sContext;
    }

    public static boolean findActivity(Class<?> cls) {
        if (sActivities == null || sActivities.isEmpty()) {
            return false;
        }

        for (Activity activity :
                sActivities) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }
}
