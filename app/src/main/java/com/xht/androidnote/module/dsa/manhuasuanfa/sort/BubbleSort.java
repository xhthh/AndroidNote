package com.xht.androidnote.module.dsa.manhuasuanfa.sort;

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
     * 最普通的
     * 两个元素进行比较，然后交换
     *
     * 注意内层循环的范围：array.length - i - 1
     * 因为外层每比较一轮，都会排好一个，排好的不用比，所以 - i
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


    /**
     * 优化一
     *
     * 设置一个标记，是否有序，默认为 true，如果有元素交换，即无序，标记置为 false
     * 下一轮如果 标记不变，一直为true，即已经有序，则跳出循环，不用再比较
     *
     * @param array
     */
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
     * 优化二
     *
     * 有的序列，一部分可能已经有序，可能会进行无用的比较
     * 问题关键在于对数列有序区的界定
     *
     * 记录最后一个交换的位置，一个无序数列的边界标志（每次只需要比较到这里即可，作为内层循环的范围）
     *
     * 在有元素交换时，记录交换位置，在内层循环外，即一轮遍历结束后，将交换位置赋给无序边界
     * 即下一轮比较只需比到此边界即可
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
     * 鸡尾酒排序
     *
     * 从左到右一遍，然后从右到左一遍
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
