package com.xht.androidnote.summarize.test;

/**
 * 动态规划，一种分阶段求解的思路
 * 三个概念：
 * 最优子结构
 * 边界
 * 状态转移公式
 */
public class DPTest {
    public static void main(String[] args) {
        System.out.println(getClimbingWays(5));

        testGold();
    }

    /**
     * 有一座高度是10级台阶的楼梯，从下往上走，每跨一步只能向上1级或者2级台阶。要求用程序来求出一共有多少种走法。
     * 1    1
     * 2    2
     * 3    3
     * 4    5
     * 5    8
     *
     * @param n
     * @return
     */
    private static int getClimbingWays(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        int a = 1;
        int b = 2;
        int temp = 0;
        for (int i = 3; i <= n; i++) {
            temp = a + b;
            a = b;
            b = temp;
        }
        return temp;
    }

    private static void testGold() {
        int[] g = {400, 500, 200, 300, 350};
        int[] p = {5, 5, 3, 4, 3};

        int bestGoldMiningV3 = getBestGoldMiningV3(10, p, g);
        System.out.println(bestGoldMiningV3);
    }


    /**
     * 获得金矿最优收益
     * <p>
     * 10个工人
     * 400kg/5人
     * 500kg/5人
     * 200kg/3人
     * 300kg/4人
     * 350kg/3人
     * <p>
     * 1、当金矿数为 0，或工人数为 0
     * F(n,w) = 0 (n=0或w=0)
     * 2、当剩下的工人不够挖当前金矿时，只有一种解
     * F(n,w) = F(n-1,w) (n≥1, w<p[n-1])
     * 3、常规情况下，两种解，以 5 个金矿为例
     * 3.1 10个人挖5个金矿
     * 3.2 7个人挖前4个金矿的数量 + 剩下一个金矿的数量
     * F(n,w) = max(F(n-1,w), F(n-1,w-p[n-1])+g[n-1]) (n≥1, w≥p[n-1])
     *
     * @param w 工人数量
     * @param p 金矿开采所需的工人数量
     * @param g 金矿储量
     */
    public static int getBestGoldMiningV3(int w, int[] p, int[] g) {
        //创建当前结果
        int[] results = new int[w + 1];
        //填充一维数组，外层金矿，内层人数
        for (int i = 1; i <= g.length; i++) {
            for (int j = w; j >= 1; j--) {
                if (j >= p[i - 1]) {
                    results[j] = Math.max(results[j],
                            results[j - p[i - 1]] + g[i - 1]);
                }
            }
        }
        //返回最后1个格子的值
        return results[w];
    }

    /**
     * 递归
     * F(n,w) = 0 (n=0或w=0)
     * F(n,w) = F(n-1,w) (n≥1, w<p[n-1])
     * F(n,w) = max(F(n-1,w), F(n-1,w-p[n-1])+g[n-1]) (n≥1, w≥p[n-1])
     * 二叉树
     * 时间复杂度O(2^n)
     * 做了许多重复的计算
     *
     * @param w
     * @param n
     * @param p
     * @param g
     * @return
     */
    public static int getBestGoldMining(int w, int n, int[] p, int[] g) {
        if (w == 0 || n == 0) {
            return 0;
        }
        if (w < p[n - 1]) {
            return getBestGoldMining(w, n - 1, p, g);
        }
        return Math.max(getBestGoldMining(w, n - 1, p, g),
                getBestGoldMining(w - p[n - 1], n - 1, p, g) + g[n - 1]);
    }

    /**
     * F(n,w) = max(F(n-1,w), F(n-1,w-p[n-1])+g[n-1]) (n>1, w≥p[n-1]);
     * @param w
     * @param p
     * @param g
     * @return
     */
    private static int getBestGoldMiningV2(int w, int[] p, int[] g) {
        //创建表格
        int[][] resultTable = new int[g.length + 1][w + 1];
        //填充表格
        for (int i = 1; i <= g.length; i++) {
            for (int j = 1; j <= w; j++) {
                if (j < p[i - 1]) {
                    resultTable[i][j] = resultTable[i - 1][j];
                } else {
                    resultTable[i][j] = Math.max(resultTable[i - 1][j],
                            resultTable[i - 1][j - p[i - 1]] + g[i - 1]);
                }
            }
        }
        //返回最后一个格子的值
        return resultTable[g.length][w];
    }


    /**
     * 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
     * 子数组 是数组中的一个连续部分。
     * 假设sum<=0，那么后面的子序列肯定不包含目前的子序列，所以令sum = num；
     * 如果sum > 0对于后面的子序列是有好处的。res = Math.max(res, sum)保证可以找到最大的子序和。
     *
     * @param nums
     * @return
     */
    private static int maxSubArray(int[] nums) {
        int res = nums[0];
        int sum = 0;
        for (int num : nums) {
            if (sum > 0) {
                sum += num;
            } else {
                sum = num;
            }
            res = Math.max(res, sum);
        }
        return res;
    }
}
