package com.wheel.daniel.downloder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/13 10:15
 */
public class DownDbHelper extends SQLiteOpenHelper {


    /**
     * 数据库版本
     */
    private static final int VERSION = 1;

    /**
     * 数据库名
     */
    private static final String DB_NAME = "othershe_dutil";

    public static final String DATABASE_FILTER_INFO = "download_info";


    private static volatile SQLiteDatabase db = null;
    private static int dbOpenCount = 0;

    public DownDbHelper(Context context, String name) {
        super(context, name, null, VERSION);
    }

    private static DownDbHelper getDatabaseHelper(Context context, String dbName) {

        return new DownDbHelper(context, dbName);
    }

    public static synchronized SQLiteDatabase get(Context context) {
        dbOpenCount++;
        if (db == null) {
            dbOpenCount = 1;
            db = getDatabaseHelper(context, DB_NAME)
                    .getWritableDatabase();
        }
        return db;
    }

    public static synchronized void close(SQLiteDatabase db) {
        dbOpenCount--;
        if (dbOpenCount <= 0) {
            dbOpenCount = 0;
            if (db != null) {
                db.close();
                DownDbHelper.db = null;
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_FILTER_INFO + "("
                + "id integer primary key autoincrement, "
                + "url text, "
                + "path text, "
                + "name text, "
                + "child_task_count integer, "
                + "current_length integer, "
                + "total_length integer, "
                + "percentage real, "
                + "status integer, "
                + "last_modify text, "
                + "date text"
                + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
