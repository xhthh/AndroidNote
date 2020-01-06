package com.xht.androidnote.module.dsa.sort;

import java.util.Arrays;

/**
 * Created by xht on 2020/1/3.
 */
public class Test {


    public static void main(String[] args) {
        Test test = new Test();

        int[] arr = new int[]{4, 4, 6, 5, 3, 2, 8, 1};

        System.out.println(Arrays.toString(arr) + "\n");
//        test.test1(arr, 0, arr.length - 1);
        test.test2(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));

    }


    /*
        {4, 4, 6, 5, 3, 2, 8, 1}


     */
    public void test1(int[] array, int startIndex, int endIndex) {

        //递归结束条件：startIndex大于或等于endIndex时
        if (startIndex >= endIndex) {
            return;
        }

        //得到基准元素位置
        int pivotIndex = partition(array, startIndex, endIndex);

        System.out.println("pivotIndex=" + pivotIndex + "  " + Arrays.toString(array) + "\n");

        test1(array, startIndex, pivotIndex - 1);//基准元素左边
        test1(array, pivotIndex + 1, endIndex);//基准元素右边
    }

    /*
        {4, 4, 6, 5, 3, 2, 8, 1}                              left=0  right=7  pivot=4
        {4, 4, 6, 5, 3, 2, 8, 1}=>{4, 4, 1, 5, 3, 2, 8, 6}    left=2  right=7  pivot=4
        {4, 4, 1, 5, 3, 2, 8, 6}=>{4, 4, 1, 2, 3, 5, 8, 6}    left=3  right=5  pivot=4
        {4, 4, 1, 2, 3, 5, 8, 6}=>{4, 4, 1, 2, 3, 5, 8, 6}    left=4  right=4  pivot=4

        {3, 4, 1, 2, 4, 5, 8, 6}                              left=4  right=4  pivot=4


     */
    private int partition(int[] array, int startIndex, int endIndex) {
        //取第1个位置（也可以选择随机位置）的元素作为基准元素
        int pivot = array[startIndex];

        int left = startIndex;
        int right = endIndex;

        while (left != right) {
            //控制right指针比较并左移
            while (left < right && array[right] > pivot) {
                right--;
            }

            //控制left指针比较并右移
            while (left < right && array[left] <= pivot) {
                left++;
            }

            //交换left和right 指针所指向的元素
            if (left < right) {
                int p = array[right];
                array[right] = array[left];
                array[left] = p;
            }
            System.out.println("left==" + left + "  right==" + right + " " + Arrays.toString(array));
        }

        array[startIndex] = array[left];
        array[left] = pivot;


        return left;
    }


    public void test2(int[] arrary, int startIndex, int endIndex) {
        if (startIndex >= endIndex) {
            return;
        }

        int pivotIndex = partition2(arrary, startIndex, endIndex);
        test2(arrary, 0, pivotIndex - 1);
        test2(arrary, pivotIndex + 1, endIndex);
    }

    private int partition2(int[] array, int startIndex, int endIndex) {
        int pivot = array[startIndex];

        int mark = startIndex;

        for (int i = startIndex + 1; i <= endIndex; i++) {
            if (array[i] < pivot) {
                mark++;

                int p = array[mark];
                array[mark] = array[i];
                array[i] = p;
            }
        }

        array[startIndex] = array[mark];
        array[mark] = pivot;

        return mark;
    }

}
