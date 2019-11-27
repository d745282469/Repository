package com.dong.repository.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Create by AndroidStudio
 * Author: pd
 * Time: 2019/11/27 15:58
 */
public class NetWorkUtil {
    @SuppressLint("StaticFieldLeak")
    private static Activity topActivity = null;

    private static Application.ActivityLifecycleCallbacks lifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (topActivity != activity){
                topActivity = activity;
            }

            Log.d("dong", "topActivity=" + activity.getLocalClassName());
            if (isNetworkLost) {
                networkLost();
            } else {
                networkConnect();
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d("dong", "移除topActivity：" + activity.getLocalClassName());
            if (activity == topActivity){
                topActivity = null;//移除引用，防止内存泄露
            }
        }
    };

    private static TextView textView = null;
    private static boolean isNetworkLost = false;

    public static void init(Application application) {
        //内部调用的是add的方法，因此不怕会覆盖之前的
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks);

        ConnectivityManager manager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            manager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    //网络丢失
                    Log.d("dong", "当前线程：" + Thread.currentThread().getName());
                    networkLost();
                }

                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    networkConnect();
                }
            });
        }
    }

    private static void networkLost() {
        isNetworkLost = true;
        if (topActivity != null) {
            textView = new TextView(topActivity);
            textView.setText("网络丢失");
            textView.setTextSize(50);
            textView.setTextColor(Color.RED);
            textView.setPadding(0, 100, 0, 0);

            topActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ViewGroup root = (ViewGroup) topActivity.getWindow().getDecorView();
                    root.addView(textView);
                }
            });

        }
    }

    private static void networkConnect() {
        isNetworkLost = false;
        if (topActivity != null) {
            Log.d("dong", "移除添加的View");
            topActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ViewGroup root = (ViewGroup) topActivity.getWindow().getDecorView();
                    root.removeView(textView);
                }
            });
        }
    }

}
