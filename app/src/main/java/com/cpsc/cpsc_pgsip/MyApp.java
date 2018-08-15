package com.cpsc.cpsc_pgsip;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.allens.lib_retrofit.XRetrofitApp;
import com.cpsc.cpsc_pgsip.BroadcastReceiver.NetReceiver;
import com.cpsc.cpsc_pgsip.Utils.CrashHandler;
import com.cpsc.cpsc_pgsip.Utils.PJSipUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * 描述:
 * <p>
 *
 * @author allens
 * @date 2018/1/25
 */

public class MyApp extends Application {
    public static Context content;

    @Override
    public void onCreate() {
        super.onCreate();
        content = this;


        Logger.addLogAdapter(new AndroidLogAdapter());
        PJSipUtil.newInstance().init();
//        CrashHandler.getInstance().init(this);
        XRetrofitApp.init(this, true);

        XRetrofitApp.setConnectTimeout(10);//设置连接超时时间 默认10
        XRetrofitApp.setReadTimeout(10);//设置读取超时时间 默认10
        XRetrofitApp.setWriteTimeout(10);//设置写的超时时间 默认10
        XRetrofitApp.setLogTag("XRetrofit");//设置debug模式下的LogTag 默认 XRetrofit

    }


}
