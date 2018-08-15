package com.cpsc.cpsc_pgsip.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.cpsc.cpsc_pgsip.MyApp;
import com.cpsc.cpsc_pgsip.R;

public class OnePxActivity extends AppCompatActivity {

    private static finishReceiver mfinishReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
        Log.i("stf", "--onCreate--->OnePxActivity");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("stf", "--onResume--->OnePxActivity");
        checkScreen();
    }

    private void initTestFinsh() {
        Log.i("stf", "--关闭页面广播注册--->finishReceiver");
        mfinishReceiver = new finishReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        MyApp.content.registerReceiver(mfinishReceiver, intentFilter);
    }

    /**
     * @author stf
     * @time 2018-06-13 14:16
     * @remark 检查屏幕状态  isScreenOn为true  屏幕“亮”
     */
    private void checkScreen() {
        PowerManager pm = (PowerManager) OnePxActivity.this.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        Log.i("stf", "-屏幕状态-isScreenOn--->" + isScreenOn);
        if (!isScreenOn) {
            initTestFinsh();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mfinishReceiver != null) {
            Log.i("stf", "--onDestroy->OnePxActivity,取消finishReceiver-->");
            MyApp.content.unregisterReceiver(mfinishReceiver);
        }

    }

    /**
     * @author stf
     * @time 2018-06-13 11:51
     * @remark 结束该页面的广播
     */
    public class finishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("stf", "--finishReceiver-->" + intent.getAction());
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.i("stf", "--finishReceiver--关闭OnePxActivity>");
                finish();
            }
        }
    }

}
