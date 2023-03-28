package com.example.demosbymodule2.database.teacher;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.demosbymodule2.database.sqlutil.DatabaseUtil;

public class TeacherDBUtil extends DatabaseUtil<TeacherEntity> {

    private static final String TAG = "TeacherDBUtil";
    public static final String[] KEYS = {TeacherMetaData.TEACHER_ID, TeacherMetaData.TEACHER_AGE,
            TeacherMetaData.TEACHER_NAME, TeacherMetaData.TEACHER_SEX};

    public static TeacherDBUtil getDB() {
        TeacherDBUtil teacherUtil;
        synchronized (TeacherDBUtil.class) {
            teacherUtil = (TeacherDBUtil) dbUtilPool.get(TAG);
            if(teacherUtil == null) {
                teacherUtil = new TeacherDBUtil(TeacherMetaData.DATABASE_NAME);
                dbUtilPool.put(TAG, teacherUtil);
            }
        }
        return teacherUtil;

    }

    /**
     * 初始化数据库操作对象{@code SQLiteHelper}
     *
     * @param databaseName 数据库名称，作为Helper的key
     */
    public TeacherDBUtil(String databaseName) {
        super(databaseName);
    }

    @Override
    protected ContentValues getContentValues(TeacherEntity teacher) {
        ContentValues values = new ContentValues();
        values.put(TeacherMetaData.TEACHER_ID, teacher.id);
        values.put(TeacherMetaData.TEACHER_AGE, teacher.age);
        values.put(TeacherMetaData.TEACHER_NAME, teacher.name);
        values.put(TeacherMetaData.TEACHER_SEX, teacher.sex);
        return values;
    }

    @SuppressLint("Range")
    @Override
    protected TeacherEntity create(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        TeacherEntity entity = new TeacherEntity();
        if (cursor.moveToFirst()) {
            entity.setId(cursor.getString(cursor.getColumnIndex(TeacherMetaData.TEACHER_ID)));
            entity.setAge(cursor.getInt(cursor.getColumnIndex(TeacherMetaData.TEACHER_AGE)));
            entity.setName(cursor.getString(cursor.getColumnIndex(TeacherMetaData.TEACHER_NAME)));
            entity.setSex(cursor.getInt(cursor.getColumnIndex(TeacherMetaData.TEACHER_SEX)));
        }
        return entity;
    }

    @Override
    protected String[] getQueryKeyList() {
        return KEYS;
    }

    @Override
    protected String getTableName() {
        return TeacherMetaData.TABLE_NAME;
    }
}
