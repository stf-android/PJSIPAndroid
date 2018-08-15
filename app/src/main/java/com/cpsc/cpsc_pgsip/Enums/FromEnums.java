package com.cpsc.cpsc_pgsip.Enums;

/**
 * 描述:
 * <p>
 * <p>
 * 从哪个界面跳转到呼叫界面的
 * Created by allens on 2018/1/29.
 */

public enum FromEnums {
    MainAct(1, "MainAct"),
    CallAct(2, "CallAct"),
    SipCallSettingAct(3, "SipCallSettingAct");

    private int state;

    protected String msg;

    FromEnums(int state, String msg) {
        this.msg = msg;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public String getMsg() {
        return msg;
    }
}
