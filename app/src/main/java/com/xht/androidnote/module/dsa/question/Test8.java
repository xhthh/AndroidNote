package com.xht.androidnote.module.dsa.question;

/**
 * Created by xht on 2020/1/10.
 */
public class Test8 {


    public static String bigNumberSum(String numA, String numB) {
        int maxLength = numA.length() > numB.length() ? numA.length() : numB.length();

        int[] arrayA = new int[maxLength + 1];
        int[] arrayB = new int[maxLength + 1];

        for (int i = 0; i < numA.length(); i++) {
            arrayA[i] = numA.charAt(numA.length() - i - 1) - '0';//-'0' 是将字符转为int？
        }

        for (int i = 0; i < numB.length(); i++) {
            arrayB[i] = numB.charAt(numB.length() - i - 1) - '0';
        }

        int[] result = new int[maxLength + 1];

        /*int extra = 0;
        for (int i = 0; i < result.length; i++) {
            int num = arrayA[i] + arrayB[i] + extra;

            if (num < 10) {
                extra = 0;
                result[i] = num;
            } else if (num == 10) {
                extra = 1;
                result[i] = 0;
            } else if (num > 10) {
                extra = 1;
                result[i] = num - 10;
            }
        }*/

        for (int i = 0; i < result.length; i++) {
            int temp = result[i];
            temp += arrayA[i];
            temp += arrayB[i];

            if (temp >= 10) {
                temp = temp - 10;
                result[i + 1] = 1;
            }

            result[i] = temp;
        }

        StringBuilder sb = new StringBuilder();
        boolean findFirst = false;
        for (int i = result.length - 1; i >= 0; i--) {
            if (!findFirst) {
                if (result[i] == 0) {
                    continue;
                }

                findFirst = true;
            }

            sb.append(result[i]);
        }

        return sb.toString();


        /*for (int i = 0, j = result.length - 1; i < j; i++, j--) {
            int temp = result[i];
            result[i] = result[j];
            result[j] = temp;
        }

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < result.length; i++) {
            buffer.append(result[i]);
        }

        String sum = buffer.toString();

        while (sum.startsWith("0")) {
            sum = sum.substring(1);
        }

        return sum;*/
    }

    public static void main(String[] args) {
        /*
                426709752318
                 95481253129

                522191005447

         */
        System.out.println(bigNumberSum("426709752318", "95481253129"));
    }

}
