package com.xht.androidnote.module.dsa.manhuasuanfa.question;

import java.util.Arrays;

/**
 * Created by xht on 2020/1/9.
 * 给出一个正整数，找出这个正整数所有数字全排列的下一个数。
 */
public class Test6 {


    public static int[] findNearestNumber(int[] numbers) {

        //1、从后向前查看逆序区域，找到逆序区域的前一位，也就是数字置换的边界
        int index = findTransferPoint(numbers);

        //如果数字置换边界是0，说明整个数组已经逆序，无法得到更大的相同数字组成的整数，返回null
        if (index == 0) {//已经逆序
            return null;
        }

        //2、把逆序区域的前一位和逆序区域中刚刚大于它的数字交换位置
        //复制并入参，避免直接修改入参
        int[] numbersCopy = Arrays.copyOf(numbers, numbers.length);

        exchangeHead(numbersCopy, index);

        //3、把原来的逆序区域转为顺序
        reverse(numbersCopy, index);

        return numbersCopy;
    }

    private static void reverse(int[] numbers, int index) {
        //将逆序区域转为顺序
        //12453 =》12435
        for (int i = index, j = numbers.length - 1; i < j; i++, j--) {
            int temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }
    }

    private static void exchangeHead(int[] numbers, int index) {
        //12354
        //逆序区域的前一位
        int head = numbers[index - 1];

        //从后往前遍历，找出第一个大于head的值，进行交换，然后跳出循环
        for (int i = numbers.length - 1; i > 0; i--) {
            if (head < numbers[i]) {
                numbers[index - 1] = numbers[i];
                numbers[i] = head;
                break;//交换一次就跳出，否则下一个比head大的也会替换，数值就偏大了
            }
        }
    }

    private static int findTransferPoint(int[] numbers) {
        for (int i = numbers.length - 1; i > 0; i--) {
            if (numbers[i] > numbers[i - 1]) {
                return i;
            }
        }

        return 0;
    }


    public static void main(String[] args) {
        int number = 12354;


        String str = String.valueOf(number);

        int[] array = new int[str.length()];

        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.parseInt(String.valueOf(str.charAt(i)));
        }

        int[] nearestNumber = findNearestNumber(array);

        System.out.println(Arrays.toString(nearestNumber));
    }


}
