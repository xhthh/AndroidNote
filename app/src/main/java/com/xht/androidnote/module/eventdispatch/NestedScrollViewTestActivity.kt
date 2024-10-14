package com.xht.androidnote.module.eventdispatch

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseViewActivity
import com.xht.androidnote.databinding.ActivityNestedScrollViewTestBinding

class NestedScrollViewTestActivity : BaseViewActivity<ActivityNestedScrollViewTestBinding>() {
    override fun getViewBinding(): ActivityNestedScrollViewTestBinding {
        return ActivityNestedScrollViewTestBinding.inflate(layoutInflater)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_nested_scroll_view_test
    }

    override fun initEventAndData() {

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 生成一些测试数据
        val itemList = List(20) { Person("Item #$it") }

        // 绑定 Adapter
        val adapter = MyAdapter(itemList)
        recyclerView.adapter = adapter
    }

}

data class Person(val name: String)
