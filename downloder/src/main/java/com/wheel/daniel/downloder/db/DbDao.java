package com.wheel.daniel.downloder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wheel.daniel.downloder.data.DownloadInfo;

import java.util.ArrayList;
import java.util.List;

import static com.wheel.daniel.downloder.data.Status.PROGRESS;
import static com.wheel.daniel.downloder.db.DownDbHelper.DATABASE_FILTER_INFO;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/13 10:27
 */
public class DbDao {

    private SQLiteDatabase dbHelper;
    private static DbDao dbDao;


    public DbDao(Context context) {
        dbHelper = DownDbHelper.get(context);
    }

    public static DbDao getInstance(Context context) {

        if (dbDao == null) {
            synchronized (DbDao.class) {
                dbDao = new DbDao(context);
            }
        }
        return dbDao;
    }

    /**
     * 保存下载信息
     */
    public void insertData(DownloadInfo data) {
        ContentValues values = new ContentValues();
        values.put("url", data.getUrl());
        values.put("path", data.getPath());
        values.put("name", data.getName());
        values.put("child_task_count", data.getChildTaskCount());
        values.put("current_length", data.getCurrentLength());
        values.put("total_length", data.getTotalLength());
        values.put("percentage", data.getPercentage());
        values.put("status", data.getStatus());
        values.put("last_modify", data.getLastModify());
        values.put("date", data.getDate());
        dbHelper.insert(DATABASE_FILTER_INFO, null, values);

        DownDbHelper.close(dbHelper);
    }


    /**
     * 获得url对应的下载数据
     */
    public DownloadInfo getInfo(String url) {
        Cursor cursor = dbHelper.query(DATABASE_FILTER_INFO, null, "url = ?",
                new String[]{url}, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        DownloadInfo data = new DownloadInfo();
        data.setUrl(cursor.getString(cursor.getColumnIndex("url")));
        data.setPath(cursor.getString(cursor.getColumnIndex("path")));
        data.setName(cursor.getString(cursor.getColumnIndex("name")));
        data.setChildTaskCount(cursor.getInt(cursor.getColumnIndex("child_task_count")));
        data.setCurrentLength(cursor.getInt(cursor.getColumnIndex("current_length")));
        data.setTotalLength(cursor.getInt(cursor.getColumnIndex("total_length")));
        data.setPercentage(cursor.getFloat(cursor.getColumnIndex("percentage")));
        data.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
        data.setLastModify(cursor.getString(cursor.getColumnIndex("last_modify")));
        data.setDate(cursor.getInt(cursor.getColumnIndex("date")));

        cursor.close();
        DownDbHelper.close(dbHelper);

        return data;
    }


    /**
     * 获得全部下载数据
     *
     * @return
     */
    public List<DownloadInfo> getAllData() {
        List<DownloadInfo> list = new ArrayList<>();
        Cursor cursor = dbHelper.query(DATABASE_FILTER_INFO, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                DownloadInfo data = new DownloadInfo();
                data.setUrl(cursor.getString(cursor.getColumnIndex("url")));
                data.setPath(cursor.getString(cursor.getColumnIndex("path")));
                data.setName(cursor.getString(cursor.getColumnIndex("name")));
                data.setChildTaskCount(cursor.getInt(cursor.getColumnIndex("child_task_count")));
                data.setCurrentLength(cursor.getInt(cursor.getColumnIndex("current_length")));
                data.setTotalLength(cursor.getInt(cursor.getColumnIndex("total_length")));
                data.setPercentage(cursor.getFloat(cursor.getColumnIndex("percentage")));
                data.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                data.setLastModify(cursor.getString(cursor.getColumnIndex("last_modify")));
                data.setDate(cursor.getInt(cursor.getColumnIndex("date")));

                list.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DownDbHelper.close(dbHelper);
        return list;
    }

    /**
     * 更新下载信息
     */
    public void updateProgress(int currentSize, float percentage, int status, String url) {
        ContentValues values = new ContentValues();
        if (status != PROGRESS) {
            values.put("current_length", currentSize);
            values.put("percentage", percentage);
        }
        values.put("status", status);
        dbHelper.update(DATABASE_FILTER_INFO, values, "url = ?", new String[]{url});
        DownDbHelper.close(dbHelper);
    }

    /**
     * 删除下载信息
     */
    public void deleteData(String url) {
        dbHelper.delete(DATABASE_FILTER_INFO, "url = ?", new String[]{url});
        DownDbHelper.close(dbHelper);
    }

}
