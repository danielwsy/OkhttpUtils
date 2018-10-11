package com.wheel.daniel.okhttputils.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.wheel.daniel.okhttputils.BaseActivity;
import com.wheel.daniel.okhttputils.R;
import com.wheel.daniel.okhttputils.R2;
import com.wheel.daniel.okhttputils.bean.Gank;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/14 10:30
 */
public class MainActivity extends BaseActivity {


    @BindView(R2.id.gank)
    public Button btGank;

    @BindView(R2.id.event)
    public Button btEvent;

    @BindView(R2.id.down)
    public Button btDown;


    @OnClick({R.id.gank, R.id.event, R.id.down})
    public void onClick(Button button) {
        switch (button.getId()) {
            case R.id.gank:
                startActivity(new Intent(this, GankActivity.class));
                break;
            case R.id.event:
                startActivity(new Intent(this, EventAvtivity.class));
                break;
            case R.id.down:
                startActivity(new Intent(this, TaskManageActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setContentViews() {
        super.setContentViews();
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void BindButter() {
        super.BindButter();
    }

    @Override
    protected void initViews() {
        super.initViews();
    }

    @Override
    protected void initDatas() {
        super.initDatas();
    }
}
