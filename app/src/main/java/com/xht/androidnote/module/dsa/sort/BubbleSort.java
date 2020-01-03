package com.xht.androidnote.module.dsa.sort;

import java.util.Arrays;

/**
 * Created by xht on 2020/1/2.
 */
public class BubbleSort {


    public static void main(String[] args) {
        BubbleSort bubbleSort = new BubbleSort();

        /*int[] array = new int[]{5, 8, 6, 3, 9, 2, 1, 7};

        System.out.println(Arrays.toString(array) + "\n");

        bubbleSort.test1(array);

        System.out.println(Arrays.toString(array));*/


        int[] array = new int[]{2, 3, 4, 5, 6, 7, 8, 1};

        System.out.println(Arrays.toString(array) + "\n");
        bubbleSort.tes4(array);
        System.out.println(Arrays.toString(array) + "\n");


    }


    /**
     * {5, 8, 6, 3, 9, 2, 1, 7}
     * {5, 6, 3, 8, 2, 1, 7, 9}
     * <p>
     * O(n²)
     */
    public void test1(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                int temp;
                if (array[j] > array[j + 1]) {
                    temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;

                }
            }
        }
    }


    public void test2(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            //有序标记，每一轮的初始值都是true
            boolean isSorted = true;
            for (int j = 0; j < array.length - i - 1; j++) {
                int temp;
                if (array[j] > array[j + 1]) {
                    temp = array[j];
                    array[j + 1] = array[j];
                    array[j] = temp;

                    //因为有元素进行交换，所以不是有序的，标记变为false
                    isSorted = false;
                }
            }

            if (isSorted) {
                break;
            }
        }
    }

    /**
     * {3, 4, 2, 1, 5, 6, 7, 8}
     *
     * @param array
     */
    public void test3(int[] array) {

        //记录最后一个交换的位置
        int lastExchangeIndex = 0;

        //无需数列的边界，每次比较只需要比到这里为止
        int sortBorder = array.length - 1;

        for (int i = 0; i < array.length - 1; i++) {
            //有序标记，每一轮的初始值都为true
            boolean isSorted = true;

            for (int j = 0; j < sortBorder; j++) {
                int temp = 0;

                if (array[j] > array[j + 1]) {
                    temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;

                    //因为有元素进行交换，所以不是有序的，标记变为false
                    isSorted = false;

                    //更细最后一次交换的位置
                    lastExchangeIndex = j;
                }
            }

            sortBorder = lastExchangeIndex;

            if (isSorted) {
                break;
            }
        }
    }


    /**
     * {2,3,4,5,6,7,8,1}
     *
     * @param array
     */
    public void tes4(int[] array) {

        int temp = 0;

        for (int i = 0; i < array.length / 2; i++) {
            //有序标记，每一轮的初始值都是true
            boolean isSorted = true;
            //奇数轮，从左向右比较交换
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;

                    //有元素交换，所以不是有序的，标记置为false
                    isSorted = false;
                }
            }

            if (isSorted) {
                break;
            }

            //在偶数轮之前，将isSorted重新标记为true
            isSorted = true;

            //偶数轮，从右向左比较和交换
            for (int j = array.length - i - 1; j > i; j--) {
                if (array[j] < array[j - 1]) {
                    temp = array[j];
                    array[j] = array[j - 1];
                    array[j - 1] = temp;

                    //因为有元素进行交换，所以不是有序的，标记变为false
                    isSorted = false;
                }
            }

            if (isSorted) {
                break;
            }
        }
    }

}
