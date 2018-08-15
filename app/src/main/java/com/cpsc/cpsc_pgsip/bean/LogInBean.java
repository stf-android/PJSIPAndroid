package com.cpsc.cpsc_pgsip.bean;

/**
 * 描述:
 * <p>
 * <p>
 * 登录接口返回
 *
 * @author allens
 * @date 2018/1/30
 */

public class LogInBean {


    /**
     * Token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJVaWQiOiIxMjMxMjMxMjMiLCJBY2NvdW50IjoiYWRtaW4ifQ.rJmKVPGm781oMWUrU2HGnTSE7q4JQ63iQ8e5XpAzDi8
     * Result : 1
     * Message : 登录成功！
     */

    private String Token;
    private int Result;
    private String Message;

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

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
