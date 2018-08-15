package com.cpsc.cpsc_pgsip.bean;

/**
 * 描述:
 * <p>
 *
 * 个人中新界面 rv 中单项的数据对象
 * @author allens
 * @date 2018/1/24
 */

public class FgMeRvInfo {

    private String info;

    private int img;

    private FgMeRvInfo(Builder builder) {
        setInfo(builder.info);
        setImg(builder.img);
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }


    public static final class Builder {
        private String info;
        private int img;

        public Builder() {
        }

        public Builder info(String val) {
            info = val;
            return this;
        }

        public Builder img(int val) {
            img = val;
            return this;
        }

        public FgMeRvInfo build() {
            return new FgMeRvInfo(this);
        }
    }
}
