package com.cpsc.cpsc_pgsip.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.cpsc.cpsc_pgsip.Config;
import com.cpsc.cpsc_pgsip.MyApp;
import com.cpsc.cpsc_pgsip.Utils.IPAddress;
import com.cpsc.cpsc_pgsip.Utils.PJSipUtil;
import com.cpsc.cpsc_pgsip.Utils.ShareUtils;
import com.cpsc.cpsc_pgsip.pjsip.OnPJSipRegStateListener;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by stf on 2018-07-18.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobHandlerService extends JobService {
    private JobScheduler mJobScheduler;

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.e("JobHandlerService  onStartCommand");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(startId++,
                    new ComponentName(getPackageName(), JobHandlerService.class.getName()));

            builder.setPeriodic((1000 * 60 * 1));//设置间隔时间

            builder.setRequiresCharging(true);// 设置是否充电的条件,默认false

            builder.setRequiresDeviceIdle(true);// 设置手机是否空闲的条件,默认false

            builder.setPersisted(true);//设备重启之后你的任务是否还要继续执行

            if (mJobScheduler.schedule(builder.build()) <= 0) {
                Logger.e("JobHandlerService  工作失败");
            } else {
                Logger.e("JobHandlerService  工作成功");
            }
        }
        return START_STICKY;
    }


    @Override
    public boolean onStartJob(JobParameters params) {
        Logger.e("JobHandlerService  服务启动");
        if (!isServiceRunning("com.amap.api.location.APSService")) {
            init_Aps();
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (!isServiceRunning("com.amap.api.location.APSService")) {
            init_Aps();
        }
        return false;
    }

    private void init_Aps() {
        String ipAddress = IPAddress.getIPAddress(MyApp.content);
        String ipMsg = ShareUtils.getInstance(MyApp.content).getString(Config.IpAddress, Config.EMPTY);
        if (!ipMsg.equals(Config.EMPTY)) {
            if (!ipAddress.equals(ipMsg)) {
                ShareUtils.getInstance(MyApp.content).putString(Config.IpAddress, ipAddress);

                Intent intent = new Intent();
                intent.setAction("android.net.conn.CONNECTIVITY_CHANGE");
                sendBroadcast(intent);
                init_register();

            }
        } else {
            ShareUtils.getInstance(MyApp.content).putString(Config.IpAddress, ipAddress);
        }

        init_register();
    }

    private void init_register() {

        String ip = ShareUtils.getInstance(MyApp.content).getString(Config.SipIp, Config.EMPTY);
        String account = ShareUtils.getInstance(MyApp.content).getString(Config.SipAccount, Config.EMPTY);
        String pwd = ShareUtils.getInstance(MyApp.content).getString(Config.SipPwd, Config.EMPTY);
        if (!TextUtils.isEmpty(ip) && !TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd)) {
            PJSipUtil.newInstance().register(MyApp.content, account, pwd, ip, new OnPJSipRegStateListener() {
                @Override
                public void onSuccess() {
                    com.orhanobut.logger.Logger.e("sip register onSuccess");
                }

                @Override
                public void onError() {
                    com.orhanobut.logger.Logger.e("sip register onError");
                }
            });
        }
    }

    // 服务是否运行
    public boolean isServiceRunning(String serviceName) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        // 获取运行服务再启动
        for (ActivityManager.RunningAppProcessInfo info : lists) {
            System.out.println(info.processName);
            if (info.processName.equals(serviceName)) {
                isRunning = true;
            }
        }
        return isRunning;
    }

}
