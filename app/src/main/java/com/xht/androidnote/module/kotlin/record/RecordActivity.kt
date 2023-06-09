package com.xht.androidnote.module.kotlin.record

import android.content.Intent
import android.os.Environment
import android.os.SystemClock
import android.view.WindowManager
import android.widget.Toast
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseViewActivity
import com.xht.androidnote.databinding.ActivityRecordBinding
import java.io.File

class RecordActivity : BaseViewActivity<ActivityRecordBinding>() {

    private var mStartRecording = true
    var timeWhenPaused: Long = 0 //stores time when user clicks pause button

    var beginTime: Long = 0L
    override fun getViewBinding(): ActivityRecordBinding {
        return ActivityRecordBinding.inflate(layoutInflater)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_record
    }

    override fun initEventAndData() {
        val requestPermission = RequestPermission()
        requestPermission.RequestPermission(this)

        binding.ibSwitch.setOnClickListener {

            onRecord(mStartRecording)
            mStartRecording = !mStartRecording
        }
    }

    private fun onRecord(start: Boolean) {
        val intent = Intent(this, RecordingService::class.java)

        if (start) {
            // start recording
            binding.ibSwitch.setBackgroundResource(R.drawable.icon_record_end_white)
            //mPauseButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, "开始录音", Toast.LENGTH_SHORT).show()
            val folder =
                File(Environment.getExternalStorageDirectory().toString() + "/SoundRecorder")
            if (!folder.exists()) {
                //folder /SoundRecorder doesn't exist, create the folder
                folder.mkdir()
            }

            //start Chronometer
            binding.chronometer.base = SystemClock.elapsedRealtime()
            binding.chronometer.start()

            //start RecordingService
            startService(intent)
            //keep screen on while recording
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            beginTime = System.currentTimeMillis()
        } else {
            //stop recording
            binding.ibSwitch.setBackgroundResource(R.drawable.icon_record_begin_white)
            //mPauseButton.setVisibility(View.GONE);
            binding.chronometer.stop()
            binding.chronometer.base = SystemClock.elapsedRealtime()
            timeWhenPaused = 0
            stopService(intent)
            //allow the screen to turn off again once recording is finished
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    override fun onResume() {
        super.onResume()

    }


}