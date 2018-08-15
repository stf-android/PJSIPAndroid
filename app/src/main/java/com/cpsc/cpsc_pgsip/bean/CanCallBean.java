package com.cpsc.cpsc_pgsip.bean;

/**
 * 描述:
 * <p>
 * <p>
 * can 接口返回  是否能够打电话
 *
 * @author allens
 * @date 2018/1/30
 */

public class CanCallBean {


    /**
     * Result : 1
     * Message : 可以拨打
     */

    private int Result;
    private String Message;

    public int getResult() {
        return Result;
    }

    public void setResult(int Result) {
        this.Result = Result;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }
}
