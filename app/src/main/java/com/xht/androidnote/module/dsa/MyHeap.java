package com.xht.androidnote.module.dsa;

import java.util.Arrays;

/**
 * Created by xht on 2019/12/31.
 */
public class MyHeap {

    public static void main(String[] args) {
        int[] array = new int[]{1, 3, 2, 6, 5, 7, 8, 9, 10, 0};

        System.out.println(Arrays.toString(array));
        upAdjust(array);

        System.out.println(Arrays.toString(array));

        /*array = new int[]{7, 1, 3, 10, 5, 2, 8, 9, 6};

        buildHeap(array);

        System.out.println(Arrays.toString(array));*/


    }


    /*
                          1
                   3             2
              6        5     7        8
            9   10  0

                          1
                   3             2
              6        5     7        8
            9   10  5

                          1
                   3             2
              6        3     7        8
            9   10  5

                          1
                   1             2
              6        3     7        8
            9   10  5


                           0
                   1             2
              6        3     7        8
            9   10  5
     */
    public static void upAdjust(int[] array) {
        int childIndex = array.length - 1;
        int parentIndex = (childIndex - 1) / 2;

        //9     4       1       0
        //4     1       0       0

        //temp 保存插入的叶子节点值，用于最后的赋值
        int temp = array[childIndex];

        while (childIndex > 0 && temp < array[parentIndex]) {
            array[childIndex] = array[parentIndex];

            childIndex = parentIndex;

            parentIndex = (childIndex - 1) / 2;
        }

        array[childIndex] = temp;


    }


    /*
                    {7,1,3,10,5,2,8,9,6}

                            7
                     1             3
                10        5    2        8
             9      6


                            1
                     1             3
                10        5    2        8
             9      6
     */
    public static void downAdjust(int[] array, int parentIndex, int length) {
        // temp 保存父节点值，用于最后的赋值
        int temp = array[parentIndex];

        int childIndex = 2 * parentIndex + 1;

        while (childIndex < length) {
            // 如果有右孩子，且右孩子小于左孩子的值，则定位到右孩子
            if (childIndex + 1 < length && array[childIndex + 1] < array[childIndex]) {
                childIndex++;
            }

            // 如果父节点小于任何一个孩子的值，则直接跳出
            if (temp <= array[childIndex])
                break;

            //无须真正交换，单向赋值即可
            array[parentIndex] = array[childIndex];

            parentIndex = childIndex;

            childIndex = childIndex * 2 + 1;

        }

        array[parentIndex] = temp;


    }


    /*
     * {7, 1, 3, 10, 5, 2, 8, 9, 6}
     *                    7
                 1                  3
            10        5         2        8
         9     6    4    0
     *
     * @param array
     */
    public static void buildHeap(int[] array) {

        //从最后一个非叶子节点开始，依次做下沉调整
        for (int i = (array.length - 2) / 2; i >= 0; i--) {
            downAdjust(array, i, array.length);
        }

    }


}
