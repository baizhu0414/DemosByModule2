package com.example.utillibrary.database.student;

import com.example.utillibrary.database.DBMetaData;

/**
 * 数据库字段名称
 */
public class StudentMetaData implements DBMetaData {

    /* 表名*/
    public static final String TABLE_NAME = "student";
    /* 所属数据库名*/
    public static final String DATABASE_NAME = SCHOOL_DATABASE;
    /* 唯一键*/
    public static final String STU_ID = "_id";
    /* 年龄*/
    public static final String STU_AGE = "age";
    /* 姓名*/
    public static final String STU_NAME = "name";
    /* 电话*/
    public static final String MOBILE_PHONE = "mobile_phone";
    /* 地址*/
    public static final String LOCATION = "location";
    /* 在校*/
    public static final String VALIDATE = "validate";
    /* 年级*/
    public static final String STU_GRADE = "grade";
    /* version2:新增*/
    public static final String STU_SEX = "sex";

}
