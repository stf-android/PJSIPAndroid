package com.cpsc.cpsc_pgsip.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.cpsc.cpsc_pgsip.Utils.ActivityContainer;

import butterknife.ButterKnife;

/**
 * 描述:
 * <p>
 *
 * @author allens
 * @date 2018/1/24
 */

public abstract class BaseAct extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityContainer.newInstance().addActivity(this);
        onCreate();
        initInject();
        initListener();
        initColor();
        init_Orientation();
    }

    /***
     * 竖屏显示
     */
    private void init_Orientation() {
        if (getRequestedOrientation() != 1) {
            setRequestedOrientation(1);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityContainer.newInstance().romveActivity(this);
    }


    private void initColor() {
        Window window = getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.BLACK);
        }

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
    }


    protected abstract void onCreate();

    protected abstract void initInject();

    protected abstract void initListener();

}
