package com.example.utillibrary.database.student;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.utillibrary.database.sqlutil.DatabaseUtil;

/**
 * student表的数据库操作对象。数据库名参考{@see #databaseName},允许多个Util类公用一个name。
 * 核心方法：{@link #getDB()}
 */
public class StudentDBUtil extends DatabaseUtil<StudentEntity> {

    private static final String[] KEYS = new String[]{StudentMetaData.STU_ID, StudentMetaData.LOCATION,
            StudentMetaData.MOBILE_PHONE, StudentMetaData.STU_AGE, StudentMetaData.STU_GRADE,
            StudentMetaData.STU_NAME, StudentMetaData.VALIDATE};

    private static final String TAG = "StudentDBUtil";

    /**
     * 步骤：
     * 1.初始化DBUtil
     * 2.初始化SQLiteHelper
     */
    public static StudentDBUtil getDB() {
        StudentDBUtil dbUtil;
        synchronized (StudentDBUtil.class) {
            dbUtil = (StudentDBUtil) dbUtilPool.get(TAG);
            if (dbUtil == null) {
                dbUtil = new StudentDBUtil(StudentMetaData.DATABASE_NAME);
                dbUtilPool.put(TAG, dbUtil);
            }
        }
        return dbUtil;
    }

    /**
     * {@link DatabaseUtil#DatabaseUtil(String)}
     *
     * @param databaseName 数据库名称，作为{@link #sqLiteHelperPool}的key.
     *                     [注]允许多个Util公用同一个值，表示公用同一个数据库。
     */
    private StudentDBUtil(String databaseName) {
        super(databaseName);
    }

    @Override
    protected ContentValues getContentValues(StudentEntity value) {
        ContentValues values = new ContentValues();
        values.put(StudentMetaData.STU_ID, value.getId());
        values.put(StudentMetaData.LOCATION, value.getLocation());
        values.put(StudentMetaData.MOBILE_PHONE, value.getPhone());
        values.put(StudentMetaData.STU_AGE, value.getAge());
        values.put(StudentMetaData.STU_GRADE, value.getGrade());
        values.put(StudentMetaData.STU_NAME, value.getName());
        values.put(StudentMetaData.VALIDATE, value.getValidate());
        return values;
    }

    @SuppressLint("Range")
    @Override
    protected StudentEntity create(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        StudentEntity student = new StudentEntity();
        student.setId(cursor.getString(cursor.getColumnIndex(StudentMetaData.STU_ID)));
        student.setLocation(cursor.getString(cursor.getColumnIndex(StudentMetaData.LOCATION)));
        student.setPhone(cursor.getString(cursor.getColumnIndex(StudentMetaData.MOBILE_PHONE)));
        student.setAge(cursor.getInt(cursor.getColumnIndex(StudentMetaData.STU_AGE)));
        student.setGrade(cursor.getInt(cursor.getColumnIndex(StudentMetaData.STU_GRADE)));
        student.setName(cursor.getString(cursor.getColumnIndex(StudentMetaData.STU_NAME)));
        student.setValidate(cursor.getInt(cursor.getColumnIndex(StudentMetaData.VALIDATE)));
        return student;
    }

    @Override
    protected String[] getQueryKeyList() {
        return KEYS;
    }

    @Override
    protected String getTableName() {
        return StudentMetaData.TABLE_NAME;
    }
}
