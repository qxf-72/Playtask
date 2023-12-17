package com.jnu.playtask.data;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 数据库配置信息
 *
 * @author Xiaofeng Qiu
 * create at 2023/12/14
 */
public class DBConfig {
    //  数据库名称
    public static final String DB_NAME = "PlayTask.db";
    //  数据库版本
    public static final int DB_VERSION = 1;


    /**
     * 判断数据表是否为空
     */
    @SuppressLint("Recycle")
    public static boolean HaveData(SQLiteDatabase db, String table_name) {
        Cursor cursor;
        boolean is_table_exist = false;
        cursor = db.rawQuery("select name from sqlite_master where type='table' ", null);
        while (cursor.moveToNext()) {
            // 遍历表名
            String name = cursor.getString(0);
            if (name.equals(table_name)) {
                is_table_exist = true;
            }
            Log.i("System.out", name);
        }
        if (is_table_exist) {
            cursor = db.query(table_name, null, null, null, null, null, null);
            // 检查是不是空表
            return cursor.getCount() > 0;
        } else {
            return false;
        }
    }
}

