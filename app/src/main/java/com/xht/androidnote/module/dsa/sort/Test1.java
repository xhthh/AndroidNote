package com.xht.androidnote.module.dsa.sort;

import java.util.Arrays;

/**
 * Created by xht on 2020/1/16.
 * 选择排序
 */
public class Test1 {


    /**
     * 首先找到数组中最小的元素，将它和数组第一个元素进行交换，
     * 然后找到剩余数组中最小的元素，将它和数组第二个元素进行交换，
     * 如此往复，直到将整个数组排序。
     * 这种方法叫做选择排序，因为它在不断选择剩余元素中的最小者。
     *
     * @param array
     */
    public static void test(int[] array) {
        //比较N-1轮
        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }

            if (i != minIndex) {
                int temp = array[i];
                array[i] = array[minIndex];
                array[minIndex] = temp;
            }
        }
    }


    public static void main(String[] args) {
        int[] array = new int[]{5, 8, 6, 3, 9, 2, 1, 7};
        test(array);
        System.out.println(Arrays.toString(array));
    }
}
