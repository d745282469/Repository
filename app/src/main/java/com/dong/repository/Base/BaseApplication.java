package com.dong.repository.Base;

import android.app.Application;

import com.dong.repository.Util.Log;

/**
 * @author pd
 * time     2019/4/7 09:42
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.isDebug(true);//日志开关
    }
}
