package com.xht.androidnote.module.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2018/6/14.
 */

public class FragmentOne extends BaseFragment {
    private static final String ARGUMENT = "argument";
    @BindView(R.id.tv_msg)
    TextView mTvMsg;

    private String mArgument;

    public static FragmentOne newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        FragmentOne fragmentOne = new FragmentOne();
        fragmentOne.setArguments(bundle);
        return fragmentOne;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mArgument = bundle.getString(ARGUMENT);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_back_stack_one;
    }

    @Override
    protected void initEventAndData() {

        // 设置数据
        mTvMsg.setText(mArgument);

    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {

        // test1();
        test2();

    }

    private void test2() {
        if (getActivity() instanceof FOneBtnClickListener) {
            ((FOneBtnClickListener) getActivity()).onFOneBtnClick();
        }
    }

    private void test1() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //replace方法，相当于remove和add的合体，并且如果不添加事务到回退栈，前一个Fragment实例会被销毁
        //FragmentOne实例不会被销毁，但是视图层次依然会被销毁，即会调用onDestoryView和onCreateView。
        //当之后我们从FragmentTwo返回到前一个页面的时候，视图层仍旧是重新按照代码绘制，这里仅仅是是实例没有销毁
        transaction.replace(R.id.fl_container, new FragmentTwo(), "two");

        //将当前的事务添加到了回退栈
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public interface FOneBtnClickListener {
        void onFOneBtnClick();
    }
}
