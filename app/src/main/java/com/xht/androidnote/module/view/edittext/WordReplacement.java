package com.xht.androidnote.module.view.edittext;

import android.text.method.ReplacementTransformationMethod;

public class WordReplacement extends ReplacementTransformationMethod {

    String strWord = null;

    @Override
    protected char[] getOriginal() {
        //循环ASCII值 字符串形式累加到String
        for (char i = 0; i < 256; i++) {
            strWord += String.valueOf(i);
        }
        //strWord转换为字符形式的数组
        char[] charOriginal = strWord.toCharArray();
        return charOriginal;
    }

    @Override
    protected char[] getReplacement() {
        char[] charReplacement = new char[255];
        //输入的字符在ASCII范围内，将其转换为*
        for (int i = 0; i < 255; i++) {
            charReplacement[i] = '*';
        }

        return charReplacement;
    }

}
