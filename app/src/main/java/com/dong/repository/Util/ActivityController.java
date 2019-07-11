package com.dong.repository.Util;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pd
 * time     2019/4/7 09:24
 * 活动控制器，在BaseActivity中加入和移除集合
 */
public class ActivityController {
    private static final String TAG = "ActivityController";
    public static List<Activity> activityList = new ArrayList<>();

    /**
     * 新增活动，在活动onCreate时调用
     *
     * @param activity 活动
     */
    public static void addActivity(Activity activity) {
        activityList.add(activity);
        Log.d(TAG, activity.getClass().getName() + " is onCreate.");
    }

    /**
     * 根据活动对象移除活动，在活动onDestroy时调用
     *
     * @param activity 活动
     */
    public static void removeActivity(Activity activity) {
        activityList.remove(activity);
        Log.d(TAG, activity.getClass().getName() + " is onDestroy.");
    }

    /**
     * 根据索引移除活动
     *
     * @param index 索引
     * @return 成功返回true否则返回false
     */
    public static boolean removeActivity(int index) {
        if (index <= activityList.size() - 1) {
            Log.d(TAG, activityList.get(index).getClass().getName() + " is onDestroy.");
            activityList.remove(index);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得当前已启动的活动数量
     *
     * @return 已启动活动数量
     */
    public static int getActivityCount() {
        return activityList.size();
    }

    /**
     * 退出整个应用，原理就是根据已经打开的活动，一个个finish
     */
    public static void exitApplication() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
