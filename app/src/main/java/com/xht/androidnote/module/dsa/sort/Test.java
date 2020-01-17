package com.xht.androidnote.module.dsa.sort;

import java.util.Arrays;

/**
 * Created by xht on 2020/1/17.
 */
public class Test {

    public static void test1(int[] array) {

        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }

            if (i != minIndex) {
                int temp = array[minIndex];
                array[minIndex] = array[i];
                array[i] = temp;
            }
        }

    }


    public static void test2(int[] array) {

        for (int i = 1; i < array.length; i++) {
            for (int j = i; j > 0; j--) {
                if (array[j] < array[j - 1]) {
                    int temp = array[j];
                    array[j] = array[j - 1];
                    array[j - 1] = temp;
                }
            }
        }

    }


    public static void test3(int[] array) {
        int h = 1;
        while (h < array.length / 3) {
            h = 3 * h + 1;
        }

        while (h >= 1) {
            for (int i = h; i < array.length; i++) {
                for (int j = i; j >= h; j -= h) {
                    if (array[j] < array[j - h]) {
                        int temp = array[j];
                        array[j] = array[j - h];
                        array[j - h] = temp;
                    }
                }
            }
            h = h / 3;
        }
    }


    public static void main(String[] args) {
        int[] array = new int[]{5, 8, 6, 3, 9, 2, 1, 7};
        //test1(array);
        System.out.println(Arrays.toString(array));

//        test2(array);
//        System.out.println(Arrays.toString(array));

        test3(array);
        System.out.println(Arrays.toString(array));
    }

}
