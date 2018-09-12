package com.wheel.daniel.okhttputils.utils;


import android.text.TextUtils;
import android.widget.Toast;

import com.wheel.daniel.okhttputils.HiApplication;


/**
 * @author danielwang
 * @Description:
 * @date 2018/9/12 10:15
 */
public class ToastUtils {
    public static void showToast(String words) {
        if (TextUtils.isEmpty(words)) return;
        Toast.makeText(HiApplication.getContext(), words, Toast.LENGTH_SHORT).show();
    }
}
