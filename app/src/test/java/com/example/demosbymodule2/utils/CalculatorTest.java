package com.example.demosbymodule2.utils;

import android.util.Log;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalculatorTest {

    private static final String TAG = "CalculatorTest";
    Calculator calculator;
    ExecutorService cachedExecutor = Executors.newCachedThreadPool();

    @Before
    public void setUp() throws Exception {
        calculator = new Calculator(100);
    }

    @Test
    public void testAddOne() {
        calculator.addOne();
        Assert.assertEquals(calculator.value, Calculator.addOne(100));
        calculator.reduceOne();
        Assert.assertEquals(calculator.value, Calculator.reduceOne(101));
    }

    @Test
    public void testAddSync() {
        CountDownLatch latch = new CountDownLatch(2);
        cachedExecutor.execute(new Runnable() {
            @Override
            public void run() {
                calculator.reduceOne();
                Assert.assertEquals(calculator.value, Calculator.reduceOne(100));
                System.out.println("time2:" + System.currentTimeMillis());
                latch.countDown();
                System.out.println("time3:" + System.currentTimeMillis());
            }
        });
        cachedExecutor.execute(new Runnable() {
            @Override
            public void run() {
                calculator.addOne();
                Assert.assertEquals(calculator.value, Calculator.addOne(99));
                System.out.println("time4:" + System.currentTimeMillis());
                latch.countDown();
                System.out.println("time5:" + System.currentTimeMillis());
            }
        });
        try {
//            Log.d(TAG, "before await:" + System.currentTimeMillis());
            System.out.println("time1:" + System.currentTimeMillis());
            latch.await();
            System.out.println("time6:" + System.currentTimeMillis());
//            Log.d(TAG, "after await:" + System.currentTimeMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() throws Exception {
        calculator = null;
    }
}