package com.xht.androidnote.module.dsa.manhuasuanfa.question;

/**
 * Created by xht on 2020/1/10.
 */
public class Test7 {


    /*
            1593212
            153212



            1532
            1212

     */
    public static String removeKDigits1(String num, int k) {

        String newNum = num;

        for (int i = 0; i < k; i++) {
            boolean hasCut = false;
            //从左向右遍历，找到比自己右侧数字大的数字并删除
            for (int j = 0; j < newNum.length() - 1; j++) {
                if (newNum.charAt(j) > newNum.charAt(j + 1)) {
                    newNum = newNum.substring(0, j) + newNum.substring(j + 1);

                    hasCut = true;
                    break;
                }
            }

            //如果没有找到要删除的数字，则删除最后一个数字
            if (!hasCut) {
                newNum = newNum.substring(0, newNum.length() - 1);
            }

            //清除整数左侧的数字0
            newNum = removeZero(newNum);
        }

        //如果整数的所有数字都被删除了，直接返回0
        if (newNum.length() == 0) {
            return "0";
        }

        return newNum;
    }

    private static String removeZero(String newNum) {
        while (newNum.startsWith("0")) {
            newNum = newNum.substring(1);
        }

        return newNum;
    }


    public static String removeKDigits2(String num, int k) {

        //新整数的最终长度 = 原整数的最终长度 - k
        int newLength = num.length() - k;

        //创建一个栈，用于接收所有的数字
        char[] stack = new char[num.length()];

        int top = 0;

        /*
            i++是先进行一次循环，在进行累加；++i是i先进行一次累加，再循环。

            两个对于循环的次数都是没影响的，都是5次，都是当 i 小于5 不成立的时候跳出循环，但后面的
            i 还是会递增1的。

            两者不同的地方在于++i 占用的空间比 i++ 要小，i++是多定义一个变量的。看看运算符重载可以
            知道是怎么回事。

            在for循环中，如for(i=0;i<5;i++)与for(i=0;i<5;++i)在for循环的次数上没有区别，都是五
            次，因为i++和++i都是在for循环内的语句执行后才执行！
         */
        for (int i = 0; i < num.length(); ++i) {
            //遍历当前数字
            char c = num.charAt(i);
            //当栈顶数字大于遍历到的当前数字时，栈顶数字出栈（相当于删除数字）
            while (top > 0 && stack[top - 1] > c && k > 0) {
                top -= 1;
                k -= 1;
            }

            stack[top++] = c;
        }

        //找到栈中第1个非零数字的位置，以此构建新的整数字符串
        int offset = 0;

        while (offset < newLength && stack[offset] == '0') {
            offset++;
        }

        return offset == newLength ? "0" : new String(stack, offset, newLength - offset);
    }

    public static void main(String[] args) {
        System.out.println("num=" + removeKDigits1("1593212", 3));
        System.out.println("num=" + removeKDigits2("1593212", 3));
        //        System.out.println("num=" + removeKDigits1("5093212", 1));
        //        System.out.println("num=" + removeKDigits2("5093212", 1));
        System.out.println("num=" + removeKDigits1("5000000", 1));
        System.out.println("num=" + removeKDigits2("5000000", 1));
    }

}
