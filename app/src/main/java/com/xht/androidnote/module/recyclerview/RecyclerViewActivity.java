package com.xht.androidnote.module.recyclerview;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import java.util.ArrayList;
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

                if(firstCompletelyVisibleItemPosition == 0) {
                    Log.i("xht","滑动到顶部。。。。");
                }

                int lastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(lastCompletelyVisibleItemPosition == linearLayoutManager.getItemCount() -1) {
                    Log.i("xht","滑动到底部。。。。");
                }
            }
        });

    }
}
