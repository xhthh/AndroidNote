package com.xht.androidnote.module.dsa.manhuasuanfa.question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xht on 2020/1/14.
 */
public class Test12 {


    private static List<Integer> divideRedPackage(Integer totalAmount, Integer totalPeopleNum) {

        List<Integer> amountList = new ArrayList<>();

        Integer restAmount = totalAmount;
        Integer restPeopleNum = totalPeopleNum;

        Random random = new Random();

        for (int i = 0; i < totalPeopleNum - 1; i++) {
            //nextInt(bound)    [0,bound)   +1 即 [1,bound)
            int amount = random.nextInt(restAmount / restPeopleNum * 2 - 1) + 1;
            restAmount -= amount;
            restPeopleNum--;
            amountList.add(amount);
        }

        amountList.add(restAmount);

        return amountList;
    }


    public static void main(String[] args) {

        List<Integer> amountList = divideRedPackage(1000, 10);
        for (Integer amount : amountList) {
            //System.out.println(" 抢到金额：" + new BigDecimal(amount).divide(new BigDecimal(100)));
            System.out.println("抢到金额：" + amount);
        }

    }


}
