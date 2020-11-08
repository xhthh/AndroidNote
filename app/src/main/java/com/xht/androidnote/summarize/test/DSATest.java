package com.xht.androidnote.summarize.test;

public class DSATest {

    public static void main(String[] args) {

        String str = reverseStr("abcdefg");

        System.out.println(str);


        String newStr = reverseStrK("12345678", 3);
    }

    private static String reverseStrK(String str, int k) {
        char[] chars = str.toCharArray();

        for (int i = 0; i < chars.length; i += k) {

        }

        return null;
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
