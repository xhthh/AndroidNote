package com.xht.androidnote.module.kotlin.record

import android.content.Intent
import android.os.Environment
import android.os.SystemClock
import android.view.WindowManager
import android.widget.Toast
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import kotlinx.android.synthetic.main.activity_record.*
import java.io.File

class RecordActivity : BaseActivity() {

    private var mStartRecording = true
    var timeWhenPaused: Long = 0 //stores time when user clicks pause button

    var beginTime: Long = 0L

    override fun getLayoutId(): Int {
        return R.layout.activity_record
    }

    override fun initEventAndData() {
        val requestPermission = RequestPermission()
        requestPermission.RequestPermission(this)

        ibSwitch.setOnClickListener {

            onRecord(mStartRecording)
            mStartRecording = !mStartRecording
        }
    }

    private fun onRecord(start: Boolean) {
        val intent = Intent(this, RecordingService::class.java)

        if (start) {
            // start recording
            ibSwitch.setBackgroundResource(R.drawable.icon_record_end_white)
            //mPauseButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, "开始录音", Toast.LENGTH_SHORT).show()
            val folder =
                File(Environment.getExternalStorageDirectory().toString() + "/SoundRecorder")
            if (!folder.exists()) {
                //folder /SoundRecorder doesn't exist, create the folder
                folder.mkdir()
            }

            //start Chronometer
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.start()

            //start RecordingService
            startService(intent)
            //keep screen on while recording
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            beginTime = System.currentTimeMillis()
        } else {
            //stop recording
            ibSwitch.setBackgroundResource(R.drawable.icon_record_begin_white)
            //mPauseButton.setVisibility(View.GONE);
            chronometer.stop()
            chronometer.base = SystemClock.elapsedRealtime()
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