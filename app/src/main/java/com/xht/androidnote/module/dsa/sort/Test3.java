package com.xht.androidnote.module.dsa.sort;

import java.util.Arrays;

/**
 * Created by xht on 2020/1/16.
 * 希尔排序
 */
public class Test3 {

    /*
        先将整个待排序的记录序列分割成为若干子序列分别进行插入排序，
        待整个序列中的记录"基本有序"时，再对全体记录进行插入排序



        {5, 8, 6, 3, 9, 2, 1, 7}

        {5,          9}
           {8,          2}
              {6,          1}
                 {3,          7}


        {5,          9}
           {2,          8}
              {1,          6}
                 {3,          7}

         第二轮，h = 1，{5, 2, 1, 3, 9, 8, 6, 7}，依次进行插入排序
     */
    public static void test(int[] array) {

        int h = 1;

        while (h < array.length / 3) {
            h = 3 * h + 1;//1、4、7。。
        }

        while (h >= 1) {
            //将数组变为h有序
            for (int i = h; i < array.length; i++) {
                //将a[i]插入到a[i-h], a[i-2*h], a[i-3*h]。。。
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
        test(array);
        System.out.println(Arrays.toString(array));
    }
}
