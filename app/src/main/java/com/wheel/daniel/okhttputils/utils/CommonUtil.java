package com.wheel.daniel.okhttputils.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;

import com.wheel.daniel.okhttputils.HiApplication;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

public class CommonUtil {

    private static final String TAG = CommonUtil.class.getSimpleName();
    public static final String SHARED_NAME = "phone_util";
    public static final String IMEI_SHARED_NAME = "imei";
    public static final String USER_ID_FILE_NAME = "userId";

    @Nullable
    public static String getIMEI(Context context) {
        if (context == null)
            return "";
        @SuppressLint("WrongConstant") SharedPreferences sp = context.getSharedPreferences(SHARED_NAME, Context.MODE_APPEND);
        String imei = sp.getString(IMEI_SHARED_NAME, "");
        if (!imei.equals("")) {
            return imei;
        }
        //检查权限
        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            return "";
        }

        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        imei = tm.getDeviceId();
        //存储imei号
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(IMEI_SHARED_NAME, imei);
        editor.apply();
        return imei;
    }

    public static String getUserID(Context context) {
//        String uuid = getIMEI(context);
//        if ("".equals(uuid)){
//            uuid = getMacAddress();
//        }
//        return  Md5Util.getStringMd5(uuid);
        FileInputStream fi = null;
        String userId = null;
        try {
            fi = context.openFileInput(USER_ID_FILE_NAME);
            byte[] b = new byte[fi.available()];
            fi.read(b);
            fi.close();
            userId = Arrays.toString(b);
        } catch (Exception e) {

        }
        if (userId == null || userId.equals("")) {
//            context.getFileStreamPath(USER_ID_FILE_NAME);
//            userId = getInstanceId(context) + getGUID();
            userId = getGUID();
            FileOutputStream fo = null;
            try {
                fo = context.openFileOutput(USER_ID_FILE_NAME, Context.MODE_PRIVATE);
                fo.write(userId.getBytes());
                fo.flush();
                fo.close();
            } catch (Exception e) {

            }
        }
        return getStringMd5(userId);
    }


    public static String getStringMd5(String plainText) {

        MessageDigest md = null;

        try {

            md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());

        } catch (Exception e) {

            return null;

        }

        return encodeHex(md.digest());

    }

    public static String encodeHex(byte[] data) {

        if (data == null) {

            return null;
        }

        final String HEXES = "0123456789abcdef";
        int len = data.length;
        StringBuilder hex = new StringBuilder(len * 2);

        for (int i = 0; i < len; ++i) {

            hex.append(HEXES.charAt((data[i] & 0xF0) >>> 4));
            hex.append(HEXES.charAt((data[i] & 0x0F)));
        }

        return hex.toString();
    }

    public static String getGUID() {
        String guid = UUID.randomUUID().toString();
        return guid.replace("-", "");
    }

    public static String getLanguage(Context context) {
        return Locale.getDefault().getLanguage();
    }

    public static String getConuntry(Context context) {
        if (context != null && context.getResources() != null && context.getResources().getConfiguration().locale != null) {
            return context.getResources().getConfiguration().locale.getCountry();
        }
        return Locale.getDefault().getCountry();
    }


    public static String getOsVersionCode(Context context) {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    public static String getOsVersionName(Context context) {
        return Build.VERSION.RELEASE;
    }


    public static String getMetaData(Context context, String name) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        Object value = null;
        try {

            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), packageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(name);
            }

        } catch (Exception e) {

            return null;
        }

        return value == null ? null : value.toString();
    }

    public static String getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;
        return String.valueOf(width) + "*" + String.valueOf(height);
    }

    public static String getNetworkType(Context context, String defaultType) {
        String networkType = "";
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo info = cm.getActiveNetworkInfo();
                if (info != null) {
                    int type = info.getType();
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        networkType = "wifi";
                    } else {
                        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        if (tm != null) {
                            switch (tm.getNetworkType()) {
                                case TelephonyManager.NETWORK_TYPE_1xRTT:
                                case TelephonyManager.NETWORK_TYPE_EDGE:
                                case TelephonyManager.NETWORK_TYPE_GPRS:
                                case TelephonyManager.NETWORK_TYPE_CDMA:
                                case TelephonyManager.NETWORK_TYPE_IDEN:
                                    networkType = "2g";
                                    break;
                                case TelephonyManager.NETWORK_TYPE_EHRPD:
                                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                                case TelephonyManager.NETWORK_TYPE_HSPAP:
                                case TelephonyManager.NETWORK_TYPE_HSPA:
                                case TelephonyManager.NETWORK_TYPE_UMTS:
                                    networkType = "3g";
                                    break;
                                case TelephonyManager.NETWORK_TYPE_HSDPA:
                                case TelephonyManager.NETWORK_TYPE_HSUPA:
                                    networkType = "3.5g";
                                    break;
                                case TelephonyManager.NETWORK_TYPE_LTE:
                                    networkType = "4g";
                                    break;
                                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                                    networkType = "unknow";
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }

        return TextUtils.isEmpty(networkType) ? defaultType : networkType;
    }

    public static String getPkgName() {
        Context context = HiApplication.getContext();
        ComponentName cn = new ComponentName(context, context.getClass());
        return cn.getPackageName();
    }


    public static String getAppLang(Context context) {
        if (context != null && context.getResources() != null && context.getResources().getConfiguration().locale != null) {
            return context.getResources().getConfiguration().locale.getLanguage();
        }
        return "";
    }

}
