package com.cpsc.cpsc_pgsip.Enums;

/**
 * 描述:
 * <p>
 *
 * @author allens
 * @date 2018/1/25
 */

public enum SipState {

    NOTREGISTER(1, "未注册"),
    REGING(4, "正在注册..."),
    SUCCESS(2, "注册成功"),
    ERROR(3, "注册失败");

    private int state;

    protected String msg;

    SipState(int state, String msg) {
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
