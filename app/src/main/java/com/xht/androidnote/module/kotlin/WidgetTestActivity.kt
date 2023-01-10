package com.xht.androidnote.module.kotlin

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.ToastUtils
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import com.xht.androidnote.module.kotlin.deviceId.DeviceIdActivity
import com.xht.androidnote.module.kotlin.location.LocationActivity
import com.xht.androidnote.module.kotlin.location.LocationActivity2
import com.xht.androidnote.module.kotlin.multiSelect.MultiSelectActivity
import com.xht.androidnote.module.kotlin.record.RecordActivity
import com.xht.androidnote.module.kotlin.screenshot.ScreenShotActivity
import com.xht.androidnote.module.kotlin.widget.ClockActivity
import com.xht.androidnote.module.kotlin.widget.StorageTestActivity
import com.xht.androidnote.module.kotlin.widget.TableTestActivity
import com.xht.androidnote.module.kotlin.widget.TextTestActivity
import com.xht.androidnote.utils.CalendarReminderUtils
import com.xht.androidnote.utils.CalendarUtils
import com.xht.androidnote.utils.CalendarUtils.onCalendarRemindListener
import com.xht.androidnote.utils.DateUtils
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.activity_widget_test.*


class WidgetTestActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_widget_test
    }

    @RequiresApi(Build.VERSION_CODES.N)
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
                    LocationActivity::class.java
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

        btnScreenShotTest.setOnClickListener {
            skip2Activity(ScreenShotActivity::class.java)
        }

        btnStorageTest.setOnClickListener {
            skip2Activity(StorageTestActivity::class.java)
        }

        btnCalendarTest1.setOnClickListener {
            var n = 1;
            AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_CALENDAR, Permission.WRITE_CALENDAR)
                .onGranted(Action<List<String?>> { permissions: List<String?>? ->
                    CalendarReminderUtils.addCalendarEvent(
                        this,
                        "学校读书",
                        "吃了饭再去",
                        System.currentTimeMillis() + 10000*n,
                        0
                    );
                    n++
                })
                .onDenied(Action<List<String?>> { permissions: List<String?>? -> })
                .start()
            //设置日历提醒
        }

        btnCalendarTest2.setOnClickListener {
            AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_CALENDAR, Permission.WRITE_CALENDAR)
                .onGranted {
                    CalendarUtils.addCalendarEventRemind(this,
                        "日历提醒",
                        "测试日历提醒",
                        System.currentTimeMillis() + 10000,
                        System.currentTimeMillis() + 300000,
                        0,
                        object : onCalendarRemindListener {
                            override fun onFailed(error_code: onCalendarRemindListener.Status?) {
                                Toast.makeText(mContext,"添加日历失败",Toast.LENGTH_SHORT).show();
                            }
                            override fun onSuccess() {
                                Toast.makeText(mContext,"添加日历成功",Toast.LENGTH_SHORT).show();
                            }
                        })
                }
                .onDenied { }
                .start()
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