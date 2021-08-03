package com.xht.androidnote.module.kotlin.record

import android.media.MediaRecorder
import android.text.format.DateFormat
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import kotlinx.android.synthetic.main.activity_record.*
import java.io.File
import java.util.*

class RecordActivity : BaseActivity() {

    private var filePath: String? = null
    private var fileName: String? = null
    var mMediaRecorder: MediaRecorder? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_record
    }

    override fun initEventAndData() {

        val requestPermission = RequestPermission()
        requestPermission.RequestPermission(this)

        btnStart.setOnClickListener {

            startRecord()
        }

        btnEnd.setOnClickListener {
            stopRecord()
        }

        btnPlay.setOnClickListener {
            play()
        }

        mMediaRecorder = MediaRecorder()
    }

    private fun play() {

    }

    private fun stopRecord() {
        try {
            mMediaRecorder?.stop()
            mMediaRecorder?.release()
            mMediaRecorder = null
        } catch (e: RuntimeException) {
            mMediaRecorder?.reset()
            mMediaRecorder?.release()
            mMediaRecorder = null
            val file = File(filePath)
            if (file.exists()) file.delete()
        }
    }

    private fun startRecord() {
        mMediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mMediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        fileName = DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA))
            .toString() + ".mp3";
        val destDir = File((externalCacheDir?.path ?: "") + "/test/")
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        filePath = (externalCacheDir?.path ?: "") + "/test/" + fileName

        mMediaRecorder?.setOutputFile(filePath);
        mMediaRecorder?.prepare();
        mMediaRecorder?.start();
    }

}