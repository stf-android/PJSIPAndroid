package com.cpsc.cpsc_pgsip.bean;

import java.io.Serializable;

/**
 * 描述:
 * <p>
 * 通话设置  对需要打电话的 信息详情
 *
 * @author allens
 * @date 2018/1/29
 */

public class SipCallInfo implements Serializable {


    private String number;

    private int size;

    /***
     * 通话类型
     */
    private int callType;

    private int successSize = 0;


    private int pos;

    private SipCallInfo(Builder builder) {
        setNumber(builder.number);
        setSize(builder.size);
        setCallType(builder.callType);
        setSuccessSize(builder.successSize);
        setPos(builder.pos);
    }


    public int getSuccessSize() {
        return successSize;
    }

    public void setSuccessSize(int successSize) {
        this.successSize = successSize;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }


    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }


    public static final class Builder {
        private String number;
        private int size;
        private int callType;
        private int successSize;
        private int pos;

        public Builder() {
        }

        public Builder number(String val) {
            number = val;
            return this;
        }

        public Builder size(int val) {
            size = val;
            return this;
        }

        public Builder callType(int val) {
            callType = val;
            return this;
        }

        public Builder successSize(int val) {
            successSize = val;
            return this;
        }

        public Builder pos(int val) {
            pos = val;
            return this;
        }

        public SipCallInfo build() {
            return new SipCallInfo(this);
        }
    }
}
