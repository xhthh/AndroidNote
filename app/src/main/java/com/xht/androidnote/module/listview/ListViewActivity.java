package com.xht.androidnote.module.listview;

import android.content.res.Configuration;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ListView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xht on 2019/4/2.
 */

public class ListViewActivity extends BaseActivity {
    @BindView(R.id.listview)
    ListView mListview;

    private List<LvEntity> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_listview;
    }

    @Override
    protected void initEventAndData() {

        for (int i = 0; i < 20; i++) {
            LvEntity lvEntity = new LvEntity();
            lvEntity.title = i + "";
            mList.add(lvEntity);
        }

        MyAdapter adapter = new MyAdapter(ListViewActivity.this, mList);
        mListview.setAdapter(adapter);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("xht","ListViewActivity----onConfigurationChanged()");
    }
}
