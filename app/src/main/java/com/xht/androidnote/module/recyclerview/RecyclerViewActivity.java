package com.xht.androidnote.module.recyclerview;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.RecyclerView.RecycledViewPool;
import android.support.v7.widget.RecyclerView.ViewCacheExtension;
import android.util.Log;
import android.widget.LinearLayout;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xht on 2019/8/15.
 */
public class RecyclerViewActivity extends BaseActivity {
    @BindView(R.id.rv_test)
    RecyclerView rvTest;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recyclerview;
    }

    @Override
    protected void initEventAndData() {

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i + "");
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        RvTestAdapter adapter = new RvTestAdapter(list, this);

        rvTest.setLayoutManager(layoutManager);
        rvTest.setAdapter(adapter);


        //        rvTest.addOnScrollListener(new RecyclerView.OnScrollListener() {
        //            @Override
        //            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        //                super.onScrolled(recyclerView, dx, dy);
        //                if (rvTest.canScrollVertically(1)) {
        //
        //                } else {
        //                    //滑动到底部
        //                    Log.i("xht", "滑动到底部");
        //                }
        //
        //                if (rvTest.canScrollVertically(-1)) {
        //
        //                } else {
        //                    Log.i("xht", "滑动到顶部");
        //                }
        //            }
        //        });


        rvTest.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();

                if (firstCompletelyVisibleItemPosition == 0) {
                    Log.i("xht", "滑动到顶部。。。。");
                }

                int lastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastCompletelyVisibleItemPosition == linearLayoutManager.getItemCount() - 1) {
                    Log.i("xht", "滑动到底部。。。。");
                }
            }
        });

    }

    public final class Recycler {
        //一级缓存
        //存储的是当前还在屏幕中的 ViewHolder；按照 id 和 position 来查找 ViewHolder
        final ArrayList<ViewHolder> mAttachedScrap = new ArrayList<>();

        //表示数据已经改变的 ViewHolder 列表, 存储 notifyXXX 方法时需要改变的 ViewHolder
        ArrayList<ViewHolder> mChangedScrap = null;

        //二级缓存
        //用来缓存移除屏幕之外的 ViewHolder，默认情况下缓存容量是 2，可以通过 setViewCacheSize 方法来改变缓存的容量大小。
        //如果 mCachedViews 的容量已满，则会根据 FIFO 的规则移除旧 ViewHolder
        final ArrayList<ViewHolder> mCachedViews = new ArrayList<ViewHolder>();

        //三级缓存
        //开发给用户的自定义扩展缓存，需要用户自己管理 View 的创建和缓存。
        private ViewCacheExtension mViewCacheExtension;

        //四级缓存
        /*
            ViewHolder 缓存池，在有限的 mCachedViews 中如果存不下新的 ViewHolder 时，就会把 ViewHolder 存入 RecyclerViewPool 中。
            按照 Type 来查找 ViewHolder
            每个 Type 默认最多缓存 5 个
            可以多个 RecyclerView 共享 RecycledViewPool
         */
        RecycledViewPool mRecyclerPool;

        static final int DEFAULT_CACHE_SIZE = 2;
    }
}
