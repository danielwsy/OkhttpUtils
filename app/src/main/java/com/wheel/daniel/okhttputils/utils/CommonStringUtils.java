package com.wheel.daniel.okhttputils.utils;

import android.content.Context;
import android.os.Environment;

import com.wheel.daniel.downloder.data.DownloadInfo;
import com.wheel.daniel.downloder.db.DbDao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/12 11:18
 */
public class CommonStringUtils {
    public static final String URL = "url";
    public static final String NAME = "name";

    public static final String url1 = "http://imtt.dd.qq.com/16891/DC9E925209B19E7913477E7A0CCE6E52.apk";//欢乐斗地主
    public static final String url2 = "http://imtt.dd.qq.com/16891/37F5264B6EDC71F9A7888B5017A5A6C1.apk";//球球大作战
    public static final String url3 = "http://imtt.dd.qq.com/16891/8AFB093FEFF9DE2A81EDC28EB1AF89C6.apk";//节奏大师
    public static final String url4 = "http://imtt.dd.qq.com/16891/72517F996BB172A75F992962849B051A.apk";//部落冲突
    public static final String url5 = "http://imtt.dd.qq.com/16891/1DF32C4166A703FC2AB7CCE33102123E.apk";//捕鱼达人

    public static final String path = Environment.getExternalStorageDirectory() + "/DUtil/";


    public static List<DownloadInfo> getData(Context context) {
        List<DownloadInfo> lists = new ArrayList<>();

        List<String> mUrl = new ArrayList<>();
        mUrl.add(url1);
        mUrl.add(url2);
        mUrl.add(url3);
        mUrl.add(url4);
        mUrl.add(url5);

        List<String> names = new ArrayList<>();
        names.add("欢乐斗地主.apk");
        names.add("球球大作战.apk");
        names.add("节奏大师.apk");
        names.add("部落冲突.apk");
        names.add("捕鱼达人.apk");

        for (int i = 0; i < mUrl.size(); i++) {
            if (DbDao.getInstance(context).getInfo(mUrl.get(i)) != null) {
                lists.add(DbDao.getInstance(context).getInfo(mUrl.get(i)));
            } else {
                lists.add(new DownloadInfo(mUrl.get(i), path, names.get(i)));
            }
        }

        return lists;
    }

}
