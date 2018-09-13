package com.wheel.daniel.okhttputils.adapter;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wheel.daniel.okhttputils.R;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/13 10:58
 */
public class TaskHolder extends BaseHolder {

    public TextView name, download_size, percentage, start, pause, resume, cancel, restart;
    public ProgressBar progress;

    public TaskHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        download_size = itemView.findViewById(R.id.download_size);
        percentage = itemView.findViewById(R.id.percentage);
        start = itemView.findViewById(R.id.start);
        pause = itemView.findViewById(R.id.pause);
        resume = itemView.findViewById(R.id.resume);
        cancel = itemView.findViewById(R.id.cancel);
        restart = itemView.findViewById(R.id.restart);
        progress = itemView.findViewById(R.id.progress_bar);
    }
}
