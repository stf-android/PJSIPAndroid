package com.cpsc.cpsc_pgsip.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cpsc.cpsc_pgsip.Transformation.CircleTransform;

/**
 * 描述:
 * <p>
 *
 * @author allens
 * @date 2018/1/24
 */

public class ImgUtil {

    public static void load(Context context,
                            String url,
                            ImageView imageView,
                            RequestOptions options) {
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    /***
     * 加载圆形图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void load(Context context,
                            String url,
                            ImageView imageView) {

        RequestOptions options = new RequestOptions()
                .transforms(new CircleTransform(context, 1, Color.WHITE));
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }
}
