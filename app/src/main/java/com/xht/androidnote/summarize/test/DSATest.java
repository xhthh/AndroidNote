package com.xht.androidnote.summarize.test;

public class DSATest {

    public static void main(String[] args) {

        String str = reverseStr("abcdefg");

        System.out.println(str);


        String newStr = reverseStrK("12345678", 3);
        System.out.println(newStr);

    }

    private static String reverseStrK(String str, int k) {
        char[] chars = str.toCharArray();
        int n = chars.length;
        for (int i = 0; i < n; i += k) {
            int left = i;
            int right = (i + k - 1 < n) ? (i + k - 1) : n - 1;
            while (left <= right) {
                char temp = chars[left];
                chars[left] = chars[right];
                chars[right] = temp;
                left++;
                right--;
            }
        }
        return new String(chars);
    }


    private static String reverseStr(String str) {
        char[] chars = str.toCharArray();

        StringBuilder sb = new StringBuilder();
        for (int i = chars.length - 1; i >= 0; i--) {
            sb.append(chars[i]);
        }

        return new String(sb);
    }
}
