package com.xht.androidnote.module.fragment;

import android.util.Log;
import android.widget.TextView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by xht on 2018/6/14.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.tv_fragment_content_title)
    TextView mTvContent;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_content;
    }

    @Override
    protected void initEventAndData() {
        mTvContent.setText("首页");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i("xht", "HomeFragment---onHiddenChanged()---hidden = " + hidden);
    }
}
