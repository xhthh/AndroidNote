package com.xht.androidnote.module.animation;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2018/7/11.
 * 1、View动画
 * 2、帧动画
 * 3、属性动画
 */

public class AnimationActivity extends BaseActivity {
    @BindView(R.id.iv_animation)
    ImageView mIvAnimation;
    private AnimationDrawable mAnimationDrawable;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_animation;
    }

    @Override
    protected void initEventAndData() {


    }

    @OnClick({R.id.btn_frame_animation, R.id.btn_view_animation_translate, R.id
            .btn_view_animation_scale, R.id.btn_view_animation_rotate, R.id
            .btn_view_animation_alpha, R.id.btn_layout_animation, R.id.btn_property_animation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_frame_animation:
                showFrameAnimation();
                break;
            case R.id.btn_view_animation_translate:
                translate();
                break;
            case R.id.btn_view_animation_scale:
                break;
            case R.id.btn_view_animation_rotate:
                break;
            case R.id.btn_view_animation_alpha:
                break;
            case R.id.btn_layout_animation:
                //skip2Activity(ListViewAnimationActivity.class);
                Intent intent = new Intent(mContext, ListViewAnimationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                break;
            case R.id.btn_property_animation:
                skip2Activity(PropertyAnimationActivity.class);
                break;
        }
    }

    private void translate() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 200, 200);
        translateAnimation.setDuration(1000);
        // 开始动画
        mIvAnimation.startAnimation(translateAnimation);
    }

    private void showFrameAnimation() {
        mIvAnimation.setImageResource(R.drawable.jd_refresh_loading);
        mAnimationDrawable = (AnimationDrawable) mIvAnimation.getDrawable();
        mAnimationDrawable.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAnimationDrawable != null) {
            mAnimationDrawable.stop();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
    }
}


