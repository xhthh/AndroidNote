package com.xht.androidnote.module.customview;

import android.view.View;
import android.widget.Button;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.bean.ImgList;
import com.xht.androidnote.module.customview.widget.MultiImageLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xht on 2019/4/4.
 * 自定义view
 */

public class CustomViewActivity extends BaseActivity {


    private List<ImgList> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_customview;
    }

    @Override
    protected void initEventAndData() {
        MultiImageLayout multi_image = findViewById(R.id.multi_image);

        Button btnLoad = findViewById(R.id.btn_load);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multi_image.setPhotos(mList);
            }
        });

        initData(4);

    }

    private void initData(int count) {
        String url = "https://img2.autoimg.cn/hscdfs/g2/M03/4E/37/ChsEkFy-sJWAbs9nAAD8Cq9tAjA113.jpg";

        ImgList img = new ImgList();
        img.img = url;
        img.imgwidth = 800;
        img.imgheight = 300;

        for (int i = 0; i < count; i++) {
            mList.add(img);
        }
    }

}
