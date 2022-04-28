package com.xht.androidnote.module.okhttp.scode

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xht.androidnote.R
import okhttp3.*
import java.io.IOException

class OkHttpTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_okhttp_test)

        test1()
    }

    private fun test1() {
        val client = OkHttpClient()
            .newBuilder()
//            .dns(httpdns)
            .build()
        val request = Request.Builder()
            .url("https://www.baidu.com")
            .tag("")//cancel call
            .build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("onFailure:$e")
            }

            override fun onResponse(call: Call, response: Response) {
                println("onResponse:${response.body?.string()}")
            }
        })
    }

}