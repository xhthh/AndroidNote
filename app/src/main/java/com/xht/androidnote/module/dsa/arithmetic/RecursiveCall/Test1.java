package com.xht.androidnote.module.dsa.arithmetic.RecursiveCall;

public class Test1 {


    public static void main(String[] args) {
        int[] value = new int[]{1, 5, 10, 50, 100};

        int sum = sum(2, value);

        System.out.println("sum==" + sum);

    }


























    public static int sum(int i, int[] arr) {

        if (i < arr.length - 1) {
            return arr[i] + sum(i+1,arr);
        } else {
            return arr[i];
        }

    }


}
