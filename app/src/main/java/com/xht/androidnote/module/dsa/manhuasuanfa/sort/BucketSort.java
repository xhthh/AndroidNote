package com.xht.androidnote.module.dsa.manhuasuanfa.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by xht on 2020/1/7.
 */
public class BucketSort {


    public static void main(String[] args) {
        double[] array = new double[]{4.12, 6.421, 0.0023, 3.0, 2.123, 8.122, 4.12, 10.09};

        System.out.println(Arrays.toString(array) + "\n");

        double[] sortArray = test1(array);

        System.out.println(Arrays.toString(sortArray));
    }


    private static double[] test1(double[] array) {

        //1、得到数列的最大值和最小值，并算出差值
        double max = array[0];
        double min = array[0];

        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }

            if (array[i] < min) {
                min = array[i];
            }
        }

        double d = max - min;

        int bucketNum = array.length;

        ArrayList<LinkedList<Double>> bucketList = new ArrayList<>(bucketNum);

        for (int i = 0; i < bucketNum; i++) {
            bucketList.add(new LinkedList<>());
        }

        //3、遍历原始数组，将每一个元素放入桶中
        for (int i = 0; i < array.length; i++) {
            int num = (int) ((array[i] - min) * (bucketNum - 1) / d);
            bucketList.get(num).add(array[i]);
        }

        //4、对每个桶内部进行排序
        for (int i = 0; i < bucketList.size(); i++) {
            //JDK 底层采用了归并排序或归并的优化版本
            Collections.sort(bucketList.get(i));
        }

        //输出全部元素
        double[] sortArray = new double[array.length];
        int index = 0;
        for (LinkedList<Double> list : bucketList) {
            for (double element : list) {
                sortArray[index++] = element;
            }
        }


        return sortArray;
    }
}
