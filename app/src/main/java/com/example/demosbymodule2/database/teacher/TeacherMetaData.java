package com.example.demosbymodule2.database.teacher;

import com.example.demosbymodule2.database.DBMetaData;

/* version2:新增*/
public class TeacherMetaData implements DBMetaData {
    /* 表名*/
    public static final String TABLE_NAME = "teacher";
    /* 所属数据库名*/
    public static final String DATABASE_NAME = SCHOOL_DATABASE;
    /* 唯一键*/
    public static final String TEACHER_ID = "_id";
    public static final String TEACHER_NAME = "name";
    public static final String TEACHER_AGE = "age";
    public static final String TEACHER_SEX = "sex";
    public static final String TEACHER_SUBJECT = "subject";
}
