package com.dong.repository.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.List;

/**
 * @author pd
 * time     2019/4/7 11:28
 * 一些无法分类的工具
 */
public class CommonUtil {
    private static final String TAG = "CommonUtil";

    /**
     * 设置屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public static void setBackgroundAlpha(float bgAlpha, Activity mContext) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    /**
     * 获取WIFI_ssid也就是WIFI的名称
     * 需要权限：ACCESS_NETWORK_STATE，ACCESS_FINE_LOCATION，
     * ACCESS_COARSE_LOCATION，CHANGE_WIFI_STATE
     *
     * @param context 上下文
     * @return WIFI 的SSID
     */
    public static String GetWIFISSID(Context context) {
        String ssid = "";
        if (context != null) {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            if (wifiInfo != null) {
                ssid = wifiInfo.getSSID();//获取到的SSID可能带双引号，所以要去掉
                if (ssid.substring(0, 1).equals("\"") && ssid.substring(ssid.length() - 1).equals("\"")) {
                    ssid = ssid.substring(1, ssid.length() - 1);
                }
            }
        }
        return ssid;
    }

    /**
     * 调整列表高度，让列表只显示itemCount个的高度，需要知道子项的高度才可以
     *
     * @param rv         要调整的列表
     * @param itemCount  要显示的item数量
     * @param list       数据源
     * @param itemHeight 一个子项的高度
     * @param context    上下文环境
     */
    public static void adjustListHeight(RecyclerView rv, int itemCount, List list, int itemHeight, Context context) {
        ViewGroup.LayoutParams layoutParams = rv.getLayoutParams();
        if (list.size() > itemCount) {
            layoutParams.height = UnitUtil.dip2px(context, itemHeight * itemCount);
        } else {
            layoutParams.height = UnitUtil.dip2px(context, itemHeight * list.size());
        }
        rv.setLayoutParams(layoutParams);
    }

    /**
     * 获取屏幕宽高
     *
     * @param context 用来获取Resource的上下文环境
     * @return 数组，[宽度，高度]，单位px
     */
    public static int[] getScreenWH(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
//        float density = dm.density;//密度
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        return new int[]{width, height};
    }

    /**
     * 显示/隐藏软键盘
     */
    private void toggleInput(Context context) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 强制隐藏软键盘
     *
     * @param view 用于获取WindowToken
     */
    private void hideInput(Context context, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 检测辅助功能是否开启
     *
     * @param mContext    上下文环境
     * @param helpService 辅助功能服务类
     * @return boolean
     */
    public static boolean isAccessibilitySettingsOn(Context mContext, Class helpService) {
        int accessibilityEnabled = 0;
        // HelpService为对应的服务
        final String service = mContext.getPackageName() + "/" + helpService.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            android.util.Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    android.util.Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        android.util.Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }

    /**
     * 判断是否有悬浮窗权限
     *
     * @param context 上下文
     * @return true/false
     */
    public static boolean canDrawOverLays(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else {
            return true;
        }
    }

    /**
     * 跳转去授权悬浮窗界面
     *
     * @param context 上下文用来跳转活动
     */
    public static void getOverLayPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
        }
    }
}
