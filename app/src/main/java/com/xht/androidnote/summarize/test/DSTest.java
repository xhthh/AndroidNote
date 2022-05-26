package com.xht.androidnote.summarize.test;

import java.util.ArrayList;

public class DSTest {
    public static void main(String[] args) {
        arrayListTest();
    }

    private static void arrayListTest() {
        ArrayList<Integer> arrayList = new ArrayList<>(10);
        System.out.println(arrayList.size());
//        arrayList.set(5, 1);

        arrayList.add(1);
        int size = arrayList.size();
        System.out.println("======arrayList.size()=" + size);
    }


}
