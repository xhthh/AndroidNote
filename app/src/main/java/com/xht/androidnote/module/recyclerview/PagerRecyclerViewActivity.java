package com.xht.androidnote.module.recyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.listview.LvEntity;
import com.xht.androidnote.module.recyclerview.adapter.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2019/4/11.
 */

public class PagerRecyclerViewActivity extends BaseActivity {
    @BindView(R.id.rv_pager)
    RecyclerView mRvPager;
    @BindView(R.id.videoDetailList_goBack)
    ImageView mVideoDetailListGoBack;
    private List<LvEntity> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pager_recyclerview;
    }

    @Override
    protected void initEventAndData() {
        initData();

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRvPager.setLayoutManager(layoutManager);

        pagerSnapHelper.attachToRecyclerView(mRvPager);

        PagerAdapter adapter = new PagerAdapter(mList);

        mRvPager.setAdapter(adapter);
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            LvEntity lvEntity = new LvEntity();
            lvEntity.title = i + "";
            if(i%2==0) {
                lvEntity.imgId = R.mipmap.taobao_launch;
            } else {
                lvEntity.imgId = R.mipmap.splash_log;
            }
            mList.add(lvEntity);
        }
    }


    @OnClick(R.id.videoDetailList_goBack)
    public void onViewClicked() {
        finish();
    }
}
