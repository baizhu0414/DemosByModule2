package com.example.demosbymodule2;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.demosbymodule2.database.sqlutil.DatabaseUtil;
import com.example.demosbymodule2.database.student.StudentDBUtil;
import com.example.demosbymodule2.database.student.StudentEntity;
import com.example.demosbymodule2.database.student.StudentMetaData;
import com.example.demosbymodule2.database.teacher.TeacherDBUtil;
import com.example.utillibrary.logutils.LogType;
import com.example.utillibrary.logutils.LogUtil;
import com.example.utillibrary.permissionutils.IPermissionListener;
import com.example.utillibrary.permissionutils.PermissionGroup;
import com.example.utillibrary.permissionutils.PermissionUtil;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

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

    //打印日志测试
    private Button logBtnFile;
    // 申请权限测试
    private Button perBtnReq;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    public void initView() {
        tvInsert = findViewById(R.id.stu_tv_insert);

        edtAge = findViewById(R.id.stu_age);
        edtGrade = findViewById(R.id.stu_grade);
        edtLocation = findViewById(R.id.stu_location);
        edtName = findViewById(R.id.stu_name);
        edtPhone = findViewById(R.id.stu_phone);
        edtValidate = findViewById(R.id.stu_validate);

        logBtnFile = findViewById(R.id.log_btn_file);
        perBtnReq = findViewById(R.id.per_btn_req);
    }

    @Override
    public void initParam() {
        initDataBase();
    }

    @Override
    public void initListener(Context context) {
        tvInsert.setOnClickListener(this);
        logBtnFile.setOnClickListener(this);
        perBtnReq.setOnClickListener(this);
    }

    private void initDataBase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StudentDBUtil stuDBUtil = StudentDBUtil.getDB();
                TeacherDBUtil teacherDBUtil = TeacherDBUtil.getDB();
                if (stuDBUtil == null) {
                    LogUtil.log(LogType.LEVEL_E, TAG, "student dbUtil NULL error.");
                    return;
                }
                List<StudentEntity> studentEntities = stuDBUtil.retrieveAll(null, null, StudentMetaData.STU_AGE);
                for (StudentEntity entity : studentEntities) {
                    LogUtil.log(LogType.LEVEL_E, TAG, entity.toString());
                }
                if (teacherDBUtil == null) {
                    LogUtil.log(LogType.LEVEL_E, TAG, "teacher dbUtil NULL error.");
                    return;
                }
                teacherSize = teacherDBUtil.countAll();
                stuSize = stuDBUtil.countAll();
                LogUtil.log(LogType.LEVEL_E, TAG, "dbUtil number: " + DatabaseUtil.getUtilNumber()
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
                    LogUtil.log(LogType.LEVEL_I, TAG, "insert id:" + insert);
                }
            }, "insertDBThread").start();
        } else if (v.getId() == R.id.log_btn_file) {
            LogUtil.log(LogType.LEVEL_I, TAG, "Test log print.");
        } else if (v.getId() == R.id.per_btn_req) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.log(LogType.LEVEL_I, TAG, "per 原始 thread id:" + Thread.currentThread());
                    PermissionUtil.PermissionReqBuilder.withActivity(MainActivity.this)
//                    .withPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                            .withPermission(PermissionGroup.STORAGE_GROUP)
                            .withPermissionListener(new IPermissionListener() {
                                @Override
                                public void onPermissionGranted(int reqCode) {
                                    LogUtil.log(LogType.LEVEL_I, TAG, "per 成功回调 thread id:" + Thread.currentThread());
                                }

                                @Override
                                public void onPermissionDenied(int reqCode, List<String> deniedPer) {
                                    PermissionUtil.reqExternalStorage(MainActivity.this);
                                    LogUtil.log(LogType.LEVEL_I, TAG, "per 失败回调 thread id:" + Thread.currentThread() + " 失败权限：" + deniedPer);
                                }
                            })
                            .withReqCode(123)
                            .callbackOnUiThread(false)
                            .startRequest();
                }
            }, "testPerThread").start();
        }
    }
}