package com.wheel.daniel.okhttputils.adapter;

import android.view.View;
import android.widget.TextView;

import com.wheel.daniel.okhttputils.R;
import com.wheel.daniel.okhttputils.adapter.BaseHolder;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/11 15:56
 */
public class FootHolder extends BaseHolder {
    public TextView tips;

    public FootHolder(View itemView) {
        super(itemView);
        tips = (TextView) itemView.findViewById(R.id.tips);
    }
}
