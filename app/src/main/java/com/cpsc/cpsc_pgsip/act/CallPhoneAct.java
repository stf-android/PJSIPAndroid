package com.cpsc.cpsc_pgsip.act;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allens.lib_retrofit.XRetrofit;
import com.allens.lib_retrofit.impl.OnRetrofit;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cpsc.cpsc_pgsip.Config;
import com.cpsc.cpsc_pgsip.Enums.FromEnums;
import com.cpsc.cpsc_pgsip.Enums.SipCallEnums;
import com.cpsc.cpsc_pgsip.R;
import com.cpsc.cpsc_pgsip.Transformation.BlurTransformation;
import com.cpsc.cpsc_pgsip.Utils.ApiUtil;
import com.cpsc.cpsc_pgsip.Utils.PJSipUtil;
import com.cpsc.cpsc_pgsip.Utils.ShareUtils;
import com.cpsc.cpsc_pgsip.Utils.SoundUtils;
import com.cpsc.cpsc_pgsip.base.BaseAct;
import com.cpsc.cpsc_pgsip.bean.AddBean;
import com.cpsc.cpsc_pgsip.bean.CanCallBean;
import com.cpsc.cpsc_pgsip.bean.SipCallInfo;
import com.cpsc.cpsc_pgsip.pjsip.MyCall;
import com.cpsc.cpsc_pgsip.pjsip.OnCallStateListener;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.pjsip_status_code;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * <p>
 * 拨号键盘 --》互交界面
 * 来电界面
 * 呼叫界面
 *
 * @author allens
 * @date 2018/1/25
 */

public class CallPhoneAct extends BaseAct {
    @BindView(R.id.act_callPhone_img_bg)
    ImageView actCallPhoneImgBg;
    @BindView(R.id.act_callPhone_tv)
    TextView actCallPhoneTv;
    @BindView(R.id.act_callPhone_img)
    ImageView actCallPhoneImg;
    @BindView(R.id.act_callPhone_tv_sate)
    TextView actCallPhoneTvSate;
    @BindView(R.id.act_callPhone_ll_green)
    ImageView actCallPhoneLlGreen;
    @BindView(R.id.act_callPhone_ll_red)
    ImageView actCallPhoneLlRed;
    @BindView(R.id.act_callPhone_ll)
    LinearLayout actCallPhoneLl;
    private String number;
    private MyCall myCall;
    private Handler handler = new Handler();
    private BroadcastReceiver receiver;
    //通话次数
    private int size = 0;
    //通话类型
    private int callType = 0;
    //从哪个界面弹出的这个界面
    private int callPhoneType;
    private SipCallInfo sipCallInfo;


    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_callphone);
        ButterKnife.bind(this);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (PJSipUtil.currentCall != null) {
                    PJSipUtil.currentCall.delete();
                    PJSipUtil.currentCall = null;
                    finish();
                } else {
                    finish();
                }
            }
        };
        registerReceiver(receiver, new IntentFilter("handUpCall"));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        init_stopMusic();
    }

    @Override
    protected void initInject() {

    }

    @Override
    protected void initListener() {
        callPhoneType = getIntent().getIntExtra(Config.FROMTYPE, 0);
        //电话呼入时候
        if (callPhoneType == FromEnums.MainAct.getState()) {
            actCallPhoneLl.setVisibility(View.VISIBLE);
            actCallPhoneImg.setVisibility(View.GONE);
            try {
                String remoteUri = PJSipUtil.currentCall.getInfo().getRemoteUri();
                init_setData(remoteUri, true);
                actCallPhoneTvSate.setText("来电号码");
                init_PlayMusic();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //主动呼出时候
        } else if (callPhoneType == FromEnums.CallAct.getState()) {
            actCallPhoneImg.setVisibility(View.VISIBLE);
            actCallPhoneLl.setVisibility(View.GONE);
            number = getIntent().getStringExtra(CallAct.NUMBER);
//            init_IntentCanCall(); // TODO 联网获取是否可以拨打此号码

            init_setData(number, false);
            init_Call();

            //在sip 账号出设置
        } else if (callPhoneType == FromEnums.SipCallSettingAct.getState()) {
            actCallPhoneImg.setVisibility(View.VISIBLE);
            actCallPhoneLl.setVisibility(View.GONE);
            sipCallInfo = (SipCallInfo) getIntent().getSerializableExtra(SipCallSettingAct.intentType);
            number = sipCallInfo.getNumber();
            init_setData(number, false);

            size = sipCallInfo.getSize();
            callType = sipCallInfo.getCallType();

            Logger.i("size: " + size + " callType " + callType);
            init_Call();
        }
    }

    /***
     * 网络请求，是否能够打电话
     */
    private void init_IntentCanCall() {
        XRetrofit.create()
                .addHeard(ApiUtil.getToken())
                .isShowDialog(false)
                .build(Config.BaseUrl)
                .doGet(CallPhoneAct.this, CanCallBean.class, Config.BaseUrl + "call/can", new OnRetrofit.OnQueryMapListener<CanCallBean>() {
                    @Override
                    public void onMap(Map<String, String> map) {

                    }

                    @Override
                    public void onSuccess(CanCallBean canCallBean) {
                        if (canCallBean.getResult() == Config.SUCCESS) {
                            init_setData(number, false);
                            init_Call();
                        } else {
                            actCallPhoneTvSate.setText(canCallBean.getMessage());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 1000);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        actCallPhoneTvSate.setText(R.string.intent_error);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    }
                });
    }

    /***
     * 来电话时候，播放lings
     */
    private void init_PlayMusic() {
        SoundUtils.playSound(this, R.raw.tm);
    }


    /***
     * 停止音乐
     */
    private void init_stopMusic() {
        SoundUtils.release();
    }


    /***
     * 设置默认参数
     * @param number
     * @param isNeedSplit 需要切割时候切割
     */
    private void init_setData(String number, boolean isNeedSplit) {
        if (isNeedSplit) {
            String[] split = number.split(" ");
            String str = split[0];
            number = str.replace("\"", "").replace("\"", "");
        }
        actCallPhoneTv.setText(number);
        RequestOptions options = new RequestOptions()
                .transforms(new BlurTransformation(CallPhoneAct.this, 5f));
        Glide.with(CallPhoneAct.this)
                .load(Config.callBg)
                .apply(options)
                .into(actCallPhoneImgBg);
    }

    /***
     * 打电话
     */
    private void init_Call() {
        SoundUtils.playSound(this, R.raw.du_for_waite);
        myCall = new MyCall(PJSipUtil.myAccount, -1, new OnCallStateListener() {
            @Override
            public void calling() {
                actCallPhoneTvSate.setText("正在连接...");
            }

            @Override
            public void early() {
//                init_intentCallAdd(); //TODO 将打电话的信息上传到 服务器
                actCallPhoneTvSate.setText("对象已响铃");
                //只有在SipCallSettingAct 过来的 才需要做处理
                if (callPhoneType == FromEnums.SipCallSettingAct.getState()
                        //响铃就挂断
                        && callType == (SipCallEnums.CALL_RING.getState())) {
                    Logger.i("对象已响铃  响铃就挂断");
                    init_SizeRemove_Ring();
                }
            }

            @Override
            public void conmecting() {
                actCallPhoneTvSate.setText("对象已接听");
                init_stopMusic();


                //TODO 骚扰电话，此时不需要，只有在SipCallSettingAct 过来的 才需要做处理
//                if (callPhoneType == FromEnums.SipCallSettingAct.getState()) {
//                    //接听即挂断
//                    if (callType == (SipCallEnums.CALL_CALL.getState())) {
//                        Logger.e("对象已接听  接听即挂断");
//                        init_SizeRemove_Ring_Call();
//
//                    }
//
//                    //响铃时候 对方接听，挂断
//                    if (callType == (SipCallEnums.CALL_RING.getState())) {
//                        Logger.e("对象已响铃  响铃时候 对方接听，挂断");
//                        init_SizeRemove_Ring_Call();
//                    }
//                }
            }

            @Override
            public void confirmed() {
                actCallPhoneTvSate.setText("正在通话");

            }

            @Override
            public void disconnected() {
                actCallPhoneTvSate.setText("通话结束");
//                myCall.delete();
//                finish();
            }

            @Override
            public void error() {
                actCallPhoneTvSate.setText("通话结束");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1500);
            }

        });

        CallOpParam prm = new CallOpParam();
        CallSetting opt = prm.getOpt();
        opt.setAudioCount(1);
        opt.setVideoCount(0);

        String dst_uri = "sip:" + number + "@" + ShareUtils.getInstance(this).getString(Config.SipIp, Config.EMPTY);
        Log.i("stf", "--dst_uri--->" + dst_uri);
        try {
            myCall.makeCall(dst_uri, prm);
            PJSipUtil.currentCall = myCall;
        } catch (Exception e) {
            myCall.delete();
        }
    }

    /***
     * 对方响铃 就算一次 ,将结果上传到服务器
     */
    private void init_intentCallAdd() {

        Date currentTime = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String dateString = formatter.format(currentTime);
        XRetrofit.create()
                .addHeard(ApiUtil.getToken())
                .isShowDialog(false)
                .build(Config.BaseUrl)
                .doPost(CallPhoneAct.this, AddBean.class, "/call/add", new OnRetrofit.OnQueryMapListener<AddBean>() {
                    @Override
                    public void onMap(Map<String, String> map) {
                        map.put("type", String.valueOf(callType));
                        map.put("target", number);
                        map.put("result", "0");//呼叫结果 0 是成功  1 失败
                        map.put("on", dateString);
                    }

                    @Override
                    public void onSuccess(AddBean addBean) {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
    }

    /***
     *  循环呼叫 size--
     *
     *  对象已响铃  响铃时候 对方接听，挂断
     *  对象已接听  接听即挂断
     *
     *  先挂了。然后直接finis
     */
    private void init_SizeRemove_Ring_Call() {
        handUpCall();
        handler.post(new Runnable() {
            @Override
            public void run() {
                size--;
                sipCallInfo.setSize(size);
                EventBus.getDefault().post(sipCallInfo);
                finish();
            }
        });
    }

    /**
     * 循环呼叫 size--
     * <p>
     * 响铃状态时候 响铃10秒 挂断
     */
    private void init_SizeRemove_Ring() {
        Logger.i("init_SizeRemove_Ring");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handUpCall();
                size--;
                sipCallInfo.setSize(size);
                EventBus.getDefault().post(sipCallInfo);
                finish();
            }
        }, 1000 * 5);
    }

    /***
     * 挂断电话
     */
    public void handUpCall() {
        if (PJSipUtil.currentCall != null) {
            CallOpParam prm = new CallOpParam();
            prm.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);
            try {
                PJSipUtil.currentCall.hangup(prm);
                PJSipUtil.currentCall = null;
            } catch (Exception e) {
                if (PJSipUtil.currentCall != null) {
                    PJSipUtil.currentCall.delete();
                    PJSipUtil.currentCall = null;
                }
            }
        }
    }

    /**
     * 同意接听
     */
    private void init_Agree() {
        CallOpParam prm = new CallOpParam();
        prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
        try {
            PJSipUtil.currentCall.answer(prm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.act_callPhone_img, R.id.act_callPhone_ll_green, R.id.act_callPhone_ll_red})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.act_callPhone_img:
                handUpCall();
                finish();
                break;
            case R.id.act_callPhone_ll_green:
                init_Agree();
                actCallPhoneTvSate.setText("正在通话");
                actCallPhoneImg.setVisibility(View.VISIBLE);
                actCallPhoneLl.setVisibility(View.GONE);
                init_stopMusic();
                break;
            case R.id.act_callPhone_ll_red:
                handUpCall();
                actCallPhoneTvSate.setText("通话结束");
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
