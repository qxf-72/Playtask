package com.jnu.playtask.data;

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
 * RewardItem类的数据库操控类
 *
 * @author Xiaofeng Qiu
 * create at 2023/12/14
 */
public class RewardDAO {
    // 数据表名称
    public static final String TABLE_NAME = "reward_info";

    // 表的字段名
    public static String KEY_ID = "id";
    public static String KEY_TIME = "time";
    public static String KEY_NAME = "name";
    public static String KEY_SCORE = "score";
    public static String KEY_TYPE = "type";
    public static String KEY_FINISHED_AMOUNT = "finished_amount";

    public static final int NO_SORT = 0;
    public static final int TIME_ASC = 1;
    public static final int TIME_DESC = 2;
    // 存入数据库是，虽然score都是负数，但是存入的是绝对值，所以排序时需要注意
    public static final int SCORE_ASC = 4;
    public static final int SCORE_DESC = 3;

    private SQLiteDatabase mDatabase;
    // 上下文
    private Context mContext;
    // 数据库打开帮助类
    private DBMaster.DBOpenHelper mDbOpenHelper;

    public RewardDAO(Context context) {
        mContext = context;
    }

    public void setDatabase(SQLiteDatabase db) {
        mDatabase = db;
    }


    /**
     * 插入一条数据
     */
    @SuppressLint("SimpleDateFormat")
    public long insertData(RewardItem rewardItem) {
        ContentValues values = new ContentValues();
        // 将rewardItem数据放入values中
        values.put(KEY_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rewardItem.getTime()));
        values.put(KEY_NAME, rewardItem.getName());
        values.put(KEY_SCORE, rewardItem.getScore());
        values.put(KEY_TYPE, rewardItem.getType());
        values.put(KEY_FINISHED_AMOUNT, rewardItem.getFinishedAmount());

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
    public long updateData(int id, RewardItem rewardItem) {
        ContentValues values = new ContentValues();
        values.put(KEY_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rewardItem.getTime()));
        values.put(KEY_NAME, rewardItem.getName());
        values.put(KEY_SCORE, rewardItem.getScore());
        values.put(KEY_TYPE, rewardItem.getType());
        values.put(KEY_FINISHED_AMOUNT, rewardItem.getFinishedAmount());
        return mDatabase.update(TABLE_NAME, values, KEY_ID + "=" + id, null);
    }


    /**
     * 查询一条数据
     */
    public List<RewardItem> queryData(int id) {
        if (!DBConfig.HaveData(mDatabase, TABLE_NAME)) {
            return null;
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_TIME,
                        KEY_NAME,
                        KEY_TYPE,
                        KEY_SCORE,
                        KEY_FINISHED_AMOUNT},
                KEY_ID + "=" + id, null, null, null, null);
        return convertUtil(results);
    }


    /**
     * 查询所有数据
     */
    public ArrayList<RewardItem> queryDataList(int MODE) {
        if (!DBConfig.HaveData(mDatabase, TABLE_NAME)) {
            return null;
        }

        String orderBy = null;
        switch (MODE) {
            case NO_SORT:
                break;
            case TIME_ASC:
                orderBy = KEY_TIME + " ASC";
                break;
            case TIME_DESC:
                orderBy = KEY_TIME + " DESC";
                break;
            case SCORE_ASC:
                orderBy = KEY_SCORE + " ASC";
                break;
            case SCORE_DESC:
                orderBy = KEY_SCORE + " DESC";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + MODE);
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_TIME,
                        KEY_NAME,
                        KEY_TYPE,
                        KEY_SCORE,
                        KEY_FINISHED_AMOUNT},
                null, null, null, null, orderBy);
        return convertUtil(results);
    }

    
    /**
     * 查询结果转换
     */
    @SuppressLint("Range")
    private ArrayList<RewardItem> convertUtil(Cursor cursor) {
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return null;
        }
        ArrayList<RewardItem> mList = new ArrayList<>();
        for (int i = 0; i < resultCounts; i++) {
            RewardItem rewardItem = new RewardItem();

            rewardItem.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            rewardItem.setTime(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(KEY_TIME))));
            rewardItem.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            rewardItem.setScore(cursor.getInt(cursor.getColumnIndex(KEY_SCORE)));
            rewardItem.setType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE)));
            rewardItem.setFinishedAmount(cursor.getInt(cursor.getColumnIndex(KEY_FINISHED_AMOUNT)));

            mList.add(rewardItem);
            cursor.moveToNext();
        }
        return mList;
    }
}
