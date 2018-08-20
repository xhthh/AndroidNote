package com.xht.androidnote.module.animation;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2018/7/12.
 */

public class PropertyAnimationActivity extends BaseActivity {
    @BindView(R.id.tv_property_object)
    TextView mObject;
    @BindView(R.id.btn_property_increase_width)
    Button mButton;
    private ObjectAnimator mTranslationAnimator;
    private ValueAnimator mColorAnimator;
    private AnimatorSet mAnimatorSet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_property_animation;
    }

    @Override
    protected void initEventAndData() {

    }




    @OnClick({R.id.btn_property_translation, R.id.btn_property_bg_color, R.id
            .btn_property_animation_set, R.id.btn_property_cancel, R.id
            .btn_property_increase_width})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_property_translation:
                exchangeTranslation();
                break;
            case R.id.btn_property_bg_color:
                exchangeBGColor();
                break;
            case R.id.btn_property_animation_set:
                animationSet();
                break;
            case R.id.btn_property_cancel:
                cancelAnimation();
                break;
            case R.id.btn_property_increase_width:
//                performAnimate();
                performAnimate(mButton,200,500);
                break;
        }
    }

    /**
     * 给任意属性添加动画
     */
    private void performAnimate() {
        ObjectAnimator.ofInt(mButton, "width", 500).setDuration(5000).start();

        //ViewWrapper wrapper = new ViewWrapper(mButton);
        //ObjectAnimator.ofInt(wrapper, "width", 500).setDuration(5000).start();
    }

    private void performAnimate(final View target, final int start, final int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            // 持有一个 IntEvaluator 对象，方便下面估值的时候使用
            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                // 获得当前动画的进度值，整型，1~100 之间
                int currentValue = (int) animation.getAnimatedValue();
                L.i("current value：" + currentValue);

                // 获得当前进度占整个动画过程的比例，浮点型，0~1 之间
                float fraction = animation.getAnimatedFraction();

                // 直接调用整型估值器，通过比例计算出宽度，然后再设给 Button
                target.getLayoutParams().width = mEvaluator.evaluate(fraction, start, end);
                target.requestLayout();

            }
        });
        valueAnimator.setDuration(5000).start();
    }

    @SuppressLint("NewApi")
    private void cancelAnimation() {
        if (mTranslationAnimator != null) {
            mTranslationAnimator.cancel();
        }
        if (mColorAnimator != null) {
            mColorAnimator.cancel();//停止
        }
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
            mAnimatorSet.resume();
        }
    }

    private void animationSet() {
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(
                ObjectAnimator.ofFloat(mObject, "rotationX", 0, 360),
                ObjectAnimator.ofFloat(mObject, "rotationY", 0, 180),
                ObjectAnimator.ofFloat(mObject, "rotation", 0, -90),
                ObjectAnimator.ofFloat(mObject, "translationX", 0, 90),
                ObjectAnimator.ofFloat(mObject, "translationY", 0, 90),
                ObjectAnimator.ofFloat(mObject, "scaleX", 1, 1.5f),
                ObjectAnimator.ofFloat(mObject, "scaleY", 1, 0.5f),
                ObjectAnimator.ofFloat(mObject, "alpha", 1, 0.25f, 1)
        );
        mAnimatorSet.setDuration(5 * 1000).start();
    }

    private void exchangeBGColor() {
        // 从红色到蓝色
        mColorAnimator = ObjectAnimator.ofInt(mObject,
                "backgroundColor", 0xFFFF8080, 0xFF8080FF);
        mColorAnimator.setDuration(3000);
        mColorAnimator.setEvaluator(new ArgbEvaluator());
        mColorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mColorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mColorAnimator.start();
    }

    private void exchangeTranslation() {

        mTranslationAnimator = ObjectAnimator.ofFloat(mObject, "translationY", -mObject
                .getHeight());
        mTranslationAnimator.start();

    }


    private static class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View target) {
            mTarget = target;
        }

        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width) {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }
    }


}
