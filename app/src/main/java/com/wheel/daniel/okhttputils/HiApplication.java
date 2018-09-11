package com.wheel.daniel.okhttputils;

import android.app.Application;
import android.content.Context;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/7 17:03
 */
public class HiApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
