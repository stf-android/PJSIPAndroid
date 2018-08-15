package com.cpsc.cpsc_pgsip.fragment;


import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpsc.cpsc_pgsip.BroadcastReceiver.NetReceiver;
import com.cpsc.cpsc_pgsip.BroadcastReceiver.OnePixelReceiver;
import com.cpsc.cpsc_pgsip.Config;
import com.cpsc.cpsc_pgsip.Enums.SipState;
import com.cpsc.cpsc_pgsip.R;
import com.cpsc.cpsc_pgsip.Utils.ImgUtil;
import com.cpsc.cpsc_pgsip.Utils.PJSipUtil;
import com.cpsc.cpsc_pgsip.Utils.ShareUtils;
import com.cpsc.cpsc_pgsip.act.SipCallSettingAct;
import com.cpsc.cpsc_pgsip.act.SipSettingAct;
import com.cpsc.cpsc_pgsip.base.BaseFragment;
import com.cpsc.cpsc_pgsip.bean.FgMeRvInfo;
import com.cpsc.cpsc_pgsip.dto.PjRegEvent;
import com.cpsc.cpsc_pgsip.pjsip.OnPJSipRegStateListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author allens
 */
public class MeFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Unbinder unbinder;
    @BindView(R.id.fg_me_tv_name)
    TextView fgMeTvName;
    @BindView(R.id.fg_me_img_heard)
    ImageView fgMeImgHeard;
    @BindView(R.id.fg_me_tv_hasSize)
    TextView fgMeTvHasSize;
    @BindView(R.id.fg_me_tv_noHasSize)
    TextView fgMeTvNoHasSize;
    @BindView(R.id.fg_me_tv_money)
    TextView fgMeTvMoney;
    @BindView(R.id.fg_me_tv_state)
    TextView fgMeTvState;
    @BindView(R.id.fg_me_rv)
    RecyclerView fgMeRv;

    private String mParam1;
    private String mParam2;
    private NetReceiver netReceiver;
    private OnePixelReceiver onePixelReceiver;


    public MeFragment() {
    }

    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        EventBus.getDefault().register(this);

    }


    @Override
    public void onResume() {
        super.onResume();
        String ip = ShareUtils.getInstance(getActivity()).getString(Config.SipIp, Config.EMPTY);
        String account = ShareUtils.getInstance(getActivity()).getString(Config.SipAccount, Config.EMPTY);
        String pwd = ShareUtils.getInstance(getActivity()).getString(Config.SipPwd, Config.EMPTY);
        if (!TextUtils.isEmpty(ip) && !TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd)) {
            init_register(account, pwd, ip);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PjRegEvent event) {
        fgMeTvState.setText(getResources().getString(R.string.rgeState) + SipState.REGING.getMsg());
        com.orhanobut.logger.Logger.e("event : " + event);
        init_register(event.getAccount(), event.getPwd(), event.getIp());
    }

    /***
     * 注册sip
     * @param account
     * @param pwd
     * @param ip
     */
    public void init_register(String account, String pwd, String ip) {
        PJSipUtil.newInstance().register(getActivity(), account, pwd, ip, new OnPJSipRegStateListener() {
            @Override
            public void onSuccess() {
                com.orhanobut.logger.Logger.e("sip register onSuccess");
                fgMeTvState.setText(getResources().getString(R.string.rgeState) + SipState.SUCCESS.getMsg());
            }

            @Override
            public void onError() {
                com.orhanobut.logger.Logger.e("sip register onError");
                fgMeTvState.setText(getResources().getString(R.string.rgeState) + SipState.ERROR.getMsg());
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initGetData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initInject() {

    }


    @Override
    protected void initListener() {

        //heard info
        fgMeTvName.setText("孙天飞" + mParam1);
        ImgUtil.load(getActivity(), Config.logHeard, fgMeImgHeard);

        fgMeTvState.setText(getResources().getString(R.string.rgeState) + SipState.NOTREGISTER.getMsg());

        //消费信息
        fgMeTvHasSize.setText("120分钟");
        fgMeTvNoHasSize.setText("180分钟");
        fgMeTvMoney.setText("100元");
        init_RecyclerView();
        initRegisterNet();
        initOnePxAct();
    }


    private void init_RecyclerView() {

        //竖向
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        fgMeRv.setLayoutManager(mLayoutManager);
        fgMeRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        fgMeRv.setAdapter(new CommonAdapter<FgMeRvInfo>(getActivity(), R.layout.item_fg_me_rv, getRvDatas()) {
            @Override
            protected void convert(ViewHolder holder, FgMeRvInfo fgMeRvInfo, final int position) {
                holder.setImageResource(R.id.item_fg_me_rv_img, fgMeRvInfo.getImg());
                holder.setText(R.id.item_fg_me_rv_tv, fgMeRvInfo.getInfo());
                holder.setOnClickListener(R.id.item_fg_me_ll, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (position) {
                            case 0:
                                init_SipSetting();
                                break;
                            case 1:
                                init_SipCallSetting();
                                break;
                            case 2:
                                init_HotlineSipCallSetting();
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });

    }

    /**
     * @author stf
     * @time 2018-07-17 11:41
     * @remark 通话设置（热线）
     */
    private void init_HotlineSipCallSetting() {

    }

    /**
     * 通话设置（催收）
     */
    private void init_SipCallSetting() {
        startActivity(new Intent(getActivity(), SipCallSettingAct.class));
    }

    /***
     * sip设置
     */
    private void init_SipSetting() {
        startActivity(new Intent(getActivity(), SipSettingAct.class));
    }

    /***
     *
     * @return Rv 数据集合
     */
    private List<FgMeRvInfo> getRvDatas() {
        ArrayList<FgMeRvInfo> list = new ArrayList<>();
        list.add(new FgMeRvInfo.Builder().info(getResources().getString(R.string.item_fg_me_rv_sip)).img(R.mipmap.fg_me_rv_setting).build());
//        list.add(new FgMeRvInfo.Builder().info(getResources().getString(R.string.item_fg_me_rv_sip_music)).img(R.mipmap.fg_me_rv_setting_music).build());
        list.add(new FgMeRvInfo.Builder().info(getResources().getString(R.string.item_fg_me_rv_sip_callsetting)).img(R.mipmap.fg_me_rv_setting_sipcall).build());
        list.add(new FgMeRvInfo.Builder().info(getResources().getString(R.string.item_fg_me_rv_sip_callsetting2)).img(R.mipmap.fg_me_rv_setting_sipcall).build());

        return list;
    }

    /**
     * @author stf
     * @time 2018-07-18 14:14
     * @remark 注册网络变化检测广播
     */
    private void initRegisterNet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (netReceiver == null) {
                netReceiver = new NetReceiver();
                IntentFilter filter = new IntentFilter();
                filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
                filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                getActivity().registerReceiver(netReceiver, filter);
            }
        }
    }

    /**
     * @author stf
     * @time 2018-07-18 16:29
     * @remark 启动1像素的广播
     */
    private void initOnePxAct() {
        onePixelReceiver = new OnePixelReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        getActivity().registerReceiver(onePixelReceiver, intentFilter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

//        if (onePixelReceiver != null) {
//            getActivity().unregisterReceiver(onePixelReceiver);
//        }

    }
}
