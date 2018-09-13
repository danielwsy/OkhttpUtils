package com.wheel.daniel.downloder.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.wheel.daniel.downloder.callback.DownloadCallback;
import com.wheel.daniel.downloder.data.DownloadInfo;
import com.wheel.daniel.downloder.utils.FileUtils;

import static com.wheel.daniel.downloder.data.Status.CANCEL;
import static com.wheel.daniel.downloder.data.Status.DESTROY;
import static com.wheel.daniel.downloder.data.Status.ERROR;
import static com.wheel.daniel.downloder.data.Status.FINISH;
import static com.wheel.daniel.downloder.data.Status.NONE;
import static com.wheel.daniel.downloder.data.Status.PAUSE;
import static com.wheel.daniel.downloder.data.Status.PROGRESS;
import static com.wheel.daniel.downloder.data.Status.START;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/13 14:13
 */
public class UpDataHandler {

    private String url;
    private String path;
    private String name;
    private int childTaskCount;

    private Context mContext;

    private DownloadCallback mCallback;
    private DownloadInfo downloadInfo;

    private FileTask fileTask;

    private int mCurrentState = NONE;

    //是否支持断点续传
    private boolean isSupportRange;

    //重新开始下载需要先进行取消操作
    private boolean isNeedRestart;

    //记录已经下载的大小
    private int currentLength = 0;
    //记录文件总大小
    private int totalLength = 0;
    //记录已经暂停或取消的线程数
    private int tempChildTaskCount = 0;

    private long lastProgressTime;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mLastSate = mCurrentState;
            mCurrentState = msg.what;
            downloadInfo.setStatus(mCurrentState);
            switch (msg.what) {
                case START:
                    break;
                case PROGRESS:
                    synchronized (this) {
                        currentLength += msg.arg1;

                        downloadInfo.setPercentage(FileUtils.getPercentage(currentLength, totalLength));

                        if (mCallback != null && (System.currentTimeMillis() - lastProgressTime >= 20 || currentLength == totalLength)) {
                            mCallback.onProgress(currentLength, totalLength, FileUtils.getPercentage(currentLength, totalLength));
                            lastProgressTime = System.currentTimeMillis();
                        }

                        if (currentLength == totalLength) {
                            sendEmptyMessage(FINISH);
                        }
                    }

                    break;
                case CANCEL:
                    break;
                case PAUSE:
                    break;
                case FINISH:
                    break;
                case DESTROY:
                    break;
                case ERROR:
                    break;
            }
        }
    };

    public void setFileTask(FileTask fileTask) {
        this.fileTask = fileTask;
    }

    public int getCurrentState() {
        return mCurrentState;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public UpDataHandler(Context context, DownloadInfo downloadInfo, DownloadCallback callback) {
        mContext = context;
        mCallback = callback;
        this.downloadInfo = downloadInfo;
        this.url = downloadInfo.getUrl();
        this.path = downloadInfo.getPath();
        this.name = downloadInfo.getName();
        this.childTaskCount = downloadInfo.getChildTaskCount();
    }


    /**
     * 暂停（正在下载才可以暂停）
     * 如果文件不支持断点续传则不能进行暂停操作
     */
    public void pause() {
        if (mCurrentState == PROGRESS) {
            fileTask.pause();
        }
    }

    /**
     * 取消（已经被取消、下载结束则不可取消）
     */
    public void cancel(boolean isNeedRestart) {
        this.isNeedRestart = isNeedRestart;
        if (mCurrentState == PROGRESS) {
            fileTask.cancel();
        } else if (mCurrentState == PAUSE || mCurrentState == ERROR) {
            mHandler.sendEmptyMessage(CANCEL);
        }
    }

}
