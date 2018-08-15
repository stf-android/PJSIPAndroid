package com.cpsc.cpsc_pgsip.pjsip;

import android.os.Handler;
import android.os.Looper;

import com.cpsc.cpsc_pgsip.Utils.PJSipUtil;

import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.OnInstantMessageStatusParam;
import org.pjsip.pjsua2.pjmedia_type;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_role_e;
import org.pjsip.pjsua2.pjsip_status_code;
import org.pjsip.pjsua2.pjsua_call_media_status;

import java.util.logging.Logger;

/**
 * 描述:
 * <p>
 *
 * @author allens
 * @date 2018/1/25
 */

public class MyCall extends Call {

    private OnCallStateListener onCallStateListener;

    private OnComListener listener;
    private Handler handler = new Handler(Looper.getMainLooper());
    private pjsip_role_e role;
    private pjsip_inv_state state;


    //MD 接受铃声状态会显示两次，无奈
    private boolean isFirst = true;

    public MyCall(MyAccount cPtr, int cMemoryOwn, OnCallStateListener listener) {
        super(cPtr, cMemoryOwn);
        onCallStateListener = listener;

        com.orhanobut.logger.Logger.i("MyCall onCreate()");
    }

    public MyCall(MyAccount cPtr, int cMemoryOwn, OnComListener listener) {
        super(cPtr, cMemoryOwn);
        this.listener = listener;

        com.orhanobut.logger.Logger.i("MyCall onCreate()");
    }


    public MyCall(MyAccount cPtr, int cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    /***
     * 内部函数（由Endpoint调用）处理呼叫状态更改。
     * @param prm
     */
    @Override
    public void processStateChange(OnCallStateParam prm) {
        super.processStateChange(prm);
        com.orhanobut.logger.Logger.e("processStateChange");
    }

    @Override
    public void onInstantMessageStatus(OnInstantMessageStatusParam prm) {
        super.onInstantMessageStatus(prm);
        com.orhanobut.logger.Logger.e("onInstantMessageStatus");
    }


    /***
     * 当通话状态改变时通知应用程序。
     * 然后，应用程序可以通过调用getInfo（）函数来查询调用信息以获取详细调用状态。
     * @param prm
     */
    @Override
    public void onCallState(OnCallStateParam prm) {
        super.onCallState(prm);
        com.orhanobut.logger.Logger.e("==============   onCallState   =================");
        final boolean active = isActive();
        if (!active) {
            com.orhanobut.logger.Logger.i("MyCall is not active");
        } else {
            com.orhanobut.logger.Logger.i("MyCall is  active");
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    CallInfo info = getInfo();
                    state = info.getState();
                    role = info.getRole();
                    com.orhanobut.logger.Logger.i("state: " + state);
                    com.orhanobut.logger.Logger.e("role: " + role);
                    //电话呼出
                    if (role == pjsip_role_e.PJSIP_ROLE_UAC) {
                        if (onCallStateListener == null) {
                            return;
                        }
                        if (state == pjsip_inv_state.PJSIP_INV_STATE_CALLING) {
                            onCallStateListener.calling();
                        } else if (state == pjsip_inv_state.PJSIP_INV_STATE_EARLY) {
                            if (isFirst) {
                                onCallStateListener.early();
                                isFirst = false;
                            }
                        } else if (state == pjsip_inv_state.PJSIP_INV_STATE_CONNECTING) {
                            onCallStateListener.conmecting();
                        } else if (state == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
                            onCallStateListener.confirmed();
                        } else if (state == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                            onCallStateListener.disconnected();
                        }

                        //电话呼入
                    } else if (role == pjsip_role_e.PJSIP_ROLE_UAS) {
                        if (state == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                            if (listener != null) {
                                listener.disconnected();
                            }
                        }
                    }
                } catch (Exception e) {
                    com.orhanobut.logger.Logger.e("e : " + e.getMessage());
                    if (role == pjsip_role_e.PJSIP_ROLE_UAC) {
                        if (onCallStateListener != null) {
                            onCallStateListener.error();
                        }
                        //电话呼入
                    } else if (role == pjsip_role_e.PJSIP_ROLE_UAS) {
                        com.orhanobut.logger.Logger.e("e : " + e.getMessage());
                        if (listener != null) {
                            listener.disconnected();
                        }
                        if (onCallStateListener != null) {
                            onCallStateListener.error();
                        }
                    } else {
                        com.orhanobut.logger.Logger.i("Exception : " + e.getMessage());
                        if (PJSipUtil.currentCall != null) {
                            PJSipUtil.currentCall.delete();
                            com.orhanobut.logger.Logger.i("Exception : delete ");
                        }
                        PJSipUtil.currentCall = null;


                        com.orhanobut.logger.Logger.i("Exception : listener " + listener);
                        if (listener != null) {
                            listener.disconnected();
                        }

                        com.orhanobut.logger.Logger.i("Exception : onCallStateListener " + onCallStateListener);
                        if (onCallStateListener != null) {
                            onCallStateListener.error();
                        }
                    }
                }
            }
        });
    }

    /***
     * 通话中媒体状态发生变化时通知应用程序。
     * 正常的应用程序需要实现这个回调，例如将呼叫的媒体连接到声音设备。当使用ICE时，该回调也将被调用以报告ICE协商失败。
     * @param prm
     */
    @Override
    public void onCallMediaState(OnCallMediaStateParam prm) {
        CallInfo ci;
        try {
            ci = getInfo();
        } catch (Exception e) {
            return;
        }

        CallMediaInfoVector cmiv = ci.getMedia();

        for (int i = 0; i < cmiv.size(); i++) {
            CallMediaInfo cmi = cmiv.get(i);
            if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO &&
                    (cmi.getStatus() == pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE ||
                            cmi.getStatus() == pjsua_call_media_status.PJSUA_CALL_MEDIA_REMOTE_HOLD)) {
                Media m = getMedia(i);
                AudioMedia am = AudioMedia.typecastFromMedia(m);
                try {
                    PJSipUtil.ep.audDevManager().getCaptureDevMedia().startTransmit(am);
                    am.startTransmit(PJSipUtil.ep.audDevManager().getPlaybackDevMedia());
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }

}
