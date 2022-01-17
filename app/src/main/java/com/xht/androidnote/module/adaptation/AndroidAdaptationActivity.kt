package com.xht.androidnote.module.adaptation

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import kotlinx.android.synthetic.main.activity_adaptation.*
import kotlin.concurrent.thread

class AndroidAdaptationActivity : BaseActivity() {
    private val TAG = "adaptation"
    override fun getLayoutId(): Int {
        return R.layout.activity_adaptation
    }

    override fun initEventAndData() {
        /*
        2022-01-11 17:36:05.979 20127-20127/com.xht.androidnote E/adaptation: 内部路径：filesDir = /data/user/0/com.xht.androidnote/files cacheDir = /data/user/0/com.xht.androidnote/cache
        2022-01-11 17:36:05.980 20127-20127/com.xht.androidnote E/adaptation: 内部路径：getExternalFilesDir = /storage/emulated/0/Android/data/com.xht.androidnote/files getExternalCacheDir = /storage/emulated/0/Android/data/com.xht.androidnote/cache
        2022-01-11 17:36:05.980 20127-20127/com.xht.androidnote E/adaptation: 外部路径：getExternalStorageDirectory() = /storage/emulated/0
         */

        Log.e(TAG, "内部路径：filesDir = " + filesDir.path + " cacheDir = " + cacheDir.path)
        Log.e(
            TAG,
            "内部路径：getExternalFilesDir = " + getExternalFilesDir("")?.path + " getExternalCacheDir = " + externalCacheDir?.path
        )

        Log.e(
            TAG,
            "外部路径：getExternalStorageDirectory() = " + Environment.getExternalStorageDirectory().path
        )

        btnSelectPic.setOnClickListener {
            val permissionsToRequire = ArrayList<String>()
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequire.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequire.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (permissionsToRequire.isNotEmpty()) {
                ActivityCompat.requestPermissions(this, permissionsToRequire.toTypedArray(), 0)
            }

            thread {
                selectPic()
            }
        }

        btnAndroidId.setOnClickListener {
            val androidId: String =
                Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

            Log.e(TAG, "androidId = $androidId")
        }
    }

    private fun selectPic() {
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
            "${MediaStore.MediaColumns.DATE_ADDED} desc"
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                Log.e(TAG, "image uri is $uri")
            }
            cursor.close()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You must allow all the permissions.", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }
        }
    }
}