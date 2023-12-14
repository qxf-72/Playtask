package com.jnu.android_demo.data;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Task类的数据库操作类
 *
 * @author Xiaofeng Qiu
 * create at 2023/12/14
 */
public class TaskDAO {
    // 数据表名称
    public static final String TABLE_NAME = "task_info";

    // 表的字段名
    public static String KEY_ID = "id";
    public static String KEY_TIME = "time";
    public static String KEY_NAME = "name";
    public static String KEY_SCORE = "score";
    public static String KEY_TYPE = "type";
    public static String KEY_TOTAL_AMOUNT = "total_amount";
    public static String KEY_FINISHED_AMOUNT = "finished_amount";

    private SQLiteDatabase mDatabase;
    // 上下文
    private Context mContext;
    // 数据库打开帮助类
    private DBMaster.DBOpenHelper mDbOpenHelper;


    public TaskDAO(Context context) {
        mContext = context;
    }

    public void setDatabase(SQLiteDatabase db) {
        mDatabase = db;
    }

    /**
     * 插入一条数据
     */
    @SuppressLint("SimpleDateFormat")
    public long insertData(TaskItem taskItem) {
        ContentValues values = new ContentValues();
        //  放入数据
        values.put(KEY_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(taskItem.getTime()));
        values.put(KEY_NAME, taskItem.getName());
        values.put(KEY_SCORE, taskItem.getScore());
        values.put(KEY_TYPE, taskItem.getType());
        values.put(KEY_TOTAL_AMOUNT, taskItem.getTotalAmount());
        values.put(KEY_FINISHED_AMOUNT, taskItem.getFinishedAmount());

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
    public long updateData(int id, TaskItem taskItem) {
        ContentValues values = new ContentValues();
        // 放入新的数据
        values.put(KEY_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(taskItem.getTime()));
        values.put(KEY_NAME, taskItem.getName());
        values.put(KEY_SCORE, taskItem.getScore());
        values.put(KEY_TYPE, taskItem.getType());
        values.put(KEY_TOTAL_AMOUNT, taskItem.getTotalAmount());
        values.put(KEY_FINISHED_AMOUNT, taskItem.getFinishedAmount());

        return mDatabase.update(TABLE_NAME, values, KEY_ID + "=" + id, null);
    }

    /**
     * 查询一条数据
     */
    public List<TaskItem> queryData(int id) {
        if (!DBConfig.HaveData(mDatabase, TABLE_NAME)) {
            return null;
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_TIME,
                        KEY_NAME,
                        KEY_SCORE,
                        KEY_TYPE,
                        KEY_TOTAL_AMOUNT,
                        KEY_FINISHED_AMOUNT},
                KEY_ID + "=" + id, null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询所有数据
     */
    public ArrayList<TaskItem> queryDataList() {
        if (!DBConfig.HaveData(mDatabase, TABLE_NAME)) {
            return null;
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_TIME,
                        KEY_NAME,
                        KEY_SCORE,
                        KEY_TYPE,
                        KEY_TOTAL_AMOUNT,
                        KEY_FINISHED_AMOUNT},
                null, null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询结果转换
     */
    @SuppressLint("Range")
    private ArrayList<TaskItem> convertUtil(Cursor cursor) {
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return null;
        }

        ArrayList<TaskItem> mList = new ArrayList<>();

        for (int i = 0; i < resultCounts; i++) {
            TaskItem taskItem = new TaskItem();
            // 将cursor当前的数据放入taskItem中
            taskItem.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            taskItem.setTime(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(KEY_TIME))));
            taskItem.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            taskItem.setScore(cursor.getInt(cursor.getColumnIndex(KEY_SCORE)));
            taskItem.setType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE)));
            taskItem.setTotalAmount(cursor.getInt(cursor.getColumnIndex(KEY_TOTAL_AMOUNT)));
            taskItem.setFinishedAmount(cursor.getInt(cursor.getColumnIndex(KEY_FINISHED_AMOUNT)));

            mList.add(taskItem);
            cursor.moveToNext();
        }
        return mList;
    }
}
