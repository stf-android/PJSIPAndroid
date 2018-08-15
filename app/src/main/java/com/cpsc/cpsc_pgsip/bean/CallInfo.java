package com.cpsc.cpsc_pgsip.bean;

/**
 * 描述:
 * <p>
 *
 * 打电话界面数字建的值
 * @author allens
 * @date 2018/1/25
 */

public class CallInfo {

    private String number;

    private String letter;

    private CallInfo(Builder builder) {
        setNumber(builder.number);
        setLetter(builder.letter);
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }


    public static final class Builder {
        private String number;
        private String letter;

        public Builder() {
        }

        public Builder number(String val) {
            number = val;
            return this;
        }

        public Builder letter(String val) {
            letter = val;
            return this;
        }

        public CallInfo build() {
            return new CallInfo(this);
        }
    }
}
