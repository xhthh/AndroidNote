package com.xht.androidnote.module.kotlin

import android.content.Context
import android.content.Intent
import android.util.Log
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import com.xht.androidnote.module.kotlin.deviceId.DeviceIdActivity
import com.xht.androidnote.module.kotlin.location.LocationActivity2
import com.xht.androidnote.module.kotlin.multiSelect.MultiSelectActivity
import com.xht.androidnote.module.kotlin.record.RecordActivity
import com.xht.androidnote.module.kotlin.widget.ClockActivity
import com.xht.androidnote.module.kotlin.widget.TableTestActivity
import com.xht.androidnote.module.kotlin.widget.TextTestActivity
import com.xht.androidnote.utils.DateUtils
import kotlinx.android.synthetic.main.activity_kotlin_test.*

class KotlinTestActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_kotlin_test
    }

    override fun initEventAndData() {
        btnNullTest.setOnClickListener {

            val bean = Bean()
            bean.desc = null

            //java.lang.IllegalStateException: bean.desc must not be null
            //这里没抓到异常，是不是kotlin版本的问题
            setMessage(bean.desc)

        }

        btnMultiSelect.setOnClickListener {
            startActivity(Intent(this, MultiSelectActivity::class.java))
        }

        btnSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        btnLocation.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    LocationActivity2::class.java
                )
            )
        }

        btnDeviceId.setOnClickListener {
            startActivity(Intent(this, DeviceIdActivity::class.java))
        }
        btnClock.setOnClickListener {
            startActivity(Intent(this, ClockActivity::class.java))
//            testTime2()
        }
        btnRecord.setOnClickListener {
            startActivity(Intent(this, RecordActivity::class.java))
//            testTime()
        }

        btnText.setOnClickListener {
            startActivity(Intent(this, TextTestActivity::class.java))
        }

        btnTable.setOnClickListener {
            //startActivity(Intent(this, TableTestActivity::class.java))
            startActivity<TableTestActivity>(this) {
            }
        }
        btnCoroutines.setOnClickListener {
            //startActivity(Intent(this, TableTestActivity::class.java))
            startActivity<CoroutinesTestActivity>(this) {
            }
        }
    }

    inline fun <reified T> startActivity(context: Context, block: Intent.() -> Unit) {
        val intent = Intent(context, T::class.java)
        intent.block()
        context.startActivity(intent)
    }

    private fun testTime() {
        val isToday = DateUtils.IsToday(System.currentTimeMillis())
        Log.e("xht", "===isToday1=$isToday")
    }

    private fun testTime2() {
        val isToday = DateUtils.IsToday(System.currentTimeMillis() + 86400000 / 2)
        Log.e("xht", "===isToday2=$isToday")
    }

    var data: String? = null

    fun setMessage(data: String) {
        this.data = data
    }

}