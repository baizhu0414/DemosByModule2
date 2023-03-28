package com.example.demosbymodule2;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demosbymodule2.database.sqlutil.DatabaseUtil;
import com.example.demosbymodule2.database.student.StudentDBUtil;
import com.example.demosbymodule2.database.student.StudentEntity;
import com.example.demosbymodule2.database.student.StudentMetaData;
import com.example.demosbymodule2.database.teacher.TeacherDBUtil;
import com.example.utillibrary.LogUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    long stuSize = 0;
    long teacherSize = 0;
    private TextView tvInsert;
    private EditText edtAge;
    private EditText edtGrade;
    private EditText edtLocation;
    private EditText edtName;
    private EditText edtPhone;
    private EditText edtValidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDataBase();
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DatabaseUtil.closeAllDatabases();
    }

    private void initView() {
        tvInsert = findViewById(R.id.stu_tv_insert);
        tvInsert.setOnClickListener(this);

        edtAge = findViewById(R.id.stu_age);
        edtGrade = findViewById(R.id.stu_grade);
        edtLocation = findViewById(R.id.stu_location);
        edtName = findViewById(R.id.stu_name);
        edtPhone = findViewById(R.id.stu_phone);
        edtValidate = findViewById(R.id.stu_validate);
    }

    private void initDataBase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StudentDBUtil stuDBUtil = StudentDBUtil.getDB();
                TeacherDBUtil teacherDBUtil = TeacherDBUtil.getDB();
                List<StudentEntity> studentEntities = stuDBUtil.retrieveAll(null, null, StudentMetaData.STU_AGE);
                for (StudentEntity entity : studentEntities) {
                    LogUtil.e(TAG, entity.toString());
                }
                teacherSize = teacherDBUtil.countAll();
                stuSize = stuDBUtil.countAll();
                LogUtil.e(TAG, "dbUtil number: " + DatabaseUtil.getUtilNumber()
                        + " //database number:" + DatabaseUtil.getSQLiteHelperNumber());
            }
        }, "initDBThread").start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.stu_tv_insert) {
            String name = edtName.getText().toString();
            String ageStr = edtAge.getText().toString();
            String validateStr = edtValidate.getText().toString();
            String gradeStr = edtGrade.getText().toString();
            String location = edtLocation.getText().toString();
            String phone = edtPhone.getText().toString();
            int age, validate, grade;
            age = TextUtils.isEmpty(ageStr) ? 1 : Integer.parseInt(ageStr);
            validate = TextUtils.isEmpty(validateStr) ? 1 : Integer.parseInt(validateStr);
            grade = TextUtils.isEmpty(gradeStr) ? 1 : Integer.parseInt(gradeStr);
            StudentEntity entity = new StudentEntity();
            entity.setId(String.valueOf(stuSize + 1));
            stuSize++;
            entity.setName(name);
            entity.setGrade(grade);
            entity.setPhone(phone);
            entity.setLocation(location);
            entity.setAge(age);
            entity.setValidate(validate);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long insert = StudentDBUtil.getDB().insert(entity);
                    LogUtil.i(TAG, "insert id:" + insert);
                }
            }, "insertDBThread").start();
        }
    }
}