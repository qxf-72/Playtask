package com.jnu.playtask.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

/** JSON backup/restore for all user-created app data. */
public final class DatabaseBackup {
    private static final int FORMAT_VERSION = 1;
    private static final String[] TABLES = {
            TaskDAO.TABLE_NAME, RewardDAO.TABLE_NAME, CountDAO.TABLE_NAME
    };

    private DatabaseBackup() { }

    public static byte[] exportData(SQLiteDatabase db) throws JSONException {
        JSONObject root = new JSONObject();
        root.put("format", "PlayTaskBackup");
        root.put("version", FORMAT_VERSION);
        JSONObject data = new JSONObject();
        for (String table : TABLES) {
            JSONArray rows = new JSONArray();
            try (Cursor cursor = db.query(table, null, null, null, null, null, "id ASC")) {
                while (cursor.moveToNext()) {
                    JSONObject row = new JSONObject();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        int type = cursor.getType(i);
                        Object value;
                        if (type == Cursor.FIELD_TYPE_NULL) value = JSONObject.NULL;
                        else if (type == Cursor.FIELD_TYPE_INTEGER) value = cursor.getLong(i);
                        else if (type == Cursor.FIELD_TYPE_FLOAT) value = cursor.getDouble(i);
                        else value = cursor.getString(i);
                        row.put(cursor.getColumnName(i), value);
                    }
                    rows.put(row);
                }
            }
            data.put(table, rows);
        }
        root.put("data", data);
        return root.toString(2).getBytes(StandardCharsets.UTF_8);
    }

    public static int[] importData(SQLiteDatabase db, byte[] bytes) throws JSONException {
        JSONObject root = new JSONObject(new String(bytes, StandardCharsets.UTF_8));
        if (!"PlayTaskBackup".equals(root.optString("format"))
                || root.optInt("version", -1) != FORMAT_VERSION) {
            throw new JSONException("不支持此备份文件格式或版本");
        }
        JSONObject data = root.getJSONObject("data");
        JSONArray[] arrays = new JSONArray[TABLES.length];
        for (int i = 0; i < TABLES.length; i++) {
            arrays[i] = data.getJSONArray(TABLES[i]);
            validateRows(TABLES[i], arrays[i]);
        }

        int[] counts = new int[TABLES.length];
        db.beginTransaction();
        try {
            for (String table : TABLES) db.delete(table, null, null);
            for (int i = 0; i < TABLES.length; i++) {
                String table = TABLES[i];
                JSONArray rows = arrays[i];
                counts[i] = rows.length();
                for (int j = 0; j < rows.length(); j++) {
                    JSONObject row = rows.getJSONObject(j);
                    ContentValues values = new ContentValues();
                    for (java.util.Iterator<String> keys = row.keys(); keys.hasNext();) {
                        String key = keys.next();
                        Object value = row.get(key);
                        if (value == JSONObject.NULL) values.putNull(key);
                        else if (value instanceof Integer) values.put(key, (Integer) value);
                        else if (value instanceof Long) values.put(key, (Long) value);
                        else if (value instanceof Number) values.put(key, ((Number) value).doubleValue());
                        else if (value instanceof Boolean) values.put(key, (Boolean) value);
                        else values.put(key, value.toString());
                    }
                    if (db.insertOrThrow(table, null, values) < 0) {
                        throw new JSONException("写入备份数据失败");
                    }
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return counts;
    }

    private static void validateRows(String table, JSONArray rows) throws JSONException {
        String[] required;
        if (TaskDAO.TABLE_NAME.equals(table)) {
            required = new String[]{"id", "time", "name", "score", "type", "total_amount", "finished_amount"};
        } else if (RewardDAO.TABLE_NAME.equals(table)) {
            required = new String[]{"id", "time", "name", "score", "type", "finished_amount"};
        } else {
            required = new String[]{"id", "time", "name", "score"};
        }
        for (int i = 0; i < rows.length(); i++) {
            JSONObject row = rows.getJSONObject(i);
            for (String key : required) {
                if (!row.has(key) || row.isNull(key)) throw new JSONException("备份数据缺少字段：" + key);
            }
            if (row.getLong("id") <= 0 || row.getString("name").trim().isEmpty()) {
                throw new JSONException("备份数据包含无效记录");
            }
            TimestampParser.requireValid(row.getString("time"));
            for (String key : required) {
                if ("id".equals(key) || "time".equals(key) || "name".equals(key)) continue;
                row.getInt(key);
            }
        }
    }

    private static final class TimestampParser {
        static void requireValid(String value) throws JSONException {
            try {
                java.sql.Timestamp.valueOf(value);
            } catch (IllegalArgumentException e) {
                throw new JSONException("备份文件中的时间格式无效");
            }
        }
    }
}
