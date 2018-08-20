package com.xht.androidnote.module;

import android.content.Intent;
import android.webkit.WebView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by xht on 2018/6/7.
 */

public class ContentReviewActivity extends BaseActivity {

    @BindView(R.id.wv_content_review)
    WebView mWvContentReview;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_content_review;
    }

    @Override
    protected void initEventAndData() {

        Intent intent = getIntent();

        String content = intent.getStringExtra("content");

        mWvContentReview.loadUrl(content);

    }
}
