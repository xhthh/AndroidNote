package com.xht.androidnote.module.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xht on 2018/6/15.
 * 和ViewPager一起使用
 */

public class FragmentViewPagerActivity extends BaseActivity {
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private List<Fragment> fragmentList = new ArrayList<>();

    private String[] titles = {"NBA", "步行街", "影视区", "爆照区"};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_viewpager;
    }

    @Override
    protected void initEventAndData() {

        for (String title : titles) {
            fragmentList.add(ContentFragment.newInstance(title));
        }

        //Fragment中嵌套使用Fragment一定要使用getChildFragmentManager(),否则会有问题？
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),
                fragmentList, titles);

        mViewPager.setAdapter(pagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

    }
}
