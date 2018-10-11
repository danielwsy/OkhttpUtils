package com.wheel.daniel.okhttputils.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wheel.daniel.downloder.data.DownloadInfo;
import com.wheel.daniel.downloder.db.DbDao;
import com.wheel.daniel.okhttputils.BaseActivity;
import com.wheel.daniel.okhttputils.R;
import com.wheel.daniel.okhttputils.adapter.TaskAdapter;
import com.wheel.daniel.okhttputils.utils.CommonStringUtils;
import com.wheel.daniel.okhttputils.utils.StorageLocationUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/12 17:36
 */
public class TaskManageActivity extends BaseActivity {


    private RecyclerView recyclerView;

    private TaskAdapter adapter;

    private List<DownloadInfo> mLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void BindButter() {
        super.BindButter();
    }

    @Override
    protected void setContentViews() {
        super.setContentViews();
        setContentView(R.layout.activity_task);
    }

    @Override
    protected void initViews() {
        super.initViews();
        recyclerView = findViewById(R.id.rc);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        mLists = CommonStringUtils.getData(this);
        adapter = new TaskAdapter(this, mLists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }
}
