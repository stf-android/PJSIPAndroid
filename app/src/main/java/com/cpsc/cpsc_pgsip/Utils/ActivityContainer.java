package com.cpsc.cpsc_pgsip.Utils;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/19.
 */

public class ActivityContainer {

    private List<Activity> activityList = new LinkedList<Activity>();
    private static ActivityContainer instance;


    public static ActivityContainer newInstance() {
        if (null == instance) {
            instance = new ActivityContainer();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }


    public void romveActivity(Activity activity) {
        activityList.remove(activity);
    }

    //遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
    }
}
