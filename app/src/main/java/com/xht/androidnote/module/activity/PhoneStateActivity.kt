package com.xht.androidnote.module.activity

import com.xht.androidnote.R
import com.xht.androidnote.base.BaseViewActivity
import com.xht.androidnote.databinding.ActivityPhoneStateBinding


class PhoneStateActivity : BaseViewActivity<ActivityPhoneStateBinding>() {
    override fun getViewBinding(): ActivityPhoneStateBinding {
        return ActivityPhoneStateBinding.inflate(layoutInflater)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_phone_state;
    }

    override fun initEventAndData() {

    }

    private var mResumeAfterCall: Boolean = false

    /*
        private val mPhoneStateListener: PhoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    val audioManager = getSystemService<Any>(Context.AUDIO_SERVICE) as AudioManager
                    val ringvolume = audioManager
                        .getStreamVolume(AudioManager.STREAM_RING)
                    if (ringvolume > 0) {
                        mResumeAfterCall = AudioPlayer.player.isPlaying() || mResumeAfterCall
                        try {
                            stub.pause()
                        } catch (e: RemoteException) {
                            // TODO Auto-generated catch block
                            e.printStackTrace()
                        }
                    }
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    // pause the music while a conversation is in progress
                    mResumeAfterCall = AudioPlayer.player.isPlaying() || mResumeAfterCall
                    try {
                        stub.pause()
                    } catch (e: RemoteException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    // start playing again
                    if (mResumeAfterCall) {
                        // resume playback only if music was playing
                        // when the call was answered
                        try {
                            stub.resume()
                        } catch (e: RemoteException) {
                            // TODO Auto-generated catch block
                            e.printStackTrace()
                        }
                        mResumeAfterCall = false
                    }
                }
            }
        }
    */
}