package com.xht.androidnote.module.fragment;

import android.view.View;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by xht on 2018/6/13.
 */

public class FragmentTestActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_test;
    }

    @Override
    protected void initEventAndData() {

    }

    @OnClick({R.id.btn_fragment_navigation, R.id.btn_fragment_back_stack, R.id
            .btn_fragment_viewpager,R.id.btn_fragment_life_cycle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_fragment_life_cycle:
                skip2Activity(TestLifeCycleActivity.class);
                break;
            case R.id.btn_fragment_navigation:
                skip2Activity(FragmentNavigationActivity.class);
                break;
            case R.id.btn_fragment_viewpager:
                skip2Activity(FragmentViewPagerActivity.class);
                break;
            case R.id.btn_fragment_back_stack:
                skip2Activity(FragmentBackStackActivity.class);
                break;
        }
    }
}
