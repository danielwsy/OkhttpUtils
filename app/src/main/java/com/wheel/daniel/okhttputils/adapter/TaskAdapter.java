package com.wheel.daniel.okhttputils.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.wheel.daniel.downloder.DownloadManger;
import com.wheel.daniel.downloder.callback.DownloadCallback;
import com.wheel.daniel.downloder.data.DownloadInfo;
import com.wheel.daniel.downloder.utils.FileUtils;
import com.wheel.daniel.okhttputils.R;

import java.io.File;
import java.util.List;
import java.util.zip.Inflater;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/13 10:56
 */
public class TaskAdapter extends RecyclerView.Adapter<BaseHolder> {

    private Context mContext;
    private List<DownloadInfo> mLists;

    public TaskAdapter(Context context, List<DownloadInfo> lists) {
        mContext = context;
        mLists = lists;
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_download_layout, null);
        BaseHolder holder = new TaskHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        DownloadInfo data = mLists.get(position);
        initHolder((TaskHolder) holder, data);
        setListener((TaskHolder) holder, data);
    }

    private void initHolder(TaskHolder holder, DownloadInfo data) {
        holder.name.setText(data.getName());
        holder.download_size.setText(FileUtils.formatSize(data.getCurrentLength()) + "/" + FileUtils.formatSize(data.getTotalLength()));
        holder.percentage.setText(data.getPercentage() + "%");
        holder.progress.setProgress((int) data.getPercentage());
        setItemListener(holder, data);
    }

    private void setItemListener(TaskHolder holder, final DownloadInfo data) {
        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManger.getInstance(mContext).start(data.getUrl());
            }
        });
        holder.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManger.getInstance(mContext).pause(data.getUrl());
            }
        });
        holder.resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManger.getInstance(mContext).resume(data.getUrl());
            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManger.getInstance(mContext).cancel(data.getUrl());
            }
        });
        holder.restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManger.getInstance(mContext).restart(data.getUrl());
            }
        });

    }

    private void setListener(TaskHolder holder, DownloadInfo data) {
        DownloadManger.getInstance(mContext).setOnDownloadCallback(data, new DownloadCallback() {
            @Override
            public void onStart(long currentSize, long totalSize, float progress) {


            }

            @Override
            public void onProgress(long currentSize, long totalSize, float progress) {

            }

            @Override
            public void onPause() {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFinish(File file) {

            }

            @Override
            public void onWait() {

            }

            @Override
            public void onError(String error) {

            }
        });


    }


    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }


}
