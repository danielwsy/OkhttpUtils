package com.wheel.daniel.okhttputils.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wheel.daniel.okhttputils.R;
import com.wheel.daniel.okhttputils.adapter.BaseHolder;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/11 15:52
 */
public class NormalHolder extends BaseHolder {
    public ImageView mImageView;
    public TextView descText;
    public TextView authorText;

    public NormalHolder(View itemView) {
        super(itemView);
        mImageView = (ImageView) itemView.findViewById(R.id.gank_iamge);
        descText = (TextView) itemView.findViewById(R.id.desc);
        authorText = (TextView) itemView.findViewById(R.id.author);
    }
}
