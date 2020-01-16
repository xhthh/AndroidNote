package com.xht.androidnote.module.dsa.sort;

import java.util.Arrays;

/**
 * Created by xht on 2020/1/16.
 * 插入排序
 */
public class Test2 {

    /*
        将待排序序列第一个元素看作一个有序序列，把第二个元素到最后一个元素当成是未排序序列
        从头到尾依次扫描未排序序列，将扫描到的每个元素插入有序序列的适当位置
        (如果待插入的元素与有序序列中的某个元素相等，则将待插入元素插入相等元素的后面)
     */
    private static void test(int[] array) {

        //比较N-1轮
        for (int i = 1; i < array.length; i++) {
            for (int j = i; j > 0; j--) {//范围，当前元素前面
                int temp = array[j];

                //从小到大排，如果当前元素小于前一个元素，交换位置
                if (temp < array[j - 1]) {
                    array[j] = array[j - 1];
                    array[j - 1] = temp;
                }
            }
        }

    }


    public static void main(String[] args) {
        int[] array = new int[]{5, 8, 6, 3, 9, 2, 1, 7};
        test(array);
        System.out.println(Arrays.toString(array));
    }


}
