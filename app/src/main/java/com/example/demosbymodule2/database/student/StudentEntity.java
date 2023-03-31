package com.example.demosbymodule2.database.student;

// 向上提取一个父类，并且集成comparator和Cloneable
public class StudentEntity {
    /* 唯一键，从1开始*/
    private String id;
    /* 年龄*/
    private int age;
    /* 姓名*/
    private String name;
    /* 电话*/
    private String phone;
    /* 地址*/
    private String location;
    /* 在校*/
    private int validate;
    /* 年级*/
    private int grade;
    /* version2:新增, 0男，1女*/
    private int sex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getValidate() {
        return validate;
    }

    public void setValidate(int validate) {
        this.validate = validate;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "StudentEntity{" +
                "id='" + id + '\'' +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", location='" + location + '\'' +
                ", validate=" + validate +
                ", grade=" + grade +
                ", sex=" + sex +
                '}';
    }
}
