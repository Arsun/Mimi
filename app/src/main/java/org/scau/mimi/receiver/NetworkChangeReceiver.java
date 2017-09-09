package org.scau.mimi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.scau.mimi.util.NetworkUtil;

/**
 * Created by 10313 on 2017/9/8.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if (!NetworkUtil.isNetworkAvailable(context)) {
            Toast.makeText(context,
                    "网络不可用",
                    Toast.LENGTH_SHORT).show();
        }


    }
}
