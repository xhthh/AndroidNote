package com.xht.androidnote

import android.os.Handler
import com.xht.androidnote.base.BaseViewActivity
import com.xht.androidnote.databinding.ActivitySplashBinding

class SplashActivity : BaseViewActivity<ActivitySplashBinding>() {

    private val handler = Handler()
    private var mCountNum = 60
    override fun getViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initEventAndData() {

        handler.postDelayed({
            //startActivity(Intent(mContext, MainActivity::class.java))
            startActivity<MainActivity>(this)
            finish()
        }, 500)

        //test
//        startActivity<MainActivity>(mContext) {
//            putExtra("params1", "erdai")
//            putExtra("params2", "666")
//        }
    }
}