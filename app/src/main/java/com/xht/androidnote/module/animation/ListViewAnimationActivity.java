package com.xht.androidnote.module.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by xht on 2018/7/12.
 */

public class ListViewAnimationActivity extends BaseActivity {
    @BindView(R.id.lv_animation)
    ListView mListView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_animation_listview;
    }

    @Override
    protected void initEventAndData() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_item);
        LayoutAnimationController controller = new LayoutAnimationController
                (animation);
        controller.setDelay(0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        mListView.setLayoutAnimation(controller);

        ArrayList<String> datas = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            datas.add("name " + i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.item_list_content, R.id.name, datas);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(mContext, "click item", Toast.LENGTH_SHORT).show();

            }
        });
    }


}
