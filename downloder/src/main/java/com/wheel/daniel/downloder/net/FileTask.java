package com.wheel.daniel.downloder.net;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.wheel.daniel.downloder.data.DownloadInfo;
import com.wheel.daniel.downloder.data.Ranges;
import com.wheel.daniel.downloder.db.DbDao;
import com.wheel.daniel.downloder.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.wheel.daniel.downloder.data.Status.CANCEL;
import static com.wheel.daniel.downloder.data.Status.DESTROY;
import static com.wheel.daniel.downloder.data.Status.ERROR;
import static com.wheel.daniel.downloder.data.Status.PAUSE;
import static com.wheel.daniel.downloder.data.Status.PROGRESS;
import static com.wheel.daniel.downloder.data.Status.START;
import static java.nio.channels.FileChannel.MapMode.READ_WRITE;

/**
 * @author danielwang
 * @Description: 对应文件的下载线程
 * @date 2018/9/13 14:26
 */
public class FileTask implements Runnable {

    private int EACH_TEMP_SIZE = 16;
    private int TEMP_FILE_TOTAL_SIZE;//临时文件的总大小

    private int BUFFER_SIZE = 4096;

    private Context mContext;

    private String url;
    private String path;
    private String name;
    private int childTaskCount;
    private Handler mHandler;

    private boolean is_pause;
    private boolean is_destroy;
    private boolean is_cancel;
    private ArrayList<Call> callList;

    private int tempChildTaskCount;


    public FileTask(Context context, DownloadInfo downloadInfo, Handler handler) {
        mContext = context;
        mHandler = handler;
        this.url = downloadInfo.getUrl();
        this.path = downloadInfo.getPath();
        this.name = downloadInfo.getName();
        this.childTaskCount = downloadInfo.getChildTaskCount();
    }

    @Override
    public void run() {
        File saveFile = new File(path, name);
        File tempFile = new File(path, name + ".temp");
        DownloadInfo data = DbDao.getInstance(mContext).getInfo(url);
        try {
            if (FileUtils.isFileExists(saveFile) && FileUtils.isFileExists(tempFile) && data != null && data.getStatus() != PROGRESS) {
                Response response = OkHttpManager.getInstance().initRequest(url, data.getLastModify());
                if (response != null && response.isSuccessful() && FileUtils.isNotServerFileChanged(response)) {
                    TEMP_FILE_TOTAL_SIZE = EACH_TEMP_SIZE * data.getChildTaskCount();
                    onStart(data.getTotalLength(), data.getCurrentLength(), "", true);
                } else {
                    prepareRangeFile(response);
                }
                saveRangeFile();
            } else {
                Response response = OkHttpManager.getInstance().initRequest(url);
                if (response != null && response.isSuccessful()) {
                    if (FileUtils.isSupportRange(response)) {
                        prepareRangeFile(response);
                        saveRangeFile();
                    } else {
                        saveCommonFile(response);
                    }
                }
            }

        } catch (Exception e) {
        }
    }

    private void saveCommonFile(Response response) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            long fileLength = response.body().contentLength();
            onStart(fileLength, 0, "", false);
            FileUtils.deleteFile(path, name);
            File file = FileUtils.createFile(path, name);
            if (file == null) {
                return;
            }
            inputStream = response.body().byteStream();
            outputStream = new FileOutputStream(file);
            int len;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((len = inputStream.read(buffer)) != -1) {
                if (is_cancel) {
                    mHandler.sendEmptyMessage(CANCEL);
                    break;
                }
                if (is_destroy) {
                    break;
                }
                outputStream.write(buffer, 0, len);
                onProgress(len);
            }

            outputStream.flush();
        } catch (Exception e) {
            onError(e.toString());
        } finally {
            FileUtils.close(inputStream);
            FileUtils.close(outputStream);
            FileUtils.close(response);
        }
    }

    private void saveRangeFile() {
        final File saveFile = FileUtils.createFile(path, name);
        final File tempFile = FileUtils.createFile(path, name + ".temp");

        final Ranges range = readDownloadRange(tempFile);

        callList = new ArrayList<>();

        DbDao.getInstance(mContext).updateProgress(0, 0, PROGRESS, url);

        for (int i = 0; i < childTaskCount; i++) {
            final int tempI = i;
            Call call = OkHttpManager.getInstance().initRequest(url, range.start[i], range.end[i], new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onError(e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    startSaveRangeFile(response, tempI, range, saveFile, tempFile);
                }
            });
            callList.add(call);
        }
    }

    /**
     * 分段保存文件
     *
     * @param response
     * @param index
     * @param range
     * @param saveFile
     * @param tempFile
     */
    private void startSaveRangeFile(Response response, int index, Ranges range, File saveFile, File tempFile) {
        RandomAccessFile saveRandomAccessFile = null;
        FileChannel saveChannel = null;
        InputStream inputStream = null;

        RandomAccessFile tempRandomAccessFile = null;
        FileChannel tempChannel = null;

        try {
            saveRandomAccessFile = new RandomAccessFile(saveFile, "rws");
            saveChannel = saveRandomAccessFile.getChannel();
            MappedByteBuffer saveBuffer = saveChannel.map(READ_WRITE, range.start[index], range.end[index] - range.start[index] + 1);

            tempRandomAccessFile = new RandomAccessFile(tempFile, "rws");
            tempChannel = tempRandomAccessFile.getChannel();
            MappedByteBuffer tempBuffer = tempChannel.map(READ_WRITE, 0, TEMP_FILE_TOTAL_SIZE);

            inputStream = response.body().byteStream();
            int len;
            byte[] buffer = new byte[BUFFER_SIZE];

            while ((len = inputStream.read(buffer)) != -1) {
                if (is_cancel) {
                    mHandler.sendEmptyMessage(CANCEL);
                    callList.get(index).cancel();
                    break;
                }

                saveBuffer.put(buffer, 0, len);
                tempBuffer.putLong(index * EACH_TEMP_SIZE, tempBuffer.getLong(index * EACH_TEMP_SIZE) + len);
                onProgress(len);

                if (is_destroy) {
                    mHandler.sendEmptyMessage(DESTROY);
                    callList.get(index).cancel();
                    break;
                }

                if (is_pause) {
                    mHandler.sendEmptyMessage(PAUSE);
                    callList.get(index).cancel();
                    break;
                }
            }
            addCount();
        } catch (Exception e) {
            onError(e.toString());
        } finally {
            FileUtils.close(saveRandomAccessFile);
            FileUtils.close(saveChannel);
            FileUtils.close(inputStream);
            FileUtils.close(tempRandomAccessFile);
            FileUtils.close(tempChannel);
            FileUtils.close(response);
        }
    }

    private synchronized void addCount() {
        ++tempChildTaskCount;
    }

    private void prepareRangeFile(Response response) {
        RandomAccessFile saveRandomAccessFile = null;
        RandomAccessFile tempRandomAccessFile = null;
        FileChannel tempChannel = null;
        File saveFile;
        File tempFile;

        try {

            saveFile = new File(path, name);
            tempFile = new File(path, name + ".temp");

            long fileLength = response.body().contentLength();
            onStart(fileLength, 0, FileUtils.getLastModify(response), true);

            DbDao.getInstance(mContext).deleteData(url);
            FileUtils.deleteFile(saveFile, tempFile);

            saveFile = FileUtils.createFile(path, name);
            tempFile = FileUtils.createFile(path, name + ".temp");

            saveRandomAccessFile = new RandomAccessFile(saveFile, "rws");
            saveRandomAccessFile.setLength(fileLength);

            tempRandomAccessFile = new RandomAccessFile(tempFile, "rws");
            tempRandomAccessFile.setLength(TEMP_FILE_TOTAL_SIZE);
            tempChannel = tempRandomAccessFile.getChannel();
            MappedByteBuffer buffer = tempChannel.map(READ_WRITE, 0, TEMP_FILE_TOTAL_SIZE);

            long start;
            long end;
            int eachSize = (int) (fileLength / childTaskCount);
            for (int i = 0; i < childTaskCount; i++) {
                if (i == childTaskCount - 1) {
                    start = i * eachSize;
                    end = fileLength - 1;
                } else {
                    start = i * eachSize;
                    end = (i + 1) * eachSize - 1;
                }
                buffer.putLong(start);
                buffer.putLong(end);
            }
        } catch (Exception e) {
            onError(e.toString());
        } finally {
            FileUtils.close(saveRandomAccessFile);
            FileUtils.close(tempRandomAccessFile);
            FileUtils.close(tempChannel);
            FileUtils.close(response);
        }
    }

    /**
     * 读取保存的断点信息
     *
     * @param tempFile
     * @return
     */
    public Ranges readDownloadRange(File tempFile) {
        RandomAccessFile record = null;
        FileChannel channel = null;
        try {
            record = new RandomAccessFile(tempFile, "rws");
            channel = record.getChannel();
            MappedByteBuffer buffer = channel.map(READ_WRITE, 0, TEMP_FILE_TOTAL_SIZE);
            long[] startByteArray = new long[childTaskCount];
            long[] endByteArray = new long[childTaskCount];
            for (int i = 0; i < childTaskCount; i++) {
                startByteArray[i] = buffer.getLong();
                endByteArray[i] = buffer.getLong();
            }
            return new Ranges(startByteArray, endByteArray);
        } catch (Exception e) {
            onError(e.toString());
        } finally {
            FileUtils.close(channel);
            FileUtils.close(record);
        }
        return null;
    }

    private void onStart(long totalLength, long currentLength, String lastModify, boolean isSupportRange) {
        Message message = Message.obtain();
        message.what = START;
        Bundle bundle = new Bundle();
        bundle.putInt("totalLength", (int) totalLength);
        bundle.putInt("currentLength", (int) currentLength);
        bundle.putString("lastModify", lastModify);
        bundle.putBoolean("isSupportRange", isSupportRange);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    public void onProgress(int length) {
        Message message = Message.obtain();
        message.what = PROGRESS;
        message.arg1 = length;
        mHandler.sendMessage(message);
    }

    public void onError(String msg) {
        Message message = Message.obtain();
        message.what = ERROR;
        message.obj = msg;
        mHandler.sendMessage(message);
    }

    public void pause() {
        is_pause = true;
    }

    public void cancel() {
        is_cancel = true;
    }

    public void destroy() {
        is_destroy = true;
    }
}
