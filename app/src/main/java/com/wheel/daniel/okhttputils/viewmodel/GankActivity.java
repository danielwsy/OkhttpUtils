package com.wheel.daniel.okhttputils.viewmodel;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.wheel.daniel.okhttputils.BaseActivity;
import com.wheel.daniel.okhttputils.R;
import com.wheel.daniel.okhttputils.bean.Gank;
import com.wheel.daniel.okhttputils.bean.GankContentBean;
import com.wheel.daniel.okhttputils.view.LoadingView;
import com.wheel.daniel.okhttputils.adapter.GankAdapter;
import com.wheel.daniel.okhttputils.view.MyItemDecoration;
import com.wheel.daniel.okhttputils.viewmodel.GankModel;
import com.wheel.daniel.okhttputils.viewmodel.GankObserver;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * activity/fragemnt 实现 LifecycleOwner 接口，
 * 重写 getLifecycle()方法返回一个 LifecycleRegistry 实例用来注册生命周期组件。
 */
public class GankActivity extends BaseActivity implements LifecycleOwner {

    private GankObserver observer;
    private GankModel gankModel;
    //用来注册生命周期组件
    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    private List<Gank> lists;
    private RecyclerView recyclerView;
    private GankAdapter mAdapter;
    private LoadingView mLoading;


    //请求图片观察者
    Observer<GankContentBean> mMovieObserver = new Observer<GankContentBean>() {
        @Override
        public void onChanged(@Nullable GankContentBean gankContentBean) {
            if (gankContentBean != null) {
                lists = gankContentBean.results;
                if (lists != null) {
                    mLoading.setVisibility(View.GONE);
                    mAdapter.setDatas(lists);
                    mAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(mAdapter);
                }
            }
        }
    };


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gank);
        //要考虑到主 activity 的 oncreate 等生命周期方法和组件绑定的方法执行先后顺序，避免
        //View 没初始化就请求绑定数据，造成一些空指针异常，所以需要在指定位置分发事件。
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        initView();
        initData();
    }

    @Override
    protected void BindButter() {
        super.BindButter();
    }


    private void initView() {
        recyclerView = findViewById(R.id.recycleview);
        mLoading = findViewById(R.id.loading);
        mLoading.setVisibility(View.VISIBLE);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new MyItemDecoration());
        mAdapter = new GankAdapter(this);
    }

    private void initData() {
        lists = new ArrayList<>();
        //注册观察者
        observer = new GankObserver(this);
        mLifecycleRegistry.addObserver(observer);
        //activity/fragment 也需要监听这个 ViewModel 对应的 LiveData 数据
        //这样业务模块也能成功的通知到界面组件进行数据刷新。
        gankModel = ViewModelProviders.of(this).get(GankModel.class);
        gankModel.getValue().observe(this, mMovieObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);
        mLifecycleRegistry.removeObserver(observer);
    }

}
