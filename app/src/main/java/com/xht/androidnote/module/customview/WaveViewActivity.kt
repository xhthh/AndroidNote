package com.xht.androidnote.module.customview

import android.graphics.Color
import android.graphics.Paint
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseViewActivity
import com.xht.androidnote.databinding.ActivityWaveViewBinding

class WaveViewActivity : BaseViewActivity<ActivityWaveViewBinding>() {
    override fun getViewBinding(): ActivityWaveViewBinding {
        return ActivityWaveViewBinding.inflate(layoutInflater)
    }

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

        binding.waveView.setDuration(5000);
        binding.waveView.setStyle(Paint.Style.FILL);
        binding.waveView.setColor(Color.RED);
        binding.waveView.setInterpolator(LinearOutSlowInInterpolator());
        binding.waveView.start();

        //binding.waveView.postDelayed({ binding.waveView.stop(); }, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.waveView.stop()
    }
}
