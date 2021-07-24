package com.xht.androidnote.module.kotlin.multiSelect.lib;

/**
 * Created by Mr_Zeng
 *
 * @date 2019/2/16
 */
public interface Interceptor {
    void intercept(Chain chain);

    interface Chain{
        void proceed(Stream stream);
        Stream stream();
    }
}
