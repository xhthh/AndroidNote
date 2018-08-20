package com.xht.androidnote.module.fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2018/6/14.
 */

public class FragmentTwo extends BaseFragment {

    @BindView(R.id.btn_next)
    Button mBtnNext;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_back_stack_two;
    }

    @Override
    protected void initEventAndData() {

    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        //test1();
        test2();
    }

    private void test2() {
        if (fTwoBtnClickListener != null) {
            fTwoBtnClickListener.onFTwoBtnClick();
        }
    }

    private void test1() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //先隐藏了当前的Fragment，然后添加了FragmentThree的实例，最后将事务添加到回退栈。
        transaction.hide(this);

        transaction.add(R.id.fl_container, new FragmentThree(), "three");

        transaction.addToBackStack(null);

        transaction.commit();
    }


    private FTwoBtnClickListener fTwoBtnClickListener;

    public interface FTwoBtnClickListener {
        void onFTwoBtnClick();
    }

    //设置回调接口
    public void setfTwoBtnClickListener(FTwoBtnClickListener fTwoBtnClickListener) {
        this.fTwoBtnClickListener = fTwoBtnClickListener;
    }
}
