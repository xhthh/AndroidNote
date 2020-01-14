package com.xht.androidnote.module.dsa.manhuasuanfa.question;

/**
 * Created by xht on 2020/1/13.
 * 金矿问题
 */
public class Test9 {

    /**
     * @param w 工人数量
     * @param n 可选金矿数量
     * @param p 金矿开采所需的工人数量
     * @param g 金矿储量
     * @return 最后一个一定挖 or 最后一个一定不挖
     */
    public static int getBestGoldMining(int w, int n, int[] p, int[] g) {
        if (w == 0 || n == 0) {
            return 0;
        }

        //当所剩工人不够挖掘当前金矿时，只有一种最优子结构。
        if (w < p[n - 1]) {
            return getBestGoldMining(w, n - 1, p, g);
        }

        //在常规情况下，具有两种最优子结构（挖当前金矿或不挖当前金矿）
        return Math.max(getBestGoldMining(w, n - 1, p, g),
                getBestGoldMining(w - p[n - 1], n - 1, p, g) + g[n - 1]);
    }


    /**
     * @param w 工人数量
     * @param p 金矿开采所需的工人数量
     * @param g 金矿储量
     * @return
     */
    public static int getBestGoldMiningV2(int w, int[] p, int[] g) {
        //创建表格
        int[][] resultTable = new int[g.length + 1][w + 1];

        //填充表格
        for (int i = 1; i <= g.length; i++) {
            for (int j = 1; j <= w; j++) {
                if (j < p[i - 1]) {//当前工人数量小于当前金矿开采所需的工人数量
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
     * @param w 工人数量
     * @param p 金矿开采所需的工人数量
     * @param g 金矿储量
     * @return
     */
    public static int getBestGoldMiningV3(int w, int[] p, int[] g) {
        //创建当前结果
        int[] results = new int[w + 1];

        //填充一维数组
        for (int i = 1; i <= g.length; i++) {
            for (int j = w; j >= 1; j--) {
                if (j >= p[i - 1]) {
                    results[j] = Math.max(results[j], results[j - p[i - 1]] + g[i - 1]);
                }
            }
        }

        return results[w];
    }


    public static void main(String[] args) {
        int w = 10;
        int[] p = {5, 5, 3, 4, 3};
        int[] g = {400, 500, 200, 300, 350};


        //System.out.println(" 最优收益：" + getBestGoldMining(w, g.length, p, g));
        System.out.println(" 最优收益：" + getBestGoldMiningV2(w, p, g));
    }

}
