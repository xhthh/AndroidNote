package com.xht.androidnote.module.kotlin.widget

import android.content.Intent
import android.util.Log
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import kotlinx.android.synthetic.main.activity_storage_test.*

class StorageTestActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_storage_test
    }

    override fun initEventAndData() {
        btnPicSelectTest.setOnClickListener {
            val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
            contentSelectionIntent.type = "image/*"

            val chooserIntent = Intent(Intent.ACTION_CHOOSER)
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "图片选择")
            startActivityForResult(
                chooserIntent, 233
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            233 -> {
                val result = if (data == null || resultCode != RESULT_OK) null else data.data
                Log.e("xht", "----------result======$result");
                ivResult1.setImageURI(result)
            }
        }
    }
}