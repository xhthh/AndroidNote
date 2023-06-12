package com.xht.androidnote.module.generic.java;

public class Article extends Entity{
    @Override
    public void parseJson(String content) {
        super.parseJson(content);
        System.out.println(content + "：Json → Article");
    }
}
