package com.wheel.daniel.okhttputils;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/12 9:56
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViews();
        initViews();
        initDatas();

    }

    protected void setContentViews() {
    }

    protected void initDatas() {
    }

    protected void initViews() {
    }


}
