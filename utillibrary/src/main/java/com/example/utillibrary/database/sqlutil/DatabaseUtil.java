package com.example.utillibrary.database.sqlutil;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 初始化、存储、操作 {@link SQLiteHelper}，控制数据库操作。
 * <p>
 * <li>{@link #dbUtilPool}和{@link #sqLiteHelperPool}是多对多映射，一个库对应一个SQLiteHelper对象，
 * 一个表对应一个DatabaseUtil对象（库:表=1:n）。参考{@link #getUtilNumber()} ,{@link #getSQLiteHelperNumber()}.
 */
public abstract class DatabaseUtil<T> {
    private static final String TAG = "DatabaseUtil";
    private final String databaseName;

    /**
     * 存储数据库工具类(this)实例
     */
    protected static final Map<String, DatabaseUtil> dbUtilPool = new ConcurrentHashMap<>();
    /**
     * 存储数据库操作对象{@link SQLiteHelper}实例
     */
    protected static final Map<String, SQLiteHelper> sqLiteHelperPool = new ConcurrentHashMap<>();

    /**
     * 初始化数据库操作对象{@link SQLiteHelper}
     *
     * @param databaseName 数据库名称，作为Helper的key
     */
    public DatabaseUtil(String databaseName) {
        this.databaseName = databaseName;
        synchronized (DatabaseUtil.class) {
            SQLiteHelper sqLiteHelper = sqLiteHelperPool.get(databaseName);
            if (sqLiteHelper == null) {
                sqLiteHelper = new SQLiteHelper(databaseName);
                sqLiteHelperPool.put(databaseName, sqLiteHelper);
                Log.i(TAG, "#DatabaseUtil:create " + databaseName + " sqLiteHelper.");
            }
        }
    }

    /**
     * 获取实例用来插入数据库
     */
    protected abstract ContentValues getContentValues(T value);

    /**
     * 获取查询结果
     */
    protected abstract T create(Cursor cursor);

    /**
     * 获取查询字段
     */
    protected abstract String[] getQueryKeyList();

    /**
     * 获取{@link DatabaseUtil}子类所属数据库中的'表'的名称
     */
    protected abstract String getTableName();

    public long insert(@NonNull T insertVal) {
        // 日志用
        long startTime = getCurrentTime();
        // 操作
        long result = getSQLiteDB().insert(getTableName(), null, getContentValues(insertVal));
        logOfInsert(insertVal.getClass().getSimpleName(), startTime);
        return result;
    }

    public boolean insert(@NonNull List<T> values) {
        if (values.size() == 0) {
            return true;
        }
        // 日志用
        long startTime = getCurrentTime();
        // 操作
        boolean result = false;
        SQLiteDatabase db = getSQLiteDB();
        db.beginTransaction();
        try {
            for (T t : values) {
                db.insert(getTableName(), null, getContentValues(t));
            }
            db.setTransactionSuccessful();
            result = true;
        } catch (Exception | Error e) {
            Log.e(TAG, "", e);
        } finally {
            db.endTransaction();
        }
        logOfInsert(values.get(0).getClass().getSimpleName(), startTime);
        return result;
    }

    public boolean update(T value, String id) {
        long startTime = getCurrentTime();
        boolean result = false;
        SQLiteDatabase db = getSQLiteDB();
        try {
            int rows = db.update(getTableName(), getContentValues(value), getQueryKeyList()[0] + "=?",
                    new String[]{id});
            result = (rows > 0);
        } catch (Exception | Error e) {
            Log.e(TAG, "#update", e);
        }
        logOfUpdate(value.getClass().getSimpleName(), startTime, id);
        return result;
    }

    public boolean update(T value, String where, String[] args) {
        long startTime = getCurrentTime();
        boolean result = false;
        SQLiteDatabase db = getSQLiteDB();
        try {
            int rows = db.update(getTableName(), getContentValues(value), where, args);
            result = rows > 0;
        } catch (Exception | Error e) {
            Log.e(TAG, "", e);
        }
        logOfUpdate(value.getClass().getSimpleName(), startTime, Arrays.toString(args));
        return result;
    }

    public boolean delete(long id) {
        boolean result = false;
        long startTime = getCurrentTime();
        SQLiteDatabase db = getSQLiteDB();
        try {
            int rows = db.delete(getTableName(), getQueryKeyList()[0] + "=?",
                    new String[]{String.valueOf(id)});
            result = rows > 0;
        } catch (Exception | Error e) {
            Log.e(TAG, "", e);
        }
        logOfDelete(startTime, String.valueOf(id));
        return result;
    }

    // query
    public List<T> retrieveAll(String selection, String[] args, String order) {
        long startTime = getCurrentTime();
        SQLiteDatabase db = getSQLiteDB();
        Cursor cursor = retrieveForCursor(db, getQueryKeyList(), selection, args, null, null, order, null);
        List<T> resultList = createFromCursor(cursor);
        logOfRetrieve(startTime, String.valueOf(resultList.size()));
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
//        closeAllDatabases();
        return resultList;
    }

    /**
     * 统计数据库对应表的数据量
     */
    public long countAll() {
        SQLiteDatabase db = getSQLiteDB();
        String querySQL = "select count(*) from " + getTableName();
        long resultSize = 0;
        try {
            Cursor cursor = db.rawQuery(querySQL, null);
            if (cursor.moveToFirst()) {
                resultSize = cursor.getLong(0);
            }
            cursor.close();
        } catch (Exception | Error e) {
            Log.e(TAG, "", e);
        }
        return resultSize;
    }

    private Cursor retrieveForCursor(SQLiteDatabase db, String[] columns, String selection, String[] selectionArgs, String groupBy,
                                     String having, String orderBy, String limit) {
        Cursor result = null;
        try {
            result = db.query(getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        } catch (Exception | Error e) {
            Log.e(TAG, "", e);
        }
        return result;
    }

    private List<T> createFromCursor(Cursor cursor) {
        List<T> resultList;
        if (cursor == null) {
            resultList = Collections.emptyList();
        } else {
            resultList = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    T val = create(cursor);
                    resultList.add(val);
                } while (cursor.moveToNext());
            }
        }
        return resultList;
    }

    private SQLiteDatabase getSQLiteDB() {
        SQLiteHelper sqLiteHelper = sqLiteHelperPool.get(databaseName);
        if (sqLiteHelper == null) {
            sqLiteHelper = new SQLiteHelper(databaseName);
            sqLiteHelperPool.put(databaseName, sqLiteHelper);
        }
        return sqLiteHelper.open();
    }

    /**
     * 关闭全部有打开的数据库对象。
     */
    public static void closeAllDatabases() {
        Log.d(TAG, "#closeAllDatabases :utils "
                + dbUtilPool.size() + "+ helpers" + sqLiteHelperPool.size() + ".");
        for (Map.Entry<String, SQLiteHelper> entry : sqLiteHelperPool.entrySet()) {
            SQLiteHelper dbHelper = entry.getValue();
            dbHelper.closeSQLiteDB();
        }
        sqLiteHelperPool.clear();
        dbUtilPool.clear();
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    private void logOfInsert(String className, long startTime) {
        Log.i(TAG, "insert " + className
                + " into " + getTableName() + " cost:" + (System.currentTimeMillis() - startTime));
    }

    private void logOfUpdate(String className, long startTime, String id) {
        Log.i(TAG, "update " + className
                + " of " + getTableName() + " id=" + id + " cost:" + (System.currentTimeMillis() - startTime));
    }

    private void logOfDelete(long startTime, String id) {
        Log.i(TAG, "delete id=" + id
                + " of " + getTableName() + " cost:" + (System.currentTimeMillis() - startTime));
    }

    private void logOfRetrieve(long startTime, String num) {
        Log.i(TAG, "retrieve " + " from " + getTableName()
                + " for " + num + " items cost:" + (System.currentTimeMillis() - startTime));
    }

    /**
     * 等价于表数量
     */
    public static int getUtilNumber() {
        return dbUtilPool.size();
    }

    /**
     * 等价于库数量
     */
    public static int getSQLiteHelperNumber() {
        return sqLiteHelperPool.size();
    }
}
