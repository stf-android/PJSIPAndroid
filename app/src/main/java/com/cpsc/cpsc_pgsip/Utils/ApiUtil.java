package com.cpsc.cpsc_pgsip.Utils;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpsc.cpsc_pgsip.Config;
import com.cpsc.cpsc_pgsip.MyApp;

import java.util.HashMap;

/**
 * 描述:
 * <p>
 * Created by allens on 2018/1/24.
 */

public class ApiUtil {

    public static void back(final Activity activity, TextView textView, ImageView img, String titleInfo) {
        textView.setText(titleInfo);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    public static void back(final Activity activity, TextView textView, ImageView img, int titleInfoId) {
        textView.setText(activity.getResources().getString(titleInfoId));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }


    /***
     * 返回Token的map
     * @return
     */
    public static HashMap<String, String> getToken() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Accept", "application/json");
        map.put("Authorization", "jwt " + ShareUtils.getInstance(MyApp.content).getString(Config.Token, Config.EMPTY));
        return map;
    }

}
