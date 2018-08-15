package com.cpsc.cpsc_pgsip.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cpsc.cpsc_pgsip.act.OnePxActivity;

/**
 * Created by stf on 2018-07-18.
 */

public class OnePixelReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("stf", "--OnePixelReceiver-->" + intent.getAction());
        //屏幕关闭启动1像素Activity
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) || intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            Log.i("stf", "--OnePixelReceiver-->启动OnePxActivity");
            Intent it = new Intent(context, OnePxActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        }
    }

}
