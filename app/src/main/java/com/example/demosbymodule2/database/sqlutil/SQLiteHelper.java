package com.example.demosbymodule2.database.sqlutil;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.demosbymodule2.DemoApplication;
import com.example.demosbymodule2.database.student.StudentMetaData;
import com.example.demosbymodule2.database.teacher.TeacherMetaData;
import com.example.demosbymodule2.logic.StudentLogic;


/**
 * 继承{@link SQLiteOpenHelper}，用来协助获取{@link SQLiteDatabase}对象
 * ，进行数据库底层操作。此对象代表一个数据库，包含多个库表。
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TAG = "SQLiteHelper";
    private SQLiteDatabase db;
    /* 数据库版本*/
    private static final int DATABASE_VERSION = 1;
    /* 数据格式类型*/
    public static final String COLUMN_TYPE_TEXT = "TEXT";
    public static final String COLUMN_TYPE_INTEGER = "INTEGER";
    public static final String COLUMN_TYPE_LONG = "LONG";

    /* 1.创建数据库语句*/
    private static final String CREATE_STUDENT_TABLE = "CREATE TABLE IF NOT EXISTS "
            + StudentMetaData.TABLE_NAME + " ("
            + StudentMetaData.STU_ID + " " + COLUMN_TYPE_LONG + " PRIMARY KEY NOT NULL,"
            + StudentMetaData.STU_NAME + " " + COLUMN_TYPE_TEXT + ","
            + StudentMetaData.STU_AGE + " " + COLUMN_TYPE_INTEGER + ","
            + StudentMetaData.MOBILE_PHONE + " " + COLUMN_TYPE_TEXT + ","
            + StudentMetaData.LOCATION + " " + COLUMN_TYPE_TEXT + ","
            + StudentMetaData.VALIDATE + " " + COLUMN_TYPE_INTEGER + ","
            + StudentMetaData.STU_GRADE + " " + COLUMN_TYPE_INTEGER
            + ")";
    private static final String CREATE_TEACHER_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TeacherMetaData.TABLE_NAME + " ("
            + TeacherMetaData.TEACHER_ID + " " + COLUMN_TYPE_TEXT + " PRIMARY KEY NOT NULL,"
            + TeacherMetaData.TEACHER_NAME + " " + COLUMN_TYPE_TEXT + ","
            + TeacherMetaData.TEACHER_AGE + " " + COLUMN_TYPE_INTEGER + ","
            + TeacherMetaData.TEACHER_SEX + " " + COLUMN_TYPE_INTEGER
            + ")";

    /* 2.创建数据库索引语句*/
    private static final String CREATE_INDEX_STUDENT =
            "create index if not exists idxStudent on " + StudentMetaData.TABLE_NAME + "("
                    + StudentMetaData.STU_NAME + "," + StudentMetaData.STU_GRADE + ")";

    /* 3.删除数据库表和索引*/
    private static final String DROP_TABLE_STUDENT_TABLE = "DROP TABLE IF EXISTS " + StudentMetaData.TABLE_NAME;
    private static final String DROP_INDEX_STUDENT = "drop index if exists idxStudent";

    public SQLiteHelper(String name) {
        super(DemoApplication.context, name, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "#onCreate:" + getDatabaseName());
        createTable(db);
        initTableData(db);
        createIndex(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            return;
        }
        Log.i(TAG, "#onUpgrade:from " + oldVersion + " to " + newVersion);
        updateFromV1ToV5(db, oldVersion, newVersion);
    }

    public SQLiteDatabase open() {
        if (null == db || !db.isOpen()) {
            db = getWritableDatabase();
        }
        return db;
    }

    public void closeSQLiteDB() {
        if (null != db && db.isOpen()) {
            try {
                db.close();
            } catch (Exception e) {
                Log.e(TAG, "关闭数据库对象", e);
            }
        }
    }

    /*------------------ 以下：流程中调用的函数，包含创建表和索引，插入默认值等操作。------------------*/

    private void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_TEACHER_TABLE);
        Log.i(TAG, "#createTable");
    }

    /**
     * 数据库插入初值，如：文件互传助手这类本地对象。
     */
    private void initTableData(SQLiteDatabase db) {
        Log.i(TAG, "#initTableData");
        initEappDBData(db);
    }

    private void initEappDBData(SQLiteDatabase db) {
        Log.i(TAG, "#initEappDB");
        ContentValues values = new ContentValues();
        values.put(StudentMetaData.STU_ID, StudentLogic.STU_ROBOT_ID);
        values.put(StudentMetaData.STU_AGE, 0);
        values.put(StudentMetaData.STU_NAME, "文件互传助手");
        values.put(StudentMetaData.VALIDATE, 1);
        values.put(StudentMetaData.MOBILE_PHONE, "+86");
        values.put(StudentMetaData.STU_GRADE, 0);
        values.put(StudentMetaData.LOCATION, "Local");
        db.replace(StudentMetaData.TABLE_NAME, null, values);
    }

    private void createIndex(SQLiteDatabase db) {
        Log.i(TAG, "#createIndex");
        db.execSQL(CREATE_INDEX_STUDENT);
    }

    /**
     * 测试效果：
     * 低版本升级到高版本后补充完善student和teacher表。（与newVersion无关）
     */
    private void updateFromV1ToV5(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            db.execSQL(sqlColumnADD(StudentMetaData.TABLE_NAME, StudentMetaData.STU_SEX, COLUMN_TYPE_TEXT));
        }
        if (oldVersion <= 2) {
            // sql
//            db.execSQL(CREATE_TEACHER_TABLE);
        }
        if (oldVersion <= 3) {
            // sql
            db.execSQL(sqlColumnADD(TeacherMetaData.TABLE_NAME, TeacherMetaData.TEACHER_SUBJECT, COLUMN_TYPE_TEXT));
        }
        Log.w(TAG, "update from " + oldVersion + " to " + newVersion);
    }

    /**
     * 数据库中的表添加字段
     *
     * @param tableName  表名
     * @param columnName 新增列名
     * @param type       字段类型，如：'varchar(20)'
     * @return sql语句
     */
    private String sqlColumnADD(String tableName, String columnName, String type) {
        return "alter table " + tableName + " add " + columnName + " " + type;
    }
}
