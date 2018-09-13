package com.wheel.daniel.downloder.callback;

import java.io.File;

/**
 * @author danielwang
 * @Description: 下载状态
 * @date 2018/9/12 16:38
 */
public interface DownloadCallback {

    /**
     * 开始
     */
    void onStart(long currentSize, long totalSize, float progress);

    /**
     * 下载中
     *
     * @param currentSize
     * @param totalSize
     * @param progress
     */
    void onProgress(long currentSize, long totalSize, float progress);

    /**
     * 暂停
     */
    void onPause();

    /**
     * 取消
     */
    void onCancel();

    /**
     * 完成
     *
     * @param file
     */
    void onFinish(File file);

    /**
     * 等待下载
     */
    void onWait();

    /**
     * 出错
     *
     * @param error
     */
    void onError(String error);
}
