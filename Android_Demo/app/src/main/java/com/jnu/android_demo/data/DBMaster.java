package com.jnu.android_demo.data;

import static com.jnu.android_demo.data.DBConfig.DB_NAME;
import static com.jnu.android_demo.data.DBConfig.DB_VERSION;

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

    public DBMaster(Context context) {
        mContext = context;
        mTaskDAO = new TaskDAO(mContext);
        mRewardDAO = new RewardDAO(mContext);
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
     * 创建该数据库下phone表的语句
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
     * 创建该数据库下Company表的语句
     */
    private static final String mCompanySqlStr = "create table if not exists " + RewardDAO.TABLE_NAME + " (" +
            TaskDAO.KEY_ID + " integer  primary key autoincrement , " +
            TaskDAO.KEY_TIME + " datatime not null , " +
            TaskDAO.KEY_NAME + " text     not null , " +
            TaskDAO.KEY_SCORE + " integer  not null , " +
            TaskDAO.KEY_TYPE + " integer  not null , " +
            TaskDAO.KEY_FINISHED_AMOUNT + " integer not null" +
            ");";

    /**
     * 删除该数据库下phone表的语句
     */
    private static final String mPhoneDelSql = "DROP TABLE IF EXISTS " + TaskDAO.TABLE_NAME;

    /**
     * 删除该数据库下Company表的语句
     */
    private static final String mCompanyDelSql = "DROP TABLE IF EXISTS " + RewardDAO.TABLE_NAME;


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
            db.execSQL(mCompanySqlStr);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(mPhoneDelSql);
            db.execSQL(mCompanyDelSql);
            onCreate(db);
        }
    }
}
