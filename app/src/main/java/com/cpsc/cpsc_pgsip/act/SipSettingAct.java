package com.cpsc.cpsc_pgsip.act;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cpsc.cpsc_pgsip.Config;
import com.cpsc.cpsc_pgsip.R;
import com.cpsc.cpsc_pgsip.Utils.PJSipUtil;
import com.cpsc.cpsc_pgsip.Utils.ShareUtils;
import com.cpsc.cpsc_pgsip.base.BaseAct;
import com.cpsc.cpsc_pgsip.bean.SipInfo;
import com.cpsc.cpsc_pgsip.dto.PjRegEvent;
import com.cpsc.cpsc_pgsip.pjsip.OnPJSipRegStateListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.pjsip.pjsua2.pjsip_status_code;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p>
 * <p>
 * sip设置
 *
 * @author allens
 * @date 2018/1/24
 */

public class SipSettingAct extends BaseAct {
    @BindView(R.id.include_title_img)
    ImageView includeTitleImg;
    @BindView(R.id.include_title_tv)
    TextView includeTitleTv;
    @BindView(R.id.act_sipSet_rv)
    RecyclerView actSipSetRv;

    private CommonAdapter<SipInfo> adapter;
    private ArrayList<SipInfo> sipInfos;

    //SIP 账号是否发生修改
    private Boolean isChangeData = false;

    /***
     * sip 设置界面的下标
     */
    private static final int SipIp = 0;
    private static final int SipAccount = 1;
    private static final int SipPwd = 2;

    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_sipsetting);
        ButterKnife.bind(this);
    }

    @Override
    protected void initInject() {

    }

    @Override
    protected void initListener() {
        init_Title();
        init_adapter();
    }

    /***
     * adapter
     */
    private void init_adapter() {
        //竖向
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        actSipSetRv.setLayoutManager(mLayoutManager);
        actSipSetRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        getRvDatas();

        adapter = new CommonAdapter<SipInfo>(this, R.layout.item_act_sipset_rv, sipInfos) {
            @Override
            protected void convert(ViewHolder holder, SipInfo sipInfo, final int position) {
                holder.setText(R.id.item_act_sipSet_tv_title, sipInfo.getTitle());
                holder.setText(R.id.item_act_sipSet_tv_info, sipInfo.getData());
                holder.setOnClickListener(R.id.item_act_sipSet_ll, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        init_Click(position);
                    }
                });

            }
        };
        actSipSetRv.setAdapter(adapter);
    }

    /**
     * title
     */
    private void init_Title() {
        includeTitleTv.setText(getResources().getString(R.string.item_fg_me_rv_sip));
        includeTitleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAct();
            }
        });
    }

    /**
     * 返回上级菜单
     */
    private void finishAct() {
        if (isChangeData) {
            new MaterialDialog.Builder(this)
                    .title(R.string.hint)
                    .content(R.string.SipIsChange)
                    .positiveText(R.string.positive)
                    .negativeText(R.string.negative)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            registerSip();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            finish();
                        }
                    })
                    .show();
        } else {
            finish();
        }
    }

    /***
     * 注册SIP
     */
    private void registerSip() {
        String ip = ShareUtils.getInstance(this).getString(Config.SipIp, Config.EMPTY);
        String account = ShareUtils.getInstance(this).getString(Config.SipAccount, Config.EMPTY);
        String pwd = ShareUtils.getInstance(this).getString(Config.SipPwd, Config.EMPTY);
        PjRegEvent event = new PjRegEvent.Builder()
                .account(account)
                .ip(ip)
                .pwd(pwd)
                .build();
        EventBus.getDefault().post(event);
        finish();
    }

    /***
     * 单相电机事件
     * @param position
     */
    private void init_Click(int position) {
        switch (position) {
            case SipIp:
                showEtDialog(position, getResources().getString(R.string.Sip_IP),
                        getResources().getString(R.string.sip_please_ip),
                        ShareUtils.getInstance(this).getString(Config.SipIp, Config.EMPTY));
                break;
            case SipAccount:
                showEtDialog(position, getResources().getString(R.string.Sip_Account),
                        getResources().getString(R.string.sip_please_account),
                        ShareUtils.getInstance(this).getString(Config.SipAccount, Config.EMPTY));
                break;
            case SipPwd:
                showEtDialog(position, getResources().getString(R.string.Sip_Pwd),
                        getResources().getString(R.string.sip_please_pwd),
                        ShareUtils.getInstance(this).getString(Config.SipPwd, Config.EMPTY));
                break;
            default:
                break;
        }
    }

    /***
     * 显示dilaog
     *
     * @param pos 下标
     * @param title
     * @param hint
     * @param prtefill 默认的数据
     */
    private void showEtDialog(final int pos, String title, String hint, String prtefill) {
        new MaterialDialog.Builder(SipSettingAct.this)
                .title(title)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(hint, prtefill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        saveSipInfo(pos, input.toString());
                    }
                }).show();
    }

    /***
     * 将数据保存在Share
     * @param pos
     * @param msg
     */
    private void saveSipInfo(int pos, String msg) {
        switch (pos) {
            case SipIp:
                ShareUtils.getInstance(this).putString(Config.SipIp, msg);
                sipInfos.get(pos).setData(ShareUtils.getInstance(this).getString(Config.SipIp, Config.EMPTY));
                break;
            case SipAccount:
                ShareUtils.getInstance(this).putString(Config.SipAccount, msg);
                sipInfos.get(pos).setData(ShareUtils.getInstance(this).getString(Config.SipAccount, Config.EMPTY));
                break;
            case SipPwd:
                ShareUtils.getInstance(this).putString(Config.SipPwd, msg);
                sipInfos.get(pos).setData(ShareUtils.getInstance(this).getString(Config.SipPwd, Config.EMPTY));
                break;
            default:
                break;
        }
        adapter.notifyItemChanged(pos, "fag");
        isChangeData = true;
    }


    /***
     *
     * 适配器中的数据
     */
    private void getRvDatas() {
        sipInfos = new ArrayList<>();
        sipInfos.add(new SipInfo.Builder()
                .title(getResources().getString(R.string.Sip_IP))
                .data(ShareUtils.getInstance(this).getString(Config.SipIp, Config.EMPTY))
                .build());
        sipInfos.add(new SipInfo.Builder()
                .title(getResources().getString(R.string.Sip_Account))
                .data(ShareUtils.getInstance(this).getString(Config.SipAccount, Config.EMPTY))
                .build());
        sipInfos.add(new SipInfo.Builder()
                .title(getResources().getString(R.string.Sip_Pwd))
                .data(ShareUtils.getInstance(this).getString(Config.SipPwd, Config.EMPTY))
                .build());
    }

    @Override
    public void onBackPressed() {
        finishAct();
    }
}
