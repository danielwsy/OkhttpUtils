package com.wheel.daniel.okhttputils.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.wheel.daniel.okhttputils.R;
import com.wheel.daniel.okhttputils.bean.Gank;

import java.util.List;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/11 14:32
 */
public class GankAdapter extends RecyclerView.Adapter<BaseHolder> {
    private Context mContext;
    private List<Gank> mGanks;
    public static final int normalType = 0;     // 第一种ViewType，正常的item
    public static final int footType = 1;       // 第二种ViewType，底部的提示View

    public GankAdapter(Context context) {
        mContext = context;
    }

    public GankAdapter(Context context, List<Gank> ganks) {
        mContext = context;
        mGanks = ganks;
    }

    public void setDatas(List<Gank> ganks) {
        mGanks = ganks;
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.gank_item, null);
        BaseHolder holder = new NormalHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        Gank gank = mGanks.get(position);
        if (holder instanceof NormalHolder) {
            ((NormalHolder) holder).descText.setText(gank.desc);
            ((NormalHolder) holder).authorText.setText(gank.who);
            Glide.with(mContext).load(gank.url).asBitmap().into(((NormalHolder) holder).mImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mGanks == null ? 0 : mGanks.size();
    }


}
