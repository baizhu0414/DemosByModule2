package com.example.demosbymodule2;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.demosbymodule2.database.student.StudentDBUtil;
import com.example.demosbymodule2.database.student.StudentEntity;
import com.example.demosbymodule2.database.student.StudentMetaData;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        List<StudentEntity> entityList = StudentDBUtil.getDB()
                .retrieveAll(null, null, StudentMetaData.STU_AGE);
        for(StudentEntity entity: entityList) {
            System.out.println(entity.toString());
        }
    }
}