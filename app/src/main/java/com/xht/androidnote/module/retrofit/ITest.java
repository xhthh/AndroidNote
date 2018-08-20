package com.xht.androidnote.module.retrofit;

import retrofit2.http.GET;

/**
 * Created by xht on 2018/5/21.
 */

public interface ITest {
    @GET("/heiheihei")
    void add(int a, int b);
}
