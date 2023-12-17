package com.jnu.android_demo.data;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Xiaofeng Qiu
 * create at 2023/12/14
 */
public class CountDAO {
    // 数据表名称
    public static final String TABLE_NAME = "count_info";

    // 表的字段名
    public static String KEY_ID = "id";
    public static String KEY_TIME = "time";
    public static String KEY_NAME = "name";
    public static String KEY_SCORE = "score";


    private SQLiteDatabase mDatabase;
    // 上下文
    private Context mContext;
    // 数据库打开帮助类
    private DBMaster.DBOpenHelper mDbOpenHelper;


    public CountDAO(Context context) {
        mContext = context;
    }

    public void setDatabase(SQLiteDatabase db) {
        mDatabase = db;
    }


    /**
     * 插入一条数据
     */
    @SuppressLint("SimpleDateFormat")
    public long insertData(CountItem countItem) {
        ContentValues values = new ContentValues();
        //  放入数据
        values.put(KEY_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(countItem.getTime()));
        values.put(KEY_NAME, countItem.getName());
        values.put(KEY_SCORE, countItem.getScore());

        return mDatabase.insert(TABLE_NAME, null, values);
    }


    /**
     * 删除一条数据
     */
    public long deleteData(int id) {
        return mDatabase.delete(TABLE_NAME, KEY_ID + "=" + id, null);
    }


    /**
     * 删除所有数据
     */
    public long deleteAllData() {
        return mDatabase.delete(TABLE_NAME, null, null);
    }


    /**
     * 更新一条数据
     */
    @SuppressLint("SimpleDateFormat")
    public long updateData(int id, CountItem countItem) {
        ContentValues values = new ContentValues();
        // 放入新的数据
        values.put(KEY_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(countItem.getTime()));
        values.put(KEY_NAME, countItem.getName());
        values.put(KEY_SCORE, countItem.getScore());

        return mDatabase.update(TABLE_NAME, values, KEY_ID + "=" + id, null);
    }


    /**
     * 根据日期查询当日数据
     */
    public ArrayList<CountItem> queryDataByDay(Timestamp time) {
        if (!DBConfig.HaveData(mDatabase, TABLE_NAME)) {
            return null;
        }
        // 将 Timestamp 转换为日期字符串
        @SuppressLint("SimpleDateFormat") String dateString = new SimpleDateFormat("yyyy-MM-dd").format(time);

        // 查询日期部分匹配的数据
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_TIME,
                        KEY_NAME,
                        KEY_SCORE
                },
                "SUBSTR(" + KEY_TIME + ", 1, 10) = ?", new String[]{dateString}, null, null, null);
        return convertUtil(results);
    }


    /**
     * 根据时间查询当周数据
     */
    public ArrayList<CountItem> queryDataByWeek(Timestamp time) {
        if (!DBConfig.HaveData(mDatabase, TABLE_NAME)) {
            return null;
        }

        // 获取传入时间所在周的起始日期（周一）和结束日期（周日）
        LocalDateTime localDateTime = time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime startDate = localDateTime.with(DayOfWeek.MONDAY);
        LocalDateTime endDate = localDateTime.with(DayOfWeek.SUNDAY);

        Date startDateDate = java.util.Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        Date endDateDate = java.util.Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());


        // 将日期转换为字符串
        // 使用 DateTimeFormatter 格式化 LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateString = startDate.format(formatter);
        String endDateString = endDate.format(formatter);

        // 查询日期范围内的数据
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_TIME,
                        KEY_NAME,
                        KEY_SCORE
                },
                KEY_TIME + " BETWEEN ? AND ?",
                new String[]{startDateString + " 00:00:00", endDateString + " 23:59:59"},
                null, null, null);

        return convertUtil(results);
    }


    /**
     * 根据时间查询当月数据，按时间降序排列
     */
    public ArrayList<CountItem> queryDataByMonth(Timestamp time) {
        if (!DBConfig.HaveData(mDatabase, TABLE_NAME)) {
            return null;
        }

        // 获取传入时间所在月的年月
        LocalDateTime localDateTime = time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // 获取传入时间所在月的年月
        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();

        // 查询年月范围内的数据
        @SuppressLint("DefaultLocale") Cursor results = mDatabase.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_TIME,
                        KEY_NAME,
                        KEY_SCORE
                },
                "strftime('%Y-%m', " + KEY_TIME + ") = ?",
                new String[]{String.format("%04d-%02d", year, month)},
                null, null, KEY_TIME + " DESC");

        return convertUtil(results);
    }


    /**
     * 根据时间查询当年数据
     */
    public ArrayList<CountItem> queryDataByYear(Timestamp time) {
        if (!DBConfig.HaveData(mDatabase, TABLE_NAME)) {
            return null;
        }

        // 获取传入时间所在年的年份
        LocalDateTime localDateTime = time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        int year = localDateTime.getYear();

        // 查询年份范围内的数据
        @SuppressLint("DefaultLocale") Cursor results = mDatabase.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_TIME,
                        KEY_NAME,
                        KEY_SCORE
                },
                "strftime('%Y', " + KEY_TIME + ") = ?",
                new String[]{String.format("%04d", year)},
                null, null, null);

        return convertUtil(results);
    }


    /**
     * 根据id查询一条数据
     */
    public ArrayList<CountItem> queryData(int id) {
        if (!DBConfig.HaveData(mDatabase, TABLE_NAME)) {
            return null;
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_TIME,
                        KEY_NAME,
                        KEY_SCORE
                },
                KEY_ID + "=" + id, null, null, null, null);
        return convertUtil(results);
    }


    /**
     * 查询所有数据
     */
    public ArrayList<CountItem> queryDataList() {
        if (!DBConfig.HaveData(mDatabase, TABLE_NAME)) {
            return null;
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_TIME,
                        KEY_NAME,
                        KEY_SCORE
                },
                null, null, null, null, null);
        return convertUtil(results);
    }


    /**
     * 查询结果转换
     */
    @SuppressLint("Range")
    private ArrayList<CountItem> convertUtil(Cursor cursor) {
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return null;
        }

        ArrayList<CountItem> mList = new ArrayList<>();

        for (int i = 0; i < resultCounts; i++) {
            CountItem taskItem = new CountItem();
            // 将cursor当前的数据放入taskItem中
            taskItem.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            taskItem.setTime(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(KEY_TIME))));
            taskItem.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            taskItem.setScore(cursor.getInt(cursor.getColumnIndex(KEY_SCORE)));

            mList.add(taskItem);
            cursor.moveToNext();
        }
        return mList;
    }
}
