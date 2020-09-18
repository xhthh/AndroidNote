package com.xht.androidnote.module.constraint;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConstraintLayoutTestActivity extends BaseActivity {
    @BindView(R.id.btn_target)
    Button btnTarget;
    @BindView(R.id.btn_test)
    Button btnTest;

    @Override
    protected int getLayoutId() {
        return R.layout.constraint_layout_activity;
    }

    @Override
    protected void initEventAndData() {
        btnTarget.setVisibility(View.GONE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_test)
    public void onViewClicked() {
    }
}
