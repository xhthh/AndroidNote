package com.xht.androidnote.module.rxjava;

import android.util.Log;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by xht on 2018/6/6.
 */

public class RxJavaActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_rxjava;
    }

    @Override
    protected void initEventAndData() {
        test1();
    }

    private void test1() {
        Flowable.just("Hello World").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i("xht","s===" + s);
            }
        });
    }
}
