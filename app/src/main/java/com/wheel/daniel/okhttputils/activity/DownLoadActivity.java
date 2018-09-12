package com.wheel.daniel.okhttputils.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.wheel.daniel.okhttputils.BaseActivity;
import com.wheel.daniel.okhttputils.R;
import com.wheel.daniel.okhttputils.network.BaseHttpUtils;
import com.wheel.daniel.okhttputils.utils.CommonStringUtils;
import com.wheel.daniel.okhttputils.utils.StorageLocationUtils;
import com.wheel.daniel.okhttputils.utils.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/12 9:55
 */
public class DownLoadActivity extends BaseActivity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    String mUrl;
    String mName;
    private Button mBtStart, mBtLocation;
    private static final int DOWNFAILED = 101;
    private static final int DOWNSUCCEED = 102;
    File mFile;
    private ImageView imageView;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNFAILED:
                    ToastUtils.showToast("下载失败");
                    break;
                case DOWNSUCCEED:
                    ToastUtils.showToast("下载成功");
                    try {
                        FileInputStream stream = new FileInputStream(mFile);
                        Bitmap bitmap = BitmapFactory.decodeStream(stream);
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentViews() {
        super.setContentViews();
        setContentView(R.layout.activity_down);
    }

    @Override
    protected void initViews() {
        super.initViews();
        imageView = findViewById(R.id.iv_meinv);
        mBtStart = findViewById(R.id.bt_start);
        mBtLocation = findViewById(R.id.bt_down);
        mBtStart.setOnClickListener(this);
        mBtLocation.setOnClickListener(this);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        mUrl = getIntent().getStringExtra(CommonStringUtils.URL);
        mName = getIntent().getStringExtra(CommonStringUtils.NAME);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start:
                initProgress();
                if (!TextUtils.isEmpty(mUrl)) {
                    downLoad(mUrl);
                }
                break;
            case R.id.bt_down:
                test();
                break;
        }
    }

    private void test() {
        StorageLocationUtils.getDataDirectory();
        StorageLocationUtils.getExternalStorageDirectory();
        StorageLocationUtils.getRootDirectory();
        StorageLocationUtils.getDownloadCacheDirectory();
        StorageLocationUtils.getExternalStoragePublicDirectory();
        StorageLocationUtils.getCacheDir(this);
        StorageLocationUtils.getFilesDir(this);
        StorageLocationUtils.getExternalCacheDir(this);
        StorageLocationUtils.getExternalFilesDir(this);
        StorageLocationUtils.getPackageResourcePath(this);
        StorageLocationUtils.getDir(this);

    }

    private void downLoad(String url) {
        progressDialog.show();
        String path = StorageLocationUtils.getExternalCacheDir(this);;
        BaseHttpUtils.downLoad(url, path, mName + ".jpg", new BaseHttpUtils.OnDownloadListener() {
            @Override
            public void onDownloadFailed(Exception e) {
                mHandler.sendEmptyMessage(DOWNFAILED);
            }

            @Override
            public void onDownloading(int progress) {
                progressDialog.setProgress(progress);
            }

            @Override
            public void onDownloadSuccess(File file) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                mFile = file;
                mHandler.sendEmptyMessage(DOWNSUCCEED);
            }
        });
    }

    private void initProgress() {
        progressDialog = new ProgressDialog(DownLoadActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载");
        progressDialog.setMessage("请稍后...");
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
    }
}
