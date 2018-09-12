package com.wheel.daniel.okhttputils.activity;

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

public class MainActivity extends BaseActivity implements LifecycleOwner {

    private GankObserver observer;
    private GankModel gankModel;
    private LifecycleRegistry mLifecycleRegistry;
    private List<Gank> lists;
    private RecyclerView recyclerView;
    private GankAdapter mAdapter;
    private LoadingView mLoading;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
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
        mLifecycleRegistry = new LifecycleRegistry(this);
        observer = new GankObserver(this);
        mLifecycleRegistry.addObserver(observer);
        gankModel = ViewModelProviders.of(this).get(GankModel.class);
        gankModel.getValue().observe(this, mMovieObserver);
    }


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLifecycleRegistry.removeObserver(observer);
    }

}
