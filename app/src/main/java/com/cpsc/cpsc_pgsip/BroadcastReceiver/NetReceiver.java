package com.cpsc.cpsc_pgsip.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cpsc.cpsc_pgsip.Config;
import com.cpsc.cpsc_pgsip.Utils.NetState;
import com.cpsc.cpsc_pgsip.Utils.PJSipUtil;
import com.cpsc.cpsc_pgsip.Utils.ShareUtils;
import com.cpsc.cpsc_pgsip.pjsip.OnPJSipRegStateListener;

/**
 * Created by stf on 2018-07-18.
 */

public class NetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NetState netState = new NetState(context);
        Log.i("stf","---netState-->"+netState.isNetworkChange());
        try {
            if (netState.isNetworkChange()) {
                String ip = ShareUtils.getInstance(context).getString(Config.SipIp, Config.EMPTY);
                String account = ShareUtils.getInstance(context).getString(Config.SipAccount, Config.EMPTY);
                String pwd = ShareUtils.getInstance(context).getString(Config.SipPwd, Config.EMPTY);
                if (!TextUtils.isEmpty(ip) && !TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd)) {
                    init_register(context, account, pwd, ip);
                }
                Toast.makeText(context, "网络状态发生变化", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.fillInStackTrace();
        }
    }

    public void init_register(final Context context, String account, String pwd, String ip) {
        PJSipUtil.newInstance().register(context, account, pwd, ip, new OnPJSipRegStateListener() {
            @Override
            public void onSuccess() {
                com.orhanobut.logger.Logger.e("sip register onSuccess");
//                fgMeTvState.setText(context.getResources().getString(R.string.rgeState) + SipState.SUCCESS.getMsg());
            }

            @Override
            public void onError() {
                com.orhanobut.logger.Logger.e("sip register onError");
//                fgMeTvState.setText(context.getResources().getString(R.string.rgeState) + SipState.ERROR.getMsg());
            }
        });

    }

}
