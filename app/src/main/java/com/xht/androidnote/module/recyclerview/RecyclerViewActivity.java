package com.xht.androidnote.module.recyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.listview.LvEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xht on 2019/4/2.
 */

public class RecyclerViewActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    private List<LvEntity> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recyclerview;
    }

    @Override
    protected void initEventAndData() {
        for (int i = 0; i < 20; i++) {
            LvEntity lvEntity = new LvEntity();
            lvEntity.title = i + "";
            mList.add(lvEntity);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(RecyclerViewActivity.this);
        mRecyclerview.setLayoutManager(layoutManager);
        RvAdapter adapter = new RvAdapter(RecyclerViewActivity.this, mList);
        mRecyclerview.setAdapter(adapter);
    }

}
