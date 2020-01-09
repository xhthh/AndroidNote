package com.xht.androidnote.module.dsa.question;

import java.util.Arrays;

/**
 * Created by xht on 2020/1/9.
 * 给出一个正整数，找出这个正整数所有数字全排列的下一个数。
 */
public class Test6 {


    public static int[] findNearestNumber(int[] numbers) {

        int index = findTransferPoint(numbers);


        if (index == 0) {//已经逆序
            return null;
        }

        int[] numbersCopy = Arrays.copyOf(numbers, numbers.length);

        exchangeHead(numbersCopy, index);

        reverse(numbersCopy, index);

        return numbersCopy;
    }

    private static void reverse(int[] numbers, int index) {
        //12453
        for (int i = index, j = numbers.length - 1; i < j; i++, j--) {
            int temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }
    }

    private static void exchangeHead(int[] numbers, int index) {
        //12354
        int head = numbers[index - 1];

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
