package com.wheel.daniel.okhttputils.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wheel.daniel.okhttputils.R;

/**
 * @author danielwang
 * @Description: 加载控件
 * @date 2018/9/11 15:11
 */
public class LoadingView extends LinearLayout {


    private Context mContext;
    private ImageView mIvLoadPart;
    private ObjectAnimator mAnim;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams paramsLyt = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(paramsLyt);
        View view = LayoutInflater.from(context).inflate(R.layout.loading, this);
        mIvLoadPart = (ImageView) view.findViewById(R.id.iv_loading_part);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            startAnim();
        } else {
            stopAnim();
        }
    }

    private void stopAnim() {
        if (mAnim != null) {
            mAnim.cancel();
        }
    }

    private void startAnim() {
        if (mAnim == null) {
            mAnim = ObjectAnimator.ofFloat(mIvLoadPart, "rotation", 0, 360);
        }
        mAnim.setDuration(1000);
        mAnim.setRepeatCount(-1);
        mAnim.setRepeatMode(ObjectAnimator.RESTART);
        mAnim.setInterpolator(new LinearInterpolator());
        mAnim.start();
    }
}
