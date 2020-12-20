package com.xht.androidnote.module.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

/**
 * Created by xht on 2018/6/14.
 * <p>
 * Fragment回退栈
 * 1、Activity与Fragment交互
 *  1）在Fragment中设置回调，在Activity中操作Fragment
 *  2）通过argument传递数据
 *  3）一个Fragment中startActivityForResult()启动一个Activity，在该Activity中getIntent()获取数据，
 *     使用argument传递给Fragment，然后getActivity().setResult(resultCode, intent)
 *
 * 2、重叠问题：由于横竖屏或内存问题，Activity重建，但是onSaveInstance()保存了Fragment的状态
 *      1、在onCreate()中判断savedInstanceState是否为空，来创建操作fragment
 *      2、重写onAttachFragment，重新让新的Fragment指向了原本未被销毁的fragment，它就是onAttach方法对应的Fragment对象
 *      3、onSaveInstanceState()方法中注释掉super()方法
 *      //super.onSaveInstanceState(outState);
 *
 * 3、Fragment之间怎样传递数据
 *  1）在一个Fragment中提供一个方法，在另一个Fragment中通过findFragmentByTag()得到fragment对象，然后调用方法。
 *  getActivity().getSupportFragmentManager().findFragmentByTag("aFragment");
 *  2）采取接口回调的方式进行数据传递。
 *  3）使用三方框架，如RxBus、EventBus
 */

public class FragmentBackStackActivity extends BaseActivity implements FragmentOne
        .FOneBtnClickListener,
        FragmentTwo.FTwoBtnClickListener {

    private FragmentOne mFOne;
    private FragmentTwo mFTwo;
    private FragmentThree mFThree;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_back_stack;
    }

    @Override
    protected void initEventAndData() {

        /*if (mFOne == null) {
            mFOne = new FragmentOne();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_container, mFOne, "one");
        transaction.commit();*/

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mFOne == null) {
            mFOne = FragmentOne.newInstance("My life is getting better.");
        }

        if(savedInstanceState ==  null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fl_container, mFOne, "one");
            transaction.commit();
        }
    }

    @Override
    public void onFOneBtnClick() {
        if (mFTwo == null) {
            mFTwo = new FragmentTwo();
            mFTwo.setfTwoBtnClickListener(this);
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_container, mFTwo, "TWO");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFTwoBtnClick() {
        if (mFThree == null) {
            mFThree = new FragmentThree();

        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.hide(mFTwo);
        tx.add(R.id.fl_container, mFThree, "THREE");
        // tx.replace(R.id.id_content, fThree, "THREE");
        tx.addToBackStack(null);
        tx.commit();
    }
}
