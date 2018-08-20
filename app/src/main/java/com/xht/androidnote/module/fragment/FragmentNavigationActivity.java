package com.xht.androidnote.module.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2018/6/14.
 * 1、replace方式
 * 如果使用这种方式，是可以避免重叠的问题，但是每次replace会把生命周期全部执行一遍，如果在这些生命周期函数里拉取数据的话，
 * 就会不断重复的加载刷新数据，所以我们并不推荐使用这种方式。
 * <p>
 * 2、add、hide、show的方式
 * 虽然这种方式避免了可能重复加载刷新数据的问题，但是会出现重叠的问题。
 * <p>
 * 重叠原因：
 * 当系统内存不足，Fragment 的宿主 Activity 回收的时候，Fragment 的实例并没有随之被回收。Activity 被系统回收时，
 * 会主动调用 onSaveInstance() 方法来保存视图层（View Hierarchy），所以当 Activity 通过导航再次被重建时，
 * 之前被实例化过的 Fragment 依然会出现在 Activity 中，此时的 FragmentTransaction 中的相当于又再次 add 了 fragment 进去的，
 * hide()和show()方法对之前保存的fragment已经失效了，所以就出现了重叠。
 */

public class FragmentNavigationActivity extends BaseActivity {

    @BindView(R.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R.id.iv_tab_home)
    ImageView mIvTabHome;
    @BindView(R.id.tv_tab_home)
    TextView mTvTabHome;
    @BindView(R.id.ll_tab_home)
    LinearLayout mLlTabHome;
    @BindView(R.id.iv_tab_classify)
    ImageView mIvTabClassify;
    @BindView(R.id.tv_tab_classify)
    TextView mTvTabClassify;
    @BindView(R.id.ll_tab_classify)
    LinearLayout mLlTabClassify;
    @BindView(R.id.iv_tab_shop_cart)
    ImageView mIvTabShopCart;
    @BindView(R.id.tv_tab_shop_cart)
    TextView mTvTabShopCart;
    @BindView(R.id.ll_tab_shop_cart)
    LinearLayout mLlTabShopCart;
    @BindView(R.id.iv_tab_mine)
    ImageView mIvTabMine;
    @BindView(R.id.tv_tab_mine)
    TextView mTvTabMine;
    @BindView(R.id.ll_tab_mine)
    LinearLayout mLlTabMine;

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_CLASSIFY = 1;
    private static final int FRAGMENT_SHOPCART = 2;
    private static final int FRAGMENT_MINE = 3;

    private HomeFragment mHomeFragment;
    private ClassifyFragment mClassifyFragment;
    private ShopCartFragment mShopCartFragment;
    private MineFragment mMineFragment;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_navigation;
    }

    @Override
    protected void initEventAndData() {
        showFragment(FRAGMENT_HOME);
    }

    @OnClick({R.id.ll_tab_home, R.id.ll_tab_classify, R.id.ll_tab_shop_cart, R.id.ll_tab_mine})
    public void onViewClicked(View view) {
        resetUI();
        switch (view.getId()) {
            case R.id.ll_tab_home:
                showFragment(FRAGMENT_HOME);
                break;
            case R.id.ll_tab_classify:
                showFragment(FRAGMENT_CLASSIFY);
                break;
            case R.id.ll_tab_shop_cart:
                showFragment(FRAGMENT_SHOPCART);
                break;
            case R.id.ll_tab_mine:
                showFragment(FRAGMENT_MINE);
                break;
        }
    }

    /**
     * 重写onAttachFragment，重新让新的Fragment指向了原本未被销毁的fragment，它就是onAttach方法对应的Fragment对象
     *
     * @param fragment
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (mHomeFragment == null && fragment instanceof HomeFragment) {
            mHomeFragment = (HomeFragment) fragment;
        } else if (mClassifyFragment == null && fragment instanceof ClassifyFragment) {
            mClassifyFragment = (ClassifyFragment) fragment;
        } else if (mShopCartFragment == null && fragment instanceof ShopCartFragment) {
            mShopCartFragment = (ShopCartFragment) fragment;
        } else if (mMineFragment == null && fragment instanceof MineFragment) {
            mMineFragment = (MineFragment) fragment;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // AndroidManifest中设置了configChanges，屏幕旋转不会销毁Activity
        /*
         *  如果不设置configChanges的话，每次旋转都会销毁，回到第一个,
         */
    }

    private void showFragment(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case FRAGMENT_HOME:
                /**
                 * 如果Fragment为空，就新建一个实例
                 * 如果不为空，就将它从栈中显示出来
                 */
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    transaction.add(R.id.fl_content, mHomeFragment);
                } else {
                    transaction.show(mHomeFragment);
                }
                mIvTabHome.setImageResource(R.mipmap.h_tab_selected);
                mTvTabHome.setTextColor(getResources().getColor(R.color.orange));
                break;

            case FRAGMENT_CLASSIFY:
                if (mClassifyFragment == null) {
                    mClassifyFragment = new ClassifyFragment();
                    transaction.add(R.id.fl_content, mClassifyFragment, ClassifyFragment.class.getName());
                } else {
                    transaction.show(mClassifyFragment);
                }
                mIvTabClassify.setImageResource(R.mipmap.c_tab_selected);
                mTvTabClassify.setTextColor(getResources().getColor(R.color.orange));
                break;

            case FRAGMENT_SHOPCART:
                if (mShopCartFragment == null) {
                    mShopCartFragment = new ShopCartFragment();
                    transaction.add(R.id.fl_content, mShopCartFragment);
                } else {
                    transaction.show(mShopCartFragment);
                }
                mIvTabShopCart.setImageResource(R.mipmap.s_tab_selected);
                mTvTabShopCart.setTextColor(getResources().getColor(R.color.orange));
                break;

            case FRAGMENT_MINE:
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    transaction.add(R.id.fl_content, mMineFragment);
                } else {
                    transaction.show(mMineFragment);
                }
                mIvTabMine.setImageResource(R.mipmap.m_tab_selected);
                mTvTabMine.setTextColor(getResources().getColor(R.color.orange));
                break;
        }

        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        // 如果不为空，就先隐藏起来
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mClassifyFragment != null) {
            transaction.hide(mClassifyFragment);
        }
        if (mShopCartFragment != null) {
            transaction.hide(mShopCartFragment);
        }
        if (mMineFragment != null) {
            transaction.hide(mMineFragment);
        }
    }

    /**
     * 恢复Tab图标和文字颜色
     */
    public void resetUI() {
        mIvTabHome.setImageResource(R.mipmap.h_tab_normal);
        mIvTabClassify.setImageResource(R.mipmap.c_tab_normal);
        mIvTabShopCart.setImageResource(R.mipmap.s_tab_normal);
        mIvTabMine.setImageResource(R.mipmap.m_tab_normal);

        mTvTabHome.setTextColor(getResources().getColor(R.color.black));
        mTvTabClassify.setTextColor(getResources().getColor(R.color.black));
        mTvTabShopCart.setTextColor(getResources().getColor(R.color.black));
        mTvTabMine.setTextColor(getResources().getColor(R.color.black));
    }

}
