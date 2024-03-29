package com.xht.androidnote.summarize.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DSATest {

    public static void main(String[] args) {
        //测试链表
        //        testLinkedList();
        //测试字符串
//        testString();

        //测试数组
        //testArray();

        //是否为2的整次幂
        //        boolean r1 = isPowerOf2(4);
        //        boolean r2 = isPowerOf2(7);
        //        System.out.println("r1 = " + r1 + "  r2 = " + r2);

        //测试栈、队列
        //        testStack();

        //最大公约数
        int i = gcd3(100, 40);
        System.out.println(i);
    }

    /**
     * 求两个数的最大公约数
     * 辗转相除法：两个正整数a和b（a>b），它们的最大公约数等于a除以b的余数c和b之间的最大公约数。
     *
     * @param a
     * @param b
     * @return
     */
    private static int getGreatestCommonDivisor1(int a, int b) {
        int big = a > b ? a : b;
        int small = a < b ? a : b;

        if (big % small == 0) {
            return small;
        }

        return getGreatestCommonDivisor1(b, a % b);
    }

    /**
     * 更相减损术：两个正整数a和b（a>b），它们的最大公约数等于a-b的差值c和较小数b的最大公约数。
     * 更相减损术避免了大整数取模可能出现的性能问题
     *
     * @param a
     * @param b
     * @return
     */
    private static int gcd2(int a, int b) {
        if (a == b) {
            return a;
        }
        int big = a > b ? a : b;
        int small = a < b ? a : b;
        return gcd2(big - small, small);
    }

    /**
     * 如果(a&1)==0，则说明整数a是偶数；如果(a&1)!=0，则说明整数a是奇数。
     *
     * @param a
     * @param b
     * @return
     */
    private static int gcd3(int a, int b) {
        if (a == b) {
            return a;
        }
        if ((a & 1) == 0 && (b & 1) == 0) {
            return gcd3(a >> 1, b >> 1) << 1;
        } else if ((a & 1) == 0 && (b & 1) != 0) {
            return gcd3(a >> 1, b);
        } else if ((a & 1) != 0 && (b & 1) == 0) {
            return gcd3(a, b >> 1);
        } else {
            int big = a > b ? a : b;
            int small = a < b ? a : b;
            return gcd3(big - small, small);
        }
    }


    /**
     * 判断是否为2的整次幂
     * 2的整次幂，二进制最高位是1，其余位都是0
     * 2的整次幂减1，所有位都是1
     * 两者按位与，0&1 是 0
     * <p>
     * 7
     * 111
     * 110
     * <p>
     * 8
     * 1000
     * 0111
     *
     * @param n
     * @return
     */
    private static boolean isPowerOf2(int n) {
        return (n & n - 1) == 0;
    }


    /**
     * 测试数组
     */
    private static void testArray() {
        int[] nums = {1, 5, 4, 7, 9};
        int[] results = twoSum(nums, 8);
        System.out.println(Arrays.toString(results));
    }


    /**
     * 两数之和
     * 给定一个整数数组 nums和一个整数目标值 target，请你在该数组中找出 和为目标值 target 的那两个整数，并返回它们的数组下标。
     *
     * @param nums
     * @param target
     * @return
     */
    private static int[] twoSum(int[] nums, int target) {
        int n = nums.length;
        //        for (int i = 0; i < n; i++) {
        //            for (int j = i + 1; j < n; j++) {
        //                if (nums[i] + nums[j] == target) {
        //                    return new int[]{i, j};
        //                }
        //            }
        //        }

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; ++i) {
            if (map.containsKey(target - nums[i])) {
                return new int[]{map.get(target - nums[i]), i};
            }
            map.put(nums[i], i);
        }

        return new int[0];
    }


    //===========================================字符串===========================================
    private static void testString() {
        //        String newStr = reverseStr("abcdef");
        //        System.out.println(newStr);
        //
        //        System.out.println(reverseString("12345".toCharArray()));
        System.out.println(reverseStrK("abcdefg", 2));
    }

    /**
     * k个一组反转字符串
     * 123456789
     *
     * @param str
     * @param k
     * @return
     */
    private static String reverseStrK(String str, int k) {
        char[] chars = str.toCharArray();
        int n = chars.length;
        for (int i = 0; i < n; i += k) {
            int left = i;
            int right = (k + i - 1) < n ? (k + i - 1) : (n - 1);
            while (left < right) {
                char temp = chars[left];
                chars[left] = chars[right];
                chars[right] = temp;
                left++;
                right--;
            }
        }
        return Arrays.toString(chars);
    }

    /**
     * 反转字符串
     * 先转成char数组
     *
     * @param str
     * @return
     */
    private static String reverseStr(String str) {
        char[] chars = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = chars.length - 1; i >= 0; i--) {
            sb.append(chars[i]);
        }
        return sb.toString();
    }

    /**
     * 双指针反转字符串
     *
     * @param s
     */
    private static String reverseString(char[] s) {
        int n = s.length;
        for (int left = 0, right = n - 1; left < right; left++, right--) {
            char temp = s[left];
            s[left] = s[right];
            s[right] = temp;
        }
        return new String(s);
    }
    //===========================================字符串===========================================

}
