package com.wheel.daniel.okhttputils.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.wheel.daniel.okhttputils.BaseActivity;
import com.wheel.daniel.okhttputils.R;
import com.wheel.daniel.okhttputils.utils.InjectUtil;
import com.wheel.daniel.okhttputils.utils.InjectView;
import com.wheel.daniel.okhttputils.utils.ToastUtils;

import butterknife.ButterKnife;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/14 10:13
 */
public class EventAvtivity extends BaseActivity {

    @InjectView(R.id.bt_toast)
    Button button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setContentViews() {
        setContentView(R.layout.activity_event);
        InjectUtil.injectViews(this, this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast("成功啦");
            }
        });
    }

    @Override
    protected void initDatas() {
        super.initDatas();
    }
}
