package com.cpsc.cpsc_pgsip.act;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cpsc.cpsc_pgsip.Config;
import com.cpsc.cpsc_pgsip.Enums.FromEnums;
import com.cpsc.cpsc_pgsip.Enums.SipCallEnums;
import com.cpsc.cpsc_pgsip.R;
import com.cpsc.cpsc_pgsip.Utils.ApiUtil;
import com.cpsc.cpsc_pgsip.Utils.SerializableUtil;
import com.cpsc.cpsc_pgsip.base.BaseAct;
import com.cpsc.cpsc_pgsip.bean.SipCallInfo;
import com.cpsc.cpsc_pgsip.bean.SipInfo;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * <p>
 * 添加联系人，兵器设置他的通话天剑
 *
 * @author allens
 * @date 2018/1/29
 */

public class SipCallSettingAct extends BaseAct {

    @BindView(R.id.include_title_img)
    ImageView includeTitleImg;
    @BindView(R.id.include_title_tv)
    TextView includeTitleTv;
    @BindView(R.id.act_sipcall_rv)
    RecyclerView actSipcallRv;
    @BindView(R.id.act_sipcall_fat)
    FloatingActionButton actSipcallFat;


    /***
     * 通话方式
     */
    private int CallType;

    public static final String intentType = "data";
    private List<SipCallInfo> adapterData = new ArrayList<>();
    private String tvNumber;
    private String tvSize;
    private CommonAdapter<SipCallInfo> commonAdapter;

    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_sipcallsetting);
        ButterKnife.bind(this);
        Logger.i("onCreate", "SipCallSettingAct");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.i("onDestroy", "SipCallSettingAct");
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final SipCallInfo event) {
        SipCallInfo sipCallInfo = adapterData.get(event.getPos());

        int success = sipCallInfo.getSize() - event.getSize();
        sipCallInfo.setSuccessSize(success);
        commonAdapter.notifyItemChanged(event.getPos());
        SerializableUtil.write(SipCallSettingAct.this, adapterData);
        if (event.getSize() > 0) {
            Intent intent = new Intent(SipCallSettingAct.this, CallPhoneAct.class);
            intent.putExtra(Config.FROMTYPE, FromEnums.SipCallSettingAct.getState());
            intent.putExtra(intentType, event);
            startActivity(intent);
        }
    }


    @Override
    protected void initInject() {
        List<SipCallInfo> data = (List<SipCallInfo>) SerializableUtil.read(SipCallSettingAct.this);
        if (data != null) {
            adapterData = data;
        }
    }

    @Override
    protected void initListener() {
        ApiUtil.back(this, includeTitleTv, includeTitleImg, getResources().getString(R.string.item_fg_me_rv_sip_callsetting));
        init_adapter();
    }

    @OnClick(R.id.act_sipcall_fat)
    public void onViewClicked() {
        new MaterialDialog.Builder(this)
                .title(R.string.addCallNumber)
                .customView(R.layout.dialog_addcall, true)
                .positiveText(R.string.positive)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        View view = dialog.getCustomView();
                        EditText number = view.findViewById(R.id.dialog_et_number);
                        EditText size = view.findViewById(R.id.dialog_et_size);
                        RadioGroup radioGroup = view.findViewById(R.id.dialog_rg);


                        tvNumber = number.getText().toString();
                        if (TextUtils.isEmpty(tvNumber)) {
                            Snackbar.make(findViewById(R.id.CoordinatorLayout), R.string.please_number, Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        tvSize = size.getText().toString();
                        if (TextUtils.isEmpty(tvSize)) {
                            Snackbar.make(findViewById(R.id.CoordinatorLayout), R.string.please_call_size, Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        RadioButton childAt = (RadioButton) radioGroup.getChildAt(0);
                        boolean checked = childAt.isChecked();
                        RadioButton childAt1 = (RadioButton) radioGroup.getChildAt(1);
                        boolean checked1 = childAt1.isChecked();
                        if (checked) {
                            CallType = SipCallEnums.CALL_RING.getState();
                        } else if (checked1) {
                            CallType = SipCallEnums.CALL_CALL.getState();
                        }
                        getAdapterData();
                        if (commonAdapter != null) {
                            commonAdapter.notifyItemInserted(adapterData.size());
                            //通知数据与界面重新绑定
                            commonAdapter.notifyItemRangeChanged(adapterData.size() - 1, adapterData.size());
                        }
                        SerializableUtil.write(SipCallSettingAct.this, adapterData);
                    }
                })
                .show();
    }

    /***
     * 添加适配器
     */
    private void init_adapter() {

        //竖向
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        actSipcallRv.setLayoutManager(mLayoutManager);
        actSipcallRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        actSipcallRv.setItemAnimator(new DefaultItemAnimator());

        commonAdapter = new CommonAdapter<SipCallInfo>(this, R.layout.item_act_sipcall, adapterData) {
            @Override
            protected void convert(ViewHolder holder, final SipCallInfo sipInfo, final int position) {
                holder.setText(R.id.item_act_sipcall_tv_number, sipInfo.getNumber());
                holder.setText(R.id.item_act_sipcall_tv_success, getResources().getString(R.string.call_success) + sipInfo.getSuccessSize());
                holder.setText(R.id.item_act_sipcall_tv_size, getResources().getString(R.string.call_size) + sipInfo.getSize());
                if (sipInfo.getCallType() == (SipCallEnums.CALL_RING.getState())) {
                    holder.setText(R.id.item_act_sipcall_tv_calltype, getResources().getString(R.string.call_calltype) + getResources().getString(R.string.ring_to_end));
                } else {
                    holder.setText(R.id.item_act_sipcall_tv_calltype, getResources().getString(R.string.call_calltype) + getResources().getString(R.string.listener_to_end));
                }

                holder.setOnLongClickListener(R.id.item_act_sipcall_ll, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        init_remove(position);
                        return false;
                    }
                });

                holder.setOnClickListener(R.id.item_act_sipcall_ll, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


//                        Logger.i("sipInfo.getSize()  " + sipInfo.getSize());
//                        Logger.i("sipInfo.getSuccessSize()  " + sipInfo.getSuccessSize());
                        //需要呼叫的次数全部完成
                        if (sipInfo.getSize() > sipInfo.getSuccessSize()) {
                            Intent intent = new Intent(SipCallSettingAct.this, CallPhoneAct.class);
                            intent.putExtra(Config.FROMTYPE, FromEnums.SipCallSettingAct.getState());
                            SipCallInfo value = adapterData.get(position);
                            value.setPos(position);
                            intent.putExtra(intentType, value);
                            startActivity(intent);
                        } else {
                            Snackbar.make(findViewById(R.id.CoordinatorLayout), R.string.please_call_finish, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        actSipcallRv.setAdapter(commonAdapter);
    }

    /***
     * 删除
     * @param position
     */
    private void init_remove(final int position) {
        new MaterialDialog.Builder(this)
                .title(R.string.hint)
                .content(R.string.are_you_sure_to_delete)
                .positiveText(R.string.positive)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        adapterData.remove(position);
                        if (commonAdapter != null) {
                            commonAdapter.notifyItemRemoved(position);
                            //通知数据与界面重新绑定
                            commonAdapter.notifyItemRangeChanged(adapterData.size() - 1, adapterData.size());
                        }
                        SerializableUtil.write(SipCallSettingAct.this, adapterData);
                    }
                })
                .show();

    }


    /***
     * 获取adapter data
     * @return
     */
    private List<SipCallInfo> getAdapterData() {
        if (tvNumber != null) {
            adapterData.add(new SipCallInfo.Builder()
                    .number(tvNumber)
                    .size(Integer.parseInt(tvSize))
                    .callType(CallType)
                    .build());
        }
        return adapterData;
    }
}
