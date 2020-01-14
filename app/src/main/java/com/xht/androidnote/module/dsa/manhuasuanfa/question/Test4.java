package com.xht.androidnote.module.dsa.manhuasuanfa.question;

import java.util.Arrays;

/**
 * Created by xht on 2020/1/8.
 */
public class Test4 {


    public static int test(int[] array) {

        int max = array[0];
        int min = array[0];

        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }

            if (array[i] < min) {
                min = array[i];
            }
        }

        int d = max - min;

        if (d == 0) {
            return 0;
        }

        int bucketNum = array.length;

        Bucket[] buckets = new Bucket[bucketNum];
        for (int i = 0; i < bucketNum; i++) {
            buckets[i] = new Bucket();
        }

        //区间跨度 = （最大值-最小值）/ （桶的数量 - 1）
        for (int i = 0; i < bucketNum; i++) {
            int index = ((array[i] - min) * (bucketNum - 1) / d);

            //[2, 6, 3, 4, 5, 10, 9]

            //[0, 3, 0, 1, 2, 6, 5]
            //System.out.println("index=" + index);

            if (buckets[index].min == null || buckets[index].min > array[i]) {
                buckets[index].min = array[i];
            }

            if (buckets[index].max == null || buckets[index].max < array[i]) {
                buckets[index].max = array[i];
            }
        }

        //分好桶后，当前桶内的最小值-前一个桶的最大值，再和之前所得的差值进行比较，找出最大的
        int leftMax = buckets[0].max;
        int maxDistance = 0;
        for (int i = 0; i < bucketNum; i++) {
            if (buckets[i].min == null) {
                continue;
            }

            if (buckets[i].min - leftMax > maxDistance) {
                maxDistance = buckets[i].min - leftMax;
            }

            leftMax = buckets[i].max;
        }

        return maxDistance;
    }


    /**
     * 缺点：如果最大值和最小值相差太大，则效率低
     *
     * @param array
     * @return
     */
    public static int test2(int[] array) {
        int max = array[0];
        int min = array[0];

        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
            if (array[i] < min) {
                min = array[i];
            }
        }

        int[] countArray = new int[max - min + 1];

        for (int i = 0; i < array.length; i++) {
            countArray[array[i] - min]++;
        }

        System.out.println("countArray=" + Arrays.toString(countArray));
        //[1, 1, 1, 2, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1]

        int cont = 0;
        int maxNum = 0;
        for (int i = 0; i < countArray.length; i++) {
            //判断0值最多连续出现的次数
            if (countArray[i] == 0) {
                cont++;
                if (cont > maxNum) {
                    maxNum = cont;
                }
            } else {
                cont = 0;
            }
        }

        return maxNum + 1;
    }


    public static void main(String[] args) {
        int[] array = new int[]{2, 5, 3, 4, 5, 10, 105};




        int maxDistance1 = test(array);
        System.out.println("最大差值1：" + maxDistance1);

        int maxDistance2 = test2(array);
        System.out.println("最大差值2：" + maxDistance2);


    }


    static class Bucket {
        Integer max;
        Integer min;
    }
}
