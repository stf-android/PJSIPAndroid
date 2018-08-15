package com.cpsc.cpsc_pgsip.dto;

/**
 * 描述:
 * <p>
 * Pj sip  注册 event
 *
 * @author allens
 * @date 2018/1/25
 */

public class PjRegEvent {

    private String account;

    private String ip;

    private String pwd;

    private PjRegEvent(Builder builder) {
        setAccount(builder.account);
        setIp(builder.ip);
        setPwd(builder.pwd);
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    public static final class Builder {
        private String account;
        private String ip;
        private String pwd;

        public Builder() {
        }

        public Builder account(String val) {
            account = val;
            return this;
        }

        public Builder ip(String val) {
            ip = val;
            return this;
        }

        public Builder pwd(String val) {
            pwd = val;
            return this;
        }

        public PjRegEvent build() {
            return new PjRegEvent(this);
        }
    }
}
