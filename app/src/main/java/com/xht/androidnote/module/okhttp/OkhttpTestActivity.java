package com.xht.androidnote.module.okhttp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xht on 2019/4/16.
 */

public class OkhttpTestActivity extends BaseActivity {
    @BindView(R.id.btn_okhttp_get)
    Button mBtnOkhttpGet;
    @BindView(R.id.btn_okhttp_post)
    Button mBtnOkhttpPost;
    @BindView(R.id.btn_okhttp_post_string)
    Button mBtnOkhttpPostString;
    @BindView(R.id.btn_okhttp_post_file)
    Button mBtnOkhttpPostFile;
    @BindView(R.id.btn_okhttp_post_upload)
    Button mBtnOkhttpPostUpload;
    @BindView(R.id.btn_okhttp_download)
    Button mBtnOkhttpDownload;
    @BindView(R.id.btn_okhttp_download_display_img)
    Button mBtnOkhttpDownloadDisplayImg;
    @BindView(R.id.btn_okhttp_cache)
    Button mBtnOkhttpCache;
    @BindView(R.id.tv_okhttp_get_result)
    TextView mTvOkhttpGetResult;
    @BindView(R.id.iv_okhttp_image)
    ImageView mIvOkhttpImage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_okhttp;
    }

    @Override
    protected void initEventAndData() {

        getRequest();

    }

    private String getRequest() {
        String url = "";
        OkHttpClient client = new OkHttpClient();

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();


        Request request = new Request.Builder()
                .url(url)
                .build();


        try {
            Call call = client.newCall(request);

            Response response = call.execute();//同步请求

            //异步请求
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });

            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @OnClick({R.id.btn_okhttp_get, R.id.btn_okhttp_post, R.id.btn_okhttp_post_string, R.id.btn_okhttp_post_file, R.id.btn_okhttp_post_upload, R.id.btn_okhttp_download, R.id.btn_okhttp_download_display_img, R.id.btn_okhttp_cache, R.id.tv_okhttp_get_result, R.id.iv_okhttp_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_okhttp_get:
                break;
            case R.id.btn_okhttp_post:
                break;
            case R.id.btn_okhttp_post_string:
                break;
            case R.id.btn_okhttp_post_file:
                break;
            case R.id.btn_okhttp_post_upload:
                break;
            case R.id.btn_okhttp_download:
                break;
            case R.id.btn_okhttp_download_display_img:
                break;
            case R.id.btn_okhttp_cache:
                break;
            case R.id.tv_okhttp_get_result:
                break;
            case R.id.iv_okhttp_image:
                break;
        }
    }
}
