package com.wheel.daniel.okhttputils.viewmodel;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

/**
 * @author danielwang
 * @Description: 观察图片信息的获取状态
 * <p>
 * 添加一个感知生命周期且处理固定业务逻辑的实体类，且该类实现 LifecycleObserver 接口。
 * 并且添加对应的方法绑定生命周期。
 * @date 2018/9/11 14:13
 */
public class GankObserver implements LifecycleObserver {

    private GankModel gankModel;
    private FragmentActivity mContenxt;

    public GankObserver(FragmentActivity contenxt) {
        mContenxt = contenxt;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        //当我们从网络获取到云端数据后，需要通知到 Activity/fragment 接收数据更新界面。
        ViewModelProvider viewModelProvider = ViewModelProviders.of(mContenxt);
        gankModel = viewModelProvider.get(GankModel.class);
        //拉取云端数据
        gankModel.loadData();
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
