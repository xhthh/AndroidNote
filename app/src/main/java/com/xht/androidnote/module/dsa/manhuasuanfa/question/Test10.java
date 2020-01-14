package com.xht.androidnote.module.dsa.manhuasuanfa.question;

/**
 * Created by xht on 2020/1/13.
 */
public class Test10 {


    private static int[] findLostNum(int[] array) {
        //用于存储2个出现奇数次的整数
        int[] result = new int[2];

        //第一次进行整体异或运算
        int xorResult = 0;
        for (int i = 0; i < array.length; i++) {
            xorResult ^= array[i];
        }

        //如果进行异或运算的结果为0，则说明输入的数组不符合题目要求
        if (xorResult == 0) {//异或运算：针对二进制，相同为0，不同为1
            return null;
        }

        //确定两个整数的不同位，以此来分组
        int separator = 1;

        //第一次异或运算的结果与0001进行与运算，如果不等于0，说明某一位是1，如果为0，左移separator，继续按位与进行比较
        while (0 == (xorResult & separator)) {//与运算：针对二进制，只要有一个为0，就为0
            separator <<= 1;//向左位移：针对二进制，向左位移一位 0001 << 1 --> 0010
        }

        //第二次分组进行异或运算，separator=2 -->   0010，即两个出现奇数次的整数，不同的位置是第2位，所以这个两个数与separator进行按位与，分别为0和1两种结果，也就区分出了两个数，然后分别进行异或运算，其他出现偶数次的整数异或运算的结果是0，所以最终结果得到的就是出现奇数次的那个整数
        for(int i = 0; i < array.length; i++) {
            if (0 == (array[i] & separator)) {//
                result[0] ^= array[i];
            } else {
                result[1] ^= array[i];
            }
        }

        return result;
    }


    public static void main(String[] args) {
        int[] array = {4, 1, 2, 2, 5, 1, 4, 3};
        int[] result = findLostNum(array);
        System.out.println(result[0] + "," + result[1]);
    }


}
