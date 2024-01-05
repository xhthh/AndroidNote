package com.xht.androidnote.module;

import android.content.Intent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

        WebSettings settings = mWvContentReview.getSettings();
        settings.setJavaScriptEnabled(true);
        //settings.setSupportZoom(true);

        //设置可以支持缩放        
        settings.setSupportZoom(true);
        //设置true,才能让Webivew支持<meta>标签的viewport属性
        settings.setUseWideViewPort(true);
        //设置出现缩放工具        
        settings.setBuiltInZoomControls(true);
        //设置隐藏缩放控件        
        settings.setDisplayZoomControls(false);
        //最小缩放等级        
        mWvContentReview.setInitialScale(60);


        settings.setBuiltInZoomControls(true);
        //mWvContentReview.setWebChromeClient(new WebChromeClient());
        mWvContentReview.setWebViewClient(new WebViewClient());

        mWvContentReview.loadUrl("file:///android_asset/test.html");
//        mWvContentReview.loadUrl("https://www.baidu.com");
    }
}
