package com.xht.androidnote.module.ipc;

import android.view.View;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by xht on 2018/6/29.
 */

public class BinderActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_binder;
    }

    @Override
    protected void initEventAndData() {

    }

    @OnClick({R.id.btn_binder_bind, R.id.btn_binder_unbind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_binder_bind:
                break;
            case R.id.btn_binder_unbind:
                break;
        }
    }
}
