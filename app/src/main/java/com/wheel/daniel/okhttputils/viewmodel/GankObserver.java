package com.wheel.daniel.okhttputils.viewmodel;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

/**
 * @author danielwang
 * @Description: 观察图片信息的获取状态
 * @date 2018/9/11 14:13
 */
public class GankObserver implements LifecycleObserver {

    private GankModel gankModel;
    private FragmentActivity mContenxt;
    private Handler mHandler = new Handler();

    public GankObserver(FragmentActivity contenxt) {
        mContenxt = contenxt;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        gankModel = ViewModelProviders.of(mContenxt).get(GankModel.class);
        gankModel.loadData(mHandler);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
    }


}
