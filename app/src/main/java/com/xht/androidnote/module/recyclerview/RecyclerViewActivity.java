package com.xht.androidnote.module.recyclerview;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.listview.LvEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2019/4/2.
 */

public class RecyclerViewActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.btn_reset)
    Button mBtnReset;
    @BindView(R.id.btn_update0)
    Button mBtnUpdate0;
    @BindView(R.id.btn_update1)
    Button mBtnUpdate1;
    @BindView(R.id.btn_update2)
    Button mBtnUpdate2;
    @BindView(R.id.btn_update3)
    Button mBtnUpdate3;
    @BindView(R.id.btn_update4)
    Button mBtnUpdate4;
    @BindView(R.id.btn_update5)
    Button mBtnUpdate5;
    @BindView(R.id.btn_update6)
    Button mBtnUpdate6;
    @BindView(R.id.btn_diff)
    Button mBtnDiff;
    @BindView(R.id.ll_button)
    LinearLayout mLlButton;
    private List<LvEntity> mList = new ArrayList<>();
    private RvAdapter mAdapter;
    private int lastPosition;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recyclerview;
    }

    @Override
    protected void initEventAndData() {
        for (int i = 0; i < 5; i++) {
            LvEntity lvEntity = new LvEntity();
            lvEntity.title = i + "";
            lvEntity.imgId = R.mipmap.banner;
            mList.add(lvEntity);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(RecyclerViewActivity.this);
        mRecyclerview.setLayoutManager(layoutManager);
        mAdapter = new RvAdapter(RecyclerViewActivity.this, mList);
        mRecyclerview.setAdapter(mAdapter);

        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                //得到当前显示的最后一个item的view
                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);

                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部
                if (lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    //滑到了最底部
                }
            }
        });
    }

    @OnClick({R.id.btn_reset, R.id.btn_update0, R.id.btn_update1, R.id.btn_update2, R.id.btn_update3,
            R.id.btn_update4, R.id.btn_update5, R.id.btn_update6, R.id.btn_diff})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_diff:
                diff();
                break;
            case R.id.btn_reset:
                reset();
                break;
            case R.id.btn_update0:
                update0();
                break;
            case R.id.btn_update1:
                update1();
                break;
            case R.id.btn_update2:
                update2();
                break;
            case R.id.btn_update3:
                update3();
                break;
            case R.id.btn_update4:
                update4();
                break;
            case R.id.btn_update5:
                update5();
                break;
            case R.id.btn_update6:
                update6();
                break;
        }
    }

    private void diff() {
        List<LvEntity> newList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            LvEntity lvEntity = new LvEntity();
            lvEntity.title = i + "";
            if (i == 2) {
                lvEntity.imgId = R.mipmap.banner2;
            } else {
                lvEntity.imgId = R.mipmap.banner;
            }
            newList.add(lvEntity);
        }

        //mAdapter.notifyDataSetChanged();

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new AdapterDiffCallback(mList, newList), true);
        diffResult.dispatchUpdatesTo(mAdapter);

        mList = newList;
        mAdapter.setDatas(mList);
    }

    /**
     * 从某一位置删除count个item
     */
    private void update6() {
        mList.remove(0);
        mList.remove(1);
        mList.remove(2);
        mAdapter.notifyItemRangeRemoved(0, 3);
    }

    /**
     * 从某一位置增加count个item
     */
    private void update5() {
        for (int i = 0; i < 3; i++) {
            LvEntity lvEntity = new LvEntity();
            lvEntity.title = "我是新来的 " + i + "！";
            lvEntity.imgId = R.mipmap.banner2;
            mList.add(lvEntity);
        }
        mAdapter.notifyItemRangeInserted(5, 3);
    }

    /**
     * 从某一位置刷新count个item
     */
    private void update4() {
        mList.get(3).imgId = R.mipmap.banner2;
        mList.get(4).imgId = R.mipmap.banner2;
        mAdapter.notifyItemRangeChanged(3, 2);
    }

    /**
     * 移动某一item到指定位置
     */
    private void update3() {
        mAdapter.notifyItemMoved(0, 4);
    }

    /**
     * 删除某一位置的item
     */
    private void update2() {
        mList.remove(3);
        mAdapter.notifyItemRemoved(3);
    }

    /**
     * 指定位置增加一个item
     */
    private void update1() {
        LvEntity lvEntity = new LvEntity();
        lvEntity.title = "我是新来的！";
        lvEntity.imgId = R.mipmap.banner2;

        mList.add(2, lvEntity);

        mAdapter.notifyItemInserted(2);
    }

    /**
     * 更新某一位置的item
     */
    private void update0() {
        mList.get(1).imgId = R.mipmap.banner2;
        mAdapter.notifyItemChanged(1);
    }

    /**
     * 重置数据，更新全部
     */
    private void reset() {
        mList.clear();
        for (int i = 0; i < 5; i++) {
            LvEntity lvEntity = new LvEntity();
            lvEntity.title = i + "";
            lvEntity.imgId = R.mipmap.banner;
            mList.add(lvEntity);
        }
        mAdapter.notifyDataSetChanged();
    }
}
