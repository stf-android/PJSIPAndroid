package com.cpsc.cpsc_pgsip.pjsip;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;

import com.cpsc.cpsc_pgsip.Utils.PJSipUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnIncomingSubscribeParam;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnInstantMessageStatusParam;
import org.pjsip.pjsua2.OnRegStateParam;

/**
 * 描述:
 * <p>
 *
 * @author allens
 * @date 2018/1/25
 */

public class MyAccount extends Account {

    private Handler handler = new Handler();
    private Context context;


    private OnPJSipRegStateListener listener;

    public MyAccount(Context context, OnPJSipRegStateListener listener) {
        this.listener = listener;
        this.context = context;
    }


    /***
     *  当注册或注销已经启动时通知申请。
     *  请注意，这只会通知初始注册和注销。一旦注册会话处于活动状态，后续刷新将不会导致此回调被调用。
     * @param prm
     */
    @Override
    public void onRegState(OnRegStateParam prm) {
        if (prm.getCode().swigValue() / 100 == 2) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onSuccess();
                }
            });
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onError();
                }
            });

        }
    }

    @Override
    public void onIncomingCall(OnIncomingCallParam prm) {
        Logger.e("来电话啦");

        if (PJSipUtil.currentCall != null) {
            boolean active = PJSipUtil.currentCall.isActive();
            Logger.e("isActive : " + active);
        } else {
            Logger.e("isActive : null");
        }

        MyCall call = null;
        try {
            call = new MyCall(this, prm.getCallId(), new OnComListener() {
                @Override
                public void disconnected() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            context.sendBroadcast(new Intent("handUpCall"));
                        }
                    });
                }
            });
        } catch (Exception e) {
            Logger.e("Exception " + e);
        }

        PJSipUtil.currentCall = call;

        handler.post(new Runnable() {
            @Override
            public void run() {
                context.sendBroadcast(new Intent("onIncomingCall"));
            }
        });
    }

    @Override
    public void onInstantMessageStatus(OnInstantMessageStatusParam prm) {
        super.onInstantMessageStatus(prm);
        Logger.e("onInstantMessageStatus");
    }

    @Override
    public void onIncomingSubscribe(OnIncomingSubscribeParam prm) {
        super.onIncomingSubscribe(prm);
        Logger.e("onIncomingSubscribe");
    }

    @Override
    public void onInstantMessage(OnInstantMessageParam prm) {
        System.out.println("======== Incoming pager ======== ");
        System.out.println("From 		: " + prm.getFromUri());
        System.out.println("To			: " + prm.getToUri());
        System.out.println("Contact		: " + prm.getContactUri());
        System.out.println("Mimetype	: " + prm.getContentType());
        System.out.println("Body		: " + prm.getMsgBody());
    }


}
