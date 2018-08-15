package com.cpsc.cpsc_pgsip.Enums;

/**
 * 描述:
 * <p>
 * Created by allens on 2018/1/30.
 */

public enum SipCallEnums {
    CALL_RING(1, "响铃就挂断"),
    CALL_CALL(2, "通话就挂断..."),;
    private int state;

    protected String msg;

    SipCallEnums(int state, String msg) {
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
