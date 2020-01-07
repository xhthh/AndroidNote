package com.xht.androidnote.module.dsa.sort;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by xht on 2020/1/3.
 */
public class QuickSort {


    public static void main(String[] args) {
        QuickSort quickSort = new QuickSort();

        int[] arr = new int[]{4, 4, 6, 5, 3, 2, 8, 1};

        System.out.println(Arrays.toString(arr) + "\n");
        //        quickSort.test1(arr, 0, arr.length - 1);
        //        quickSort.test2(arr, 0, arr.length - 1);
        quickSort.test3(arr, 0, arr.length - 1);
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
        int pivotIndex = partition1(array, startIndex, endIndex);

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
    private int partition1(int[] array, int startIndex, int endIndex) {
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


    /**
     * 非递归实现
     * @param array
     * @param startIndex
     * @param endIndex
     */
    public void test3(int[] array, int startIndex, int endIndex) {
        //用一个集合栈来代替递归的函数栈
        Stack<Map<String, Integer>> quickSortStack = new Stack<>();

        //整个数列的起止下标，以哈希的形式入栈
        Map rootParam = new HashMap();
        rootParam.put("startIndex", startIndex);
        rootParam.put("endIndex", endIndex);
        quickSortStack.push(rootParam);

        //循环结束条件：当栈为空时
        while (!quickSortStack.isEmpty()) {
            //栈顶元素出栈得到起止下标
            Map<String, Integer> param = quickSortStack.pop();
            //得到基准元素位置
            int pivotIndex = partition3(array, param.get("startIndex"), param.get("endIndex"));

            //根据基准元素分分成两部分，把每一部分的起止下标入栈
            if (param.get("startIndex") < pivotIndex - 1) {
                Map<String, Integer> leftParam = new HashMap<>();
                leftParam.put("startIndex", param.get("startIndex"));
                leftParam.put("endIndex", pivotIndex - 1);
                quickSortStack.push(leftParam);
            }

            if (param.get("endIndex") >= pivotIndex + 1) {
                Map<String, Integer> rightParam = new HashMap<>();
                rightParam.put("startIndex", pivotIndex + 1);
                rightParam.put("endIndex", param.get("endIndex"));
                quickSortStack.push(rightParam);
            }
        }
    }

    private int partition3(int[] array, int startIndex, int endIndex) {
        int pivot = array[startIndex];
        int mark = startIndex;

        for (int i = startIndex + 1; i <= endIndex; i++) {
            if (array[i] < pivot) {
                mark++;
                int temp = array[mark];
                array[mark] = array[i];
                array[i] = temp;
            }
        }

        array[startIndex] = array[mark];
        array[mark] = pivot;

        return mark;
    }
}
