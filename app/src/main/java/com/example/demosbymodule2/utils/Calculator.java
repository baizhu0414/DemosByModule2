package com.example.demosbymodule2.utils;

public class Calculator {

    int value;

    public Calculator(int value) {
        this.value = value;
    }

    public static int addOne(int a) {
        return a + 1;
    }

    public static int reduceOne(int b) {
        return b - 1;
    }

    public void addOne() {
        this.value++;
    }

    public void reduceOne() {
        this.value--;
    }
}
