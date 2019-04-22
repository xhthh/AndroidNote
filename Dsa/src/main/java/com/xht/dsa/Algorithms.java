package com.xht.dsa;

/**
 * Created by xht on 2019/4/15.
 */

public class Algorithms {

    public static void main(String[] args) {
        System.out.println(nCF(1024));
    }

    /**
     * 算2的N次方数据，比较N，小于继续乘以2.等于的话就是2的N次方。大于的话就不是2的N次方。
     * @param num
     * @return
     */
    public static boolean nCF(int num) {
        boolean b;

        int x = 2;
        while (true) {
            if (x == num) {
                b = true;
                break;
            } else if (x < num) {
                x = x * 2;
            } else {
                b = false;
                break;
            }
        }

        return b;
    }

    /**
     * 判断一个数是否为2的n次方
     *
     * 循环除以2
     *
     * @param num
     * @return
     */
    public static boolean nCF2(int num) {
        boolean b;

        while (true) {
            int i = num % 2;//如果取模后，为1，自然就不是
            System.out.println("i==" + i);
            if (i == 1) {
                b = false;
                break;
            }
            num = num / 2;//不断除以2，最终如果取模不为1，且除2为1，即2的n次方
            System.out.println("num==" + num);
            if (num == 1) {
                b = true;
                break;
            }
        }

        return b;
    }

}
