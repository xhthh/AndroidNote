package com.xht.androidnote.module.dsa.arithmetic.divide;

public class Hanoi {


    public static void main(String[] args) {

    }


    public static void hanoi(int n, String sourceTower, String tempTower, String targetTower) {
        if (n == 1) {
            //如果只有一个盘子1，那么直接将其从sourceTower移动到targetTower
            move(n,sourceTower,targetTower);
        } else {

        }
    }

    private static void move(int n, String sourceTower, String targetTower) {
        System.out.println("第" + n + "号盘子 move：" + sourceTower+ "--->" + targetTower);
    }


}
