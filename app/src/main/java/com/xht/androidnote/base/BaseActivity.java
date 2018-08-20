package com.xht.androidnote.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xht on 2018/4/23.
 */

public abstract class BaseActivity extends FragmentActivity {

    protected Activity mContext;
    private Unbinder mBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBaseConfig();

        setContentView(getLayoutId());

        mBind = ButterKnife.bind(this);

        mContext = this;

        initEventAndData();
    }

    protected abstract int getLayoutId();

    protected abstract void initEventAndData();

    private void setBaseConfig() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    protected void skip2Activity(Class clazz) {
        if (mContext != null)
            mContext.startActivity(new Intent(mContext, clazz));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mBind != null) {
            mBind.unbind();
        }
    }
}
