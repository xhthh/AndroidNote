package com.xht.androidnote.module.kotlin.screenshot

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.blankj.utilcode.util.ScreenUtils
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import com.xht.androidnote.utils.L
import kotlinx.android.synthetic.main.activity_screen_shot.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream




class ScreenShotActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_screen_shot
    }

    override fun initEventAndData() {

        btnScreenShot.setOnClickListener {
            val bitmap = ScreenUtils.screenShot(this,false)
            val width = bitmap.width
            val height = bitmap.height
            L.e("------width=$width height=$height")


            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val isBm: InputStream = ByteArrayInputStream(baos.toByteArray())
            val encodeToString = Base64.encodeToString(isBm.readBytes(), Base64.DEFAULT)


            val imageBytes = Base64.decode(encodeToString,Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            ivResult.setImageBitmap(decodedImage)
        }
    }

    fun test() {
        val bitmap = ScreenUtils.screenShot(this)



    }

}