package com.jnu.playtask.data;

import static com.jnu.playtask.data.DBConfig.DB_NAME;
import static com.jnu.playtask.data.DBConfig.DB_VERSION;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库总操作类
 *
 * @author Xiaofeng Qiu
 * create at 2023/12/14
 */
public class DBMaster {

    // 上下文
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private DBOpenHelper mDbOpenHelper;

    //  数据表操作类实例化
    public TaskDAO mTaskDAO;
    public RewardDAO mRewardDAO;
    public CountDAO mCountDAO;


    /**
     * 构造函数
     */
    public DBMaster(Context context) {
        mContext = context;
        mTaskDAO = new TaskDAO(mContext);
        mRewardDAO = new RewardDAO(mContext);
        mCountDAO = new CountDAO(mContext);
    }


    /**
     * 打开数据库
     */
    public void openDataBase() {
        mDbOpenHelper = new DBOpenHelper(mContext, DB_NAME, null, DB_VERSION);
        try {
            mDatabase = mDbOpenHelper.getWritableDatabase();//获取可写数据库
        } catch (SQLException e) {
            mDatabase = mDbOpenHelper.getReadableDatabase();//获取只读数据库
        }
        // 设置数据库的SQLiteDatabase
        mTaskDAO.setDatabase(mDatabase);
        mRewardDAO.setDatabase(mDatabase);
        mCountDAO.setDatabase(mDatabase);
    }


    /**
     * 关闭数据库
     */
    public void closeDataBase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }


    /**
     * 创建该数据库下Task表的语句
     */
    private static final String mTaskSqlStr = "create table if not exists " + TaskDAO.TABLE_NAME + " (" +
            TaskDAO.KEY_ID + " integer  primary key autoincrement , " +
            TaskDAO.KEY_TIME + " datatime not null , " +
            TaskDAO.KEY_NAME + " text     not null , " +
            TaskDAO.KEY_SCORE + " integer  not null , " +
            TaskDAO.KEY_TYPE + " integer  not null , " +
            TaskDAO.KEY_TOTAL_AMOUNT + " integer  not null , " +
            TaskDAO.KEY_FINISHED_AMOUNT + " integer not null" +
            ");";


    /**
     * 创建该数据库下Reward表的语句
     */
    private static final String mRewardSqlStr = "create table if not exists " + RewardDAO.TABLE_NAME + " (" +
            RewardDAO.KEY_ID + " integer  primary key autoincrement , " +
            RewardDAO.KEY_TIME + " datatime not null , " +
            RewardDAO.KEY_NAME + " text     not null , " +
            RewardDAO.KEY_SCORE + " integer  not null , " +
            RewardDAO.KEY_TYPE + " integer  not null , " +
            RewardDAO.KEY_FINISHED_AMOUNT + " integer not null" +
            ");";


    /**
     * 创建该数据库下Count表的语句
     */
    private static final String mCountSqlStr = "create table if not exists " + CountDAO.TABLE_NAME + " (" +
            CountDAO.KEY_ID + " integer  primary key autoincrement , " +
            CountDAO.KEY_TIME + " datatime not null , " +
            CountDAO.KEY_NAME + " text     not null , " +
            CountDAO.KEY_SCORE + " integer  not null  " +
            ");";


    /**
     * 删除该数据库下Task表的语句
     */
    private static final String mTaskDelSql = "DROP TABLE IF EXISTS " + TaskDAO.TABLE_NAME;


    /**
     * 删除该数据库下Reward表的语句
     */
    private static final String mRewardDelSql = "DROP TABLE IF EXISTS " + RewardDAO.TABLE_NAME;


    /**
     * 删除该数据库下Count表的语句
     */
    private static final String mCountDelSql = "DROP TABLE IF EXISTS " + CountDAO.TABLE_NAME;


    /**
     * 数据表打开帮助类
     */
    public static class DBOpenHelper extends SQLiteOpenHelper {

        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(mTaskSqlStr);
            db.execSQL(mRewardSqlStr);
            db.execSQL(mCountSqlStr);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(mTaskDelSql);
            db.execSQL(mRewardDelSql);
            db.execSQL(mCountDelSql);
            onCreate(db);
        }
    }
}
