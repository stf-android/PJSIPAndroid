package com.cpsc.cpsc_pgsip.Utils;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

/**
 * 选项卡
 * <p>
 * Created by Allens on 2016/9/4.
 */
public class Rg2FmUtils {
    private int showindex = 0;
    private int hideindex = 0;
    private List<Fragment> fragmentList;
    private FragmentManager supportFragmentManager;
    private int id;
    private RadioGroup radioGroup;

    /**
     * 1.构造方法私有化
     */
    private Rg2FmUtils() {
    }

    /**
     * 2.暴露出一个方法，返回当前类的对象
     */
    private static Rg2FmUtils mInstance;

    public static Rg2FmUtils getInstance() {
        if (mInstance == null) {
            //实例化对象
            //加上一个同步锁，只能有一个执行路径进入
            synchronized (Rg2FmUtils.class) {
                if (mInstance == null) {
                    mInstance = new Rg2FmUtils();
                }
            }
        }
        return mInstance;
    }


    /***
     *
     * @param fragmentList  碎片集合
     * @param radioGroup
     * @param supportFragmentManager
     * @param id  容器Id
     */
    public void tabControl(List<Fragment> fragmentList, RadioGroup radioGroup, FragmentManager supportFragmentManager, int id) {
        this.radioGroup = radioGroup;
        this.id = id;
        this.fragmentList = fragmentList;
        this.supportFragmentManager = supportFragmentManager;
        //初始化选中第一个
        ((RadioButton) radioGroup.getChildAt(showindex)).setChecked(true);
        //初始化碎片
        showFragment(showindex, hideindex);

        initsetOnClickListener();
    }

    /**
     * 作用：监听
     * name: Allens
     * created at 2016/9/4 10:23
     */
    private void initsetOnClickListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                radioButton.setChecked(true);
                int i = group.indexOfChild(radioButton);
                showFragment(i, hideindex);
                hideindex = i;
            }
        });
    }


    public boolean isAlreadyAdd() {
        if (fragmentList != null) {
            return true;
        }
        return false;
    }

    /***
     * 显示碎片的逻辑
     *
     * @param showindex
     * @param hideindex
     */
    private void showFragment(int showindex, int hideindex) {
        Fragment showfragment = fragmentList.get(showindex);
        Fragment hidefragment = fragmentList.get(hideindex);
        FragmentTransaction ft = supportFragmentManager.beginTransaction();
        if (!showfragment.isAdded()) {
            ft.add(id, showfragment);
        }
        if (showindex == hideindex) {
            ft.show(showfragment);
        } else {
            ft.show(showfragment);
            ft.hide(hidefragment);
        }
        ft.commit();
    }
}