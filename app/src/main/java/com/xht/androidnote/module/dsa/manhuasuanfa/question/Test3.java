package com.xht.androidnote.module.dsa.manhuasuanfa.question;

/**
 * Created by xht on 2020/1/8.
 *
 * 求最大公约数、判断一个数是否为2的整数次幂
 *
 */
public class Test3 {


    public static int test1(int a, int b) {
        int big = a > b ? a : b;
        int small = a < b ? a : b;

        if (big % small == 0) {
            return small;
        }

        return test1(big % small, small);
    }


    public static int test2(int a, int b) {
        if (a == b) {
            return a;
        }

        int big = a > b ? a : b;

        int small = a < b ? a : b;

        int diff = big - small;

        return test2(diff, small);
    }

    public static int test3(int a, int b) {
        if (a == b) {
            return a;
        }

        if ((a & 1) == 0 && (b & 1) == 0) {
            return test3(a >> 1, b >> 1) << 1;
        } else if ((a & 1) == 0 && (b & 1) != 0) {
            return test3(a >> 1, b);
        } else if ((a & 1) != 0 && (b & 1) == 0) {
            return test3(a, b >> 1);
        } else {
            int big = a > b ? a : b;
            int small = a < b ? a : b;

            int diff = big - small;

            return test3(diff, small);
        }
    }

    public static boolean isPowerOf2(int num) {
        return (num&num-1)==0;
    }

    public static void main(String[] args) {
        int commonDivisor = test2(35, 7);

        System.out.println("commonDivisor=" + commonDivisor);

        //1010  >>1     0101 = 5
        //1010  >>2     0010 = 2

        //System.out.println("8>>1=" + (8 << 1));

        System.out.println("64 " + isPowerOf2(63));
    }


}
