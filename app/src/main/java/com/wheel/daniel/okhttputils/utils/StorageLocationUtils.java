package com.wheel.daniel.okhttputils.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/12 14:01
 */
public class StorageLocationUtils {

    /**
     * Environment.getDataDirectory() ---/data
     *
     * @return
     */
    public static String getDataDirectory() {
        String path = Environment.getDataDirectory().getAbsolutePath();
        Log.v("daniel", "Environment.getDataDirectory()--" + path);
        return path;
    }

    /**
     * Environment.getExternalStorageDirectory():/storage/emulated/0
     *
     * @return
     */
    public static String getExternalStorageDirectory() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.v("daniel", "Environment.getExternalStorageDirectory()--" + path);
        return path;
    }


    /**
     * Environment.getRootDirectory():/system
     *
     * @return
     */
    public static String getRootDirectory() {
        String path = Environment.getRootDirectory().getAbsolutePath();
        Log.v("daniel", "Environment.getRootDirectory()--" + path);
        return path;
    }


    /**
     * getDownloadCacheDirectory():/cache
     *
     * @return
     */
    public static String getDownloadCacheDirectory() {
        String path = Environment.getDownloadCacheDirectory().getAbsolutePath();
        Log.v("daniel", "Environment.getDownloadCacheDirectory()--" + path);
        return path;
    }

    /**
     * Environment.getExternalStoragePublicDirectory():/storage/emulated/0/Alarms
     * 这个方法接收一个参数，表明目录所放的文件的类型，传入的参数是Environment类中的DIRECTORY_XXX静态变量，比如DIRECTORY_DCIM等.
     * 注意：传入的类型参数不能是null，返回的目录路径有可能不存在，所以必须在使用之前确认一下，比如使用File.mkdirs创建该路径。
     */

    public static String getExternalStoragePublicDirectory() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
        if (!path.exists()) {
            path.mkdirs();
        }
        Log.v("daniel", "Environment.getDownloadCacheDirectory()---" + path.getAbsolutePath());
        return path.getAbsolutePath();
    }


    /**
     * getCacheDir():/data/user/0/com.jaya.storagedemo/cache
     *
     * @param context
     * @return
     */
    public static String getCacheDir(Context context) {
        String path = context.getCacheDir().getAbsolutePath();
        Log.v("daniel", "getCacheDir---" + path);
        return path;
    }

    /**
     * getFilesDir():/data/user/0/com.jaya.storagedemo/files
     *
     * @param context
     * @return
     */
    public static String getFilesDir(Context context) {
        String path = context.getFilesDir().getAbsolutePath();
        Log.v("daniel", "getFilesDir---" + path);
        return path;
    }

    /**
     * getExternalCacheDir():/storage/emulated/0/Android/data/com.jaya.storagedemo/cache
     *
     * @param context
     * @return
     */
    public static String getExternalCacheDir(Context context) {
        String path = context.getExternalCacheDir().getAbsolutePath();
        Log.v("daniel", "getExternalCacheDir---" + path);
        return path;
    }

    /**
     * getExternalFilesDir():/storage/emulated/0/Android/data/com.jaya.storagedemo/files/DCIM
     *
     * @param context
     * @return
     */
    public static String getExternalFilesDir(Context context) {
        String path = context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath();
        Log.v("daniel", "getExternalFilesDir---" + path);
        return path;
    }

    /**
     * getPackageResourcePath():/data/app/com.jaya.storagedemo-2/base.apk
     *
     * @param context
     * @return
     */
    public static String getPackageResourcePath(Context context) {
        String path = context.getPackageResourcePath();
        Log.v("daniel", "getPackageResourcePath---" + path);
        return path;
    }

    /**
     * getDir():/data/user/0/com.jaya.storagedemo/app_myfile
     *
     * @param context
     * @return
     */
    public static String getDir(Context context) {
        String path = context.getDir("myfile", Context.MODE_PRIVATE).getAbsolutePath();
        Log.v("daniel", "getDir---" + path);
        return path;
    }

}
