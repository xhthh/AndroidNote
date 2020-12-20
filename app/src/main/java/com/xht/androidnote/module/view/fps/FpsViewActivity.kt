package com.xht.androidnote.module.view.fps

import android.os.Build
import androidx.annotation.RequiresApi
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import kotlinx.android.synthetic.main.activity_fps.*

class FpsViewActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_fps
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun initEventAndData() {

        btn_start_fps.setOnClickListener {
            FpsMonitor.startMonitor { fps ->
                tv_fps.text = String.format("fps: %s", fps)
            }
        }

        btn_stop_fps.setOnClickListener {
            FpsMonitor.stopMonitor()
            tv_fps.text = ""
        }
    }


}