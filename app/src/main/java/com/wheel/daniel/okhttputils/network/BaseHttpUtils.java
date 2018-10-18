package com.wheel.daniel.okhttputils.network;

import android.content.Context;
import android.os.Environment;

import com.wheel.daniel.okhttputils.HiApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author danielwang
 * @Description: 网络模块统一管理
 * @date 2018/9/11 10:43
 */
public class BaseHttpUtils {

    public static final int CACHE_SIZE = 4 * 1024 * 1024; //cache size
    public static final int NETWORK_TIME_OUT = 60; //network time out

    private static OkHttpClient okHttpClient;
    private String mServerUrl;
    private Context mContext;
    private Retrofit mRetrofit;
    public static Hashtable<String, BaseHttpUtils> cacheMap = new Hashtable<>();
    //space+默认不进行压缩，鹰眼api时候需要设置为true
    private static boolean isGzipEncode = false;

    public static BaseHttpUtils getInstance(String serverUrl) {
        BaseHttpUtils baseHttpUtils = cacheMap.get(serverUrl);
        if (baseHttpUtils == null) {
            synchronized (BaseHttpUtils.class) {
                if (baseHttpUtils == null) {
                    baseHttpUtils = new BaseHttpUtils(serverUrl);
                    cacheMap.put(serverUrl, baseHttpUtils);
                }
            }
        }
        return baseHttpUtils;
    }

    public BaseHttpUtils(String serverUrl) {
        mContext = HiApplication.getContext();
        mServerUrl = serverUrl;
    }

    public Retrofit getRetrofit() {
        if (mRetrofit == null) {
            synchronized (BaseHttpUtils.class) {
                if (mRetrofit == null) {
                    mRetrofit = initDefault();
                }
            }
        }
        return mRetrofit;
    }

    /**
     * Retrofit 和 OKHttp进行结合
     *
     * @return
     */
    private Retrofit initDefault() {
        Retrofit.Builder builder = new Retrofit.Builder();
        if (okHttpClient == null) {
            OkHttpClient.Builder okBuilder = buildDefalutClient(mContext);
            okHttpClient = okBuilder.build();
        }
        builder.client(okHttpClient);
        builder.addConverterFactory(new StringConverterFactory());
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.baseUrl(mServerUrl);
        return builder.build();
    }

    private static OkHttpClient.Builder buildDefalutClient(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(new Cache(context.getCacheDir(), CACHE_SIZE));
        builder.addInterceptor(new DefaultCacheInterceptor(context, isGzipEncode));
        builder.connectTimeout(NETWORK_TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(NETWORK_TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(NETWORK_TIME_OUT, TimeUnit.SECONDS);
        return builder;
    }

    /**
     * 下载文件
     *
     * @param url          下载的链接
     * @param destFileName 下载文件名称
     * @param listener     下载监听
     */
    public static void downLoad(String url, final String destFileDir, final String destFileName, final OnDownloadListener listener) {
        if (okHttpClient == null) {
            OkHttpClient.Builder okBuilder = buildDefalutClient(HiApplication.getContext());
            okHttpClient = okBuilder.build();
        }
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onDownloadFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onDownLoad(response, destFileDir, destFileName, listener);
            }
        });
    }

    private static void onDownLoad(Response response, String destFileDir, String destFileName, OnDownloadListener listener) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        // 储存下载文件的目录
        File dir = new File(destFileDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, destFileName);
        try {
            is = response.body().byteStream();
            long total = response.body().contentLength();
            fos = new FileOutputStream(file);
            long sum = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
                int progress = (int) (sum * 1.0f / total * 100);
                // 下载中更新进度条
                listener.onDownloading(progress);
            }
            fos.flush();
            // 下载完成
            listener.onDownloadSuccess(file);
        } catch (Exception e) {
            listener.onDownloadFailed(e);
        } finally {
            if (is != null) {
                is.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    public interface OnDownloadListener {

        /**
         * @param e 下载异常信息
         */
        void onDownloadFailed(Exception e);

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * @param file 下载成功后的文件
         */
        void onDownloadSuccess(File file);


    }

}
