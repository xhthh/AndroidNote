package com.xht.androidnote.module.proxy;

import android.util.Log;

/**
 * Created by xht on 2019/8/22.
 */
public class AndroidDeveloper implements Developer {
    private String name;

    public AndroidDeveloper(String name) {
        this.name = name;
    }

    @Override
    public void code() {
        Log.i("xht","写Android代码");
    }

    @Override
    public void debug() {
        Log.i("xht","debug调试");
    }
}
