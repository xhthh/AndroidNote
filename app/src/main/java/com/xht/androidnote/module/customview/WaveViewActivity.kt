package com.xht.androidnote.module.customview

import android.graphics.Color
import android.graphics.Paint
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import kotlinx.android.synthetic.main.activity_wave_view.*

class WaveViewActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_wave_view
    }

    override fun initEventAndData() {
        /*
        wave_view.setStyle(Paint.Style.STROKE);
        wave_view.setSpeed(400);
        wave_view.setColor(Color.RED);
        wave_view.setInterpolator(AccelerateInterpolator (1.2f));
        wave_view.start();
        */

        wave_view.setDuration(5000);
        wave_view.setStyle(Paint.Style.FILL);
        wave_view.setColor(Color.RED);
        wave_view.setInterpolator(LinearOutSlowInInterpolator());
        wave_view.start();

        //wave_view.postDelayed({ wave_view.stop(); }, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        wave_view.stop()
    }
}
