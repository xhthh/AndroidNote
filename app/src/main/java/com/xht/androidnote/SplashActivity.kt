package com.xht.androidnote

import android.content.Context
import android.content.Intent
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


    //泛型
    inline fun <reified T> startActivity(mContext: Context) {
        val intent = Intent(mContext, T::class.java)
        mContext.startActivity(intent)
    }

    inline fun <reified T> startActivity(mContext: Context, block: Intent.() -> Unit) {
        val intent = Intent(mContext, T::class.java)
        intent.block()
        mContext.startActivity(intent)
    }
}