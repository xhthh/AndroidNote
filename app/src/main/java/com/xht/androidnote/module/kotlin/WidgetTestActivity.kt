package com.xht.androidnote.module.kotlin

import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseViewActivity
import com.xht.androidnote.databinding.ActivityWidgetTestBinding
import com.xht.androidnote.module.kotlin.deviceId.DeviceIdActivity
import com.xht.androidnote.module.kotlin.location.LocationActivity
import com.xht.androidnote.module.kotlin.multiSelect.MultiSelectActivity
import com.xht.androidnote.module.kotlin.record.RecordActivity
import com.xht.androidnote.module.kotlin.screenshot.ScreenShotActivity
import com.xht.androidnote.module.kotlin.widget.ClockActivity
import com.xht.androidnote.module.kotlin.widget.StatusBarTestActivity
import com.xht.androidnote.module.kotlin.widget.StorageTestActivity
import com.xht.androidnote.module.kotlin.widget.TableTestActivity
import com.xht.androidnote.module.kotlin.widget.TextTestActivity
import com.xht.androidnote.utils.*
import com.xht.androidnote.utils.CalendarUtils.onCalendarRemindListener
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission


class WidgetTestActivity : BaseViewActivity<ActivityWidgetTestBinding>() {
    override fun getViewBinding(): ActivityWidgetTestBinding {
        return ActivityWidgetTestBinding.inflate(layoutInflater)
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_widget_test
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initEventAndData() {
        binding.btnStatusBar.setOnClickListener {
            startActivity<StatusBarTestActivity>(this) {}
        }
        binding.btnNullTest.setOnClickListener {

            val bean = Bean()
            bean.desc = null

            //java.lang.IllegalStateException: bean.desc must not be null
            //这里没抓到异常，是不是kotlin版本的问题
            setMessage(bean.desc)

        }

        binding.btnMultiSelect.setOnClickListener {
            startActivity(Intent(this, MultiSelectActivity::class.java))
        }

        binding.btnSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        binding.btnLocation.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    LocationActivity::class.java
                )
            )
        }

        binding.btnDeviceId.setOnClickListener {
            startActivity(Intent(this, DeviceIdActivity::class.java))
        }
        binding.btnClock.setOnClickListener {
            startActivity(Intent(this, ClockActivity::class.java))
//            testTime2()
        }
        binding.btnRecord.setOnClickListener {
            startActivity(Intent(this, RecordActivity::class.java))
//            testTime()
        }

        binding.btnText.setOnClickListener {
            startActivity(Intent(this, TextTestActivity::class.java))
        }

        binding.btnTable.setOnClickListener {
            //startActivity(Intent(this, TableTestActivity::class.java))
            startActivity<TableTestActivity>(this) {
            }
        }
        binding.btnCoroutines.setOnClickListener {
            //startActivity(Intent(this, TableTestActivity::class.java))
            startActivity<CoroutinesTestActivity>(this) {
            }
        }

        binding.btnScreenShotTest.setOnClickListener {
            skip2Activity(ScreenShotActivity::class.java)
        }

        binding.btnStorageTest.setOnClickListener {
            skip2Activity(StorageTestActivity::class.java)
        }

        binding.btnCalendarTest1.setOnClickListener {
            var n = 1;
            AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_CALENDAR, Permission.WRITE_CALENDAR)
                .onGranted(Action<List<String?>> { permissions: List<String?>? ->
                    CalendarReminderUtils.addCalendarEvent(
                        this,
                        "学校读书",
                        "吃了饭再去",
                        System.currentTimeMillis() + 10000 * n,
                        0
                    );
                    n++
                })
                .onDenied(Action<List<String?>> { permissions: List<String?>? -> })
                .start()
            //设置日历提醒
        }

        binding.btnCalendarTest2.setOnClickListener {
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
                                Toast.makeText(mContext, "添加日历失败", Toast.LENGTH_SHORT).show();
                            }

                            override fun onSuccess() {
                                Toast.makeText(mContext, "添加日历成功", Toast.LENGTH_SHORT).show();
                            }
                        })
                }
                .onDenied { }
                .start()
        }

        binding.btnTestGoToPermission.setOnClickListener {
            //startActivity(Intent(this, TestGoToPermissionActivity::class.java))
            JumpPermissionManagement.GoToSetting(this)
        }

        val timeCount = TimeCount(20000, 1000, binding.tvGetCode)
        //测试TextView不可点击问题
        binding.tvGetCode.setOnClickListener {
            Log.e("textView", "------点击获取验证码，倒计时，设为不可点击------");
            binding.tvGetCode.isClickable = false;
            timeCount.start()
        }
        timeCount.setOnTimeFinishListener(object : TimeCount.OnTimeFinishListener {
            override fun onTimeFinished() {
                Log.e("textView", "------倒计时结束，设为可点击------");
                binding.tvGetCode.isClickable = true
            }

            override fun onTimeGoingOn() {
            }
        })
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