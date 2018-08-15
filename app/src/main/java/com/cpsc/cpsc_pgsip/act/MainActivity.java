package com.cpsc.cpsc_pgsip.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.cpsc.cpsc_pgsip.BroadcastReceiver.NetReceiver;
import com.cpsc.cpsc_pgsip.Config;
import com.cpsc.cpsc_pgsip.Enums.FromEnums;
import com.cpsc.cpsc_pgsip.R;
import com.cpsc.cpsc_pgsip.Utils.ActivityContainer;
import com.cpsc.cpsc_pgsip.Utils.PJSipUtil;
import com.cpsc.cpsc_pgsip.Utils.Rg2FmUtils;
import com.cpsc.cpsc_pgsip.base.BaseAct;
import com.cpsc.cpsc_pgsip.fragment.MeFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author allens
 */
public class MainActivity extends BaseAct {

    @BindView(R.id.main_fl)
    FrameLayout mainFl;
    @BindView(R.id.main_rg)
    RadioGroup mainRg;
    @BindView(R.id.main_add)
    ImageView mainAdd;

    private long lastTime;
    private BroadcastReceiver receiver;
    public static boolean ISFROMMAIN = true;


    private List<Fragment> getFragmentList() {
        List<Fragment> list = new ArrayList<>();
        list.add(MeFragment.newInstance("1", ""));
        list.add(MeFragment.newInstance("2", ""));
        list.add(null);
        list.add(MeFragment.newInstance("4", ""));
        list.add(MeFragment.newInstance("5", ""));
        return list;
    }



    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void initInject() {

    }

    @Override
    protected void initListener() {
        Rg2FmUtils.getInstance().tabControl(getFragmentList(), mainRg, getSupportFragmentManager(), R.id.main_fl);
        init_Gone();
        initRegisterCallCome();
    }



    /***
     * 将模块暂时隐藏，以后二次开发时候需要
     */
    private void init_Gone() {
        mainRg.setVisibility(View.GONE);
    }

    /**b
     * 注册接听电话广播
     */
    private void initRegisterCallCome() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent intentmain = new Intent(MainActivity.this, CallPhoneAct.class);
                intentmain.putExtra(Config.FROMTYPE, FromEnums.MainAct.getState());
                startActivity(intentmain);
            }
        };

        registerReceiver(receiver, new IntentFilter("onIncomingCall"));
    }

    @OnClick(R.id.main_add)
    public void onViewClicked() {
        startActivity(new Intent(this, CallAct.class));
    }


    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime < 2 * 1000) {
            ActivityContainer.newInstance().exit();
        } else {
            lastTime = currentTime;
            Snackbar.make(mainAdd, R.string.exitApp, Snackbar.LENGTH_SHORT).show();
        }
    }
}
