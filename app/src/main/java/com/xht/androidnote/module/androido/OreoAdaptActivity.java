package com.xht.androidnote.module.androido;

import android.view.View;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by xht on 2019/1/28.
 */

public class OreoAdaptActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_oreo;
    }

    @Override
    protected void initEventAndData() {

    }

    @OnClick({R.id.btn_oreo_notification, R.id.btn_oreo_permission, R.id.btn_oreo_service,
            R.id.btn_oreo_broadcast})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_oreo_notification:
                break;
            case R.id.btn_oreo_permission:
                break;
            case R.id.btn_oreo_service:
                break;
            case R.id.btn_oreo_broadcast:
                break;
        }
    }
}
