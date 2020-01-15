package com.xht.androidnote.module.dsa.manhuasuanfa.sort;

import java.util.Arrays;

/**
 * Created by xht on 2020/1/15.
 */
public class SelectSort {


    /**
     * 在要排序的一组数中，选出最小的一个数与第一个位置的数交换；然后在剩下的数当中再找最小的
     * 与第二个位置的数交换，如此循环到倒数第二个数和最后一个数比较为止。
     *
     * O(n²)
     * @param array
     */
    public static void test(int[] array) {

        /*
            注意边界，以 {1, 3, 6, 5, 9, 2, 8} 为例
            length = 7，需要比较 length - 1 次，即剩下倒数第二位，和倒数第一位比较

            外层循环是比较次数，内层循环是 从剩余未排序的数组中找出最小的值，所以 j = i + 1
            边界是最后一个，即j < array.length   => array.length - 1
         */
        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < array.length; j++) {
                if (array[i] > array[j]) {
                    minIndex = j;
                }
            }

            if (i != minIndex) {//提高效率，如果当前位置即最小值，则无需交换
                int temp = array[i];
                array[i] = array[minIndex];
                array[minIndex] = temp;
            }
        }

    }


    public static void main(String[] args) {
        int[] array = new int[]{1, 3, 6, 5, 9, 2, 8};

        test(array);

        System.out.println(Arrays.toString(array));
    }
}
