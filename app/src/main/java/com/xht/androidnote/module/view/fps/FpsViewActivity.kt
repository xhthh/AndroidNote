package com.xht.androidnote.module.view.fps

import android.os.Build
import androidx.annotation.RequiresApi
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseViewActivity
import com.xht.androidnote.databinding.ActivityFpsBinding

class FpsViewActivity : BaseViewActivity<ActivityFpsBinding>() {
    override fun getViewBinding(): ActivityFpsBinding {
        return ActivityFpsBinding.inflate(layoutInflater)
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_fps
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun initEventAndData() {

        binding.btnStartFps.setOnClickListener {
            FpsMonitor.startMonitor { fps ->
                binding.tvFps.text = String.format("fps: %s", fps)
            }
        }

        binding.btnStopFps.setOnClickListener {
            FpsMonitor.stopMonitor()
            binding.tvFps.text = ""
        }

        binding.btnStopFps.setOnClickListener {
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }


}