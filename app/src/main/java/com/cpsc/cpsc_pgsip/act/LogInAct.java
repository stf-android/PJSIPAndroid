package com.cpsc.cpsc_pgsip.act;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.allens.lib_retrofit.XRetrofit;
import com.allens.lib_retrofit.impl.OnRetrofit;
import com.bumptech.glide.Glide;
import com.cpsc.cpsc_pgsip.Config;
import com.cpsc.cpsc_pgsip.R;
import com.cpsc.cpsc_pgsip.Utils.ImgUtil;
import com.cpsc.cpsc_pgsip.Utils.ShareUtils;
import com.cpsc.cpsc_pgsip.base.BaseAct;
import com.cpsc.cpsc_pgsip.bean.LogInBean;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * <p>
 *
 * @author allens
 * @date 2018/1/24
 */

public class LogInAct extends BaseAct {
    @BindView(R.id.login_img_bg)
    ImageView loginImgBg;
    @BindView(R.id.login_img_heard)
    ImageView loginImgHeard;
    @BindView(R.id.login_et_account)
    EditText loginEtAccount;
    @BindView(R.id.login_et_password)
    EditText loginEtPassword;
    @BindView(R.id.login_btn)
    Button loginBtn;


    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void initInject() {

    }

    @Override
    protected void initListener() {
        init_setBgImg();
        init_setHeardImg();
        init_permission();
    }

    /***
     * 申请权限
     */
    private void init_permission() {
        //如果没有权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }
    }

    private void init_setHeardImg() {
        ImgUtil.load(this, Config.logHeard, loginImgHeard);
    }

    private void init_setBgImg() {
        Glide.with(this)
                .load(Config.logBg)
                .into(loginImgBg);
    }


    @OnClick(R.id.login_btn)
    public void onViewClicked() {
        String account = loginEtAccount.getText().toString();
        String pwd = loginEtPassword.getText().toString();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)) {
            Snackbar.make(loginBtn, R.string.login_error, Snackbar.LENGTH_SHORT).show();
            return;
        }
        init_IntentLogIn(account, pwd);
    }

    /***
     * login
     * @param account
     * @param pwd
     */
    private void init_IntentLogIn(final String account, final String pwd) {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(loginBtn, R.string.login_permission, Snackbar.LENGTH_SHORT).show();
            return;
        }
//        @SuppressLint("HardwareIds") final String deviceId = tm.getDeviceId();
//        XRetrofit.create()
//                .build(Config.BaseUrl)
//                .doPost(this, LogInBean.class, "/account/login", new OnRetrofit.OnQueryMapListener<LogInBean>() {
//                    @Override
//                    public void onMap(Map<String, String> map) {
//                        map.put("account", account);
//                        map.put("password", pwd);
//                        map.put("deviceId", deviceId);
//                    }
//
//                    @Override
//                    public void onSuccess(LogInBean logInBean) {
//                        if (logInBean.getResult() == Config.SUCCESS) {
//                            ShareUtils.getInstance(LogInAct.this).putString(Config.Token, logInBean.getToken());
                            startActivity(new Intent(LogInAct.this, MainActivity.class));
//                            finish();
//                        } else {
//                            Snackbar.make(loginBtn, logInBean.getMessage(), Snackbar.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        Snackbar.make(loginBtn, R.string.intent_error, Snackbar.LENGTH_SHORT).show();
//                    }
//                });
    }
}
