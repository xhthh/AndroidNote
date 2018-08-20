package com.xht.androidnote.module.fragment;

import android.widget.Button;
import android.widget.Toast;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2018/6/14.
 */

public class FragmentThree extends BaseFragment {
    @BindView(R.id.btn_next)
    Button mBtnNext;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_back_stack_three;
    }

    @Override
    protected void initEventAndData() {

    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        Toast.makeText(getActivity(), "I am a btn in Fragment three",
                Toast.LENGTH_SHORT).show();
    }

}
