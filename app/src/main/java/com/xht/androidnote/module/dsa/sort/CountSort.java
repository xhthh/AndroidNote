package com.xht.androidnote.module.dsa.sort;

import java.util.Arrays;

/**
 * Created by xht on 2020/1/7.
 */
public class CountSort {


    public static void main(String[] args) {
        //int[] array = new int[]{4, 4, 6, 5, 3, 2, 8, 1, 7, 5, 6, 0, 10};
        int[] array = new int[]{95, 94, 91, 98, 99, 90, 99, 93, 91, 92};
        System.out.println(Arrays.toString(array) + "\n");

        int[] sortedArray = test2(array);

        System.out.println(Arrays.toString(sortedArray));
    }


    public static int[] test1(int[] array) {

        //1、得到数列最大值
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        //2、根据数列最大值确定统计数组的长度
        //统计数countArray，长度是max+1，以此来保证数组的最后一个下标是max
        int[] countArray = new int[max + 1];

        //3、遍历数列，填充统计数组
        for (int i = 0; i < array.length; i++) {
            countArray[array[i]]++;
        }

        System.out.println("countArray=" + Arrays.toString(countArray) + "\n");

        //4、遍历统计数组，输出结果 countArray=[1, 1, 1, 1, 2, 2, 2, 1, 1, 0, 1]
        int index = 0;
        int[] sortedArray = new int[array.length];
        for (int i = 0; i < countArray.length; i++) {
            for (int j = 0; j < countArray[i]; j++) {//统计数组大于1的即原数组中有多个相同的元素
                sortedArray[index++] = i;//i即元素值，统计数组表示的就是该元素有几个
            }
        }

        return sortedArray;
    }


    public static int[] test2(int[] array) {

        //1、得到数列最大值和最小值，并算出差值
        int max = array[0];
        int min = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }

            if (array[i] < min) {
                min = array[i];
            }
        }

        int d = max - min;

        //2、创建统计数组并统计对应元素的个数
        int[] countArray = new int[d + 1];

        for (int i = 0; i < array.length; i++) {
            countArray[array[i] - min]++;
        }

        System.out.println("统计数组    " + Arrays.toString(countArray));

        //3、统计数组做变形，后面的元素等于前面的元素之和，


        /*
            目的是让统计数组存储的元素值，等于相应整数的最终排序位置的序号。

            原数组{95, 94, 91, 98, 99, 90, 99, 93, 91, 92}
            最小 90   最大 99

            比如统计数组[1, 2, 1, 1, 1, 1, 0, 0, 1, 2] 已经是有序的，表示 90的 有一个，91的有两个，92的有一个。。。
            后面的元素等于前面的元素之和，变形后：
            变形后的数组[1, 3, 4, 5, 6, 7, 7, 7, 8, 10]

            从后向前遍历，92-90=2  2-1=1 统计数组位置1 值为3，即92占排序后的数组第三位
            [1, 3, 3, 5, 6, 7, 7, 7, 8, 10]



            统计数组2   [1, 3, 3, 5, 6, 7, 7, 7, 8, 10]
            排列数组    [0, 0, 0, 92, 0, 0, 0, 0, 0, 0]

            统计数组2   [1, 2, 3, 5, 6, 7, 7, 7, 8, 10]
            排列数组    [0, 0, 91, 92, 0, 0, 0, 0, 0, 0]

            统计数组2   [1, 2, 3, 4, 6, 7, 7, 7, 8, 10]
            排列数组    [0, 0, 91, 92, 93, 0, 0, 0, 0, 0]

            统计数组2   [1, 2, 3, 4, 6, 7, 7, 7, 8, 9]
            排列数组    [0, 0, 91, 92, 93, 0, 0, 0, 0, 99]

            统计数组2   [0, 2, 3, 4, 6, 7, 7, 7, 8, 9]
            排列数组    [90, 0, 91, 92, 93, 0, 0, 0, 0, 99]

            统计数组2   [0, 2, 3, 4, 6, 7, 7, 7, 8, 8]
            排列数组    [90, 0, 91, 92, 93, 0, 0, 0, 99, 99]

            统计数组2   [0, 2, 3, 4, 6, 7, 7, 7, 7, 8]
            排列数组    [90, 0, 91, 92, 93, 0, 0, 98, 99, 99]

            统计数组2   [0, 1, 3, 4, 6, 7, 7, 7, 7, 8]
            排列数组    [90, 91, 91, 92, 93, 0, 0, 98, 99, 99]

            统计数组2   [0, 1, 3, 4, 5, 7, 7, 7, 7, 8]
            排列数组    [90, 91, 91, 92, 93, 94, 0, 98, 99, 99]

            统计数组2   [0, 1, 3, 4, 5, 6, 7, 7, 7, 8]
            排列数组    [90, 91, 91, 92, 93, 94, 95, 98, 99, 99]




            [90, 91, 91, 92, 93, 94, 95, 98, 99, 99]
         */
        for (int i = 1; i < countArray.length; i++) {
            countArray[i] += countArray[i - 1];
        }
        System.out.println("统计数组变形 " + Arrays.toString(countArray));


        //4、倒序遍历原始数列，从统计数组找到正确位置，输出到结果数组
        int[] sortedArray = new int[array.length];
        for (int i = array.length - 1; i >= 0; i--) {
            sortedArray[countArray[array[i] - min] - 1] = array[i];
            countArray[array[i] - min]--;

            System.out.println("统计数组2 " + Arrays.toString(countArray));
            System.out.println("排列数组 " + Arrays.toString(sortedArray));
        }

        return sortedArray;
    }


}
