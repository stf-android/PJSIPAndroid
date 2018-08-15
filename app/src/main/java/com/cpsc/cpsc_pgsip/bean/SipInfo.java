package com.cpsc.cpsc_pgsip.bean;

/**
 * 描述:
 * <p>
 * Created by allens on 2018/1/24.
 */

public class SipInfo {
    private String title;

    private String data;

    private SipInfo(Builder builder) {
        setTitle(builder.title);
        setData(builder.data);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public static final class Builder {
        private String title;
        private String data;

        public Builder() {
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder data(String val) {
            data = val;
            return this;
        }

        public SipInfo build() {
            return new SipInfo(this);
        }
    }
}
