package com.xht.androidnote.module.kotlin.multiSelect

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import com.xht.androidnote.module.kotlin.multiSelect.lib.CheckHelper
import com.xht.androidnote.module.kotlin.multiSelect.lib.MultiCheckHelper
import kotlinx.android.synthetic.main.activity_multi_select.*


/**
 * Created by xht on 2021/7/24
 */
class MultiSelectActivity : BaseActivity() {

    lateinit var clientList: List<ClientInfo>

    override fun getLayoutId(): Int {
        return R.layout.activity_multi_select
    }

    override fun initEventAndData() {

        val list = "{\n" +
                "\t\"list\": [{\n" +
                "\t\t\"title\": \"111111111111111\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"title\": \"2222222\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"title\": \"33333\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"title\": \"4444444444\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"title\": \"55555\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"title\": \"66666\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"title\": \"77777\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"title\": \"8888888888\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"title\": \"9999999\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"title\": \"1000000000000000000000\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"title\": \"111111111111111\"\n" +
                "\t}]\n" +
                "}"

        clientList = Gson().fromJson(list, ClientList::class.java).list

        val checkHelper = MultiCheckHelper()

        //注册选择器
        checkHelper.register(ClientInfo::class.java, object : CheckHelper.Checker<ClientInfo?, MultiSelectAdapter.MultiSelectViewHolder> {
            override fun check(s: ClientInfo?, holder: MultiSelectAdapter.MultiSelectViewHolder) {
                holder.itemView.setBackgroundColor(-0x8c1f1c) //蓝色
                holder.setChecked(R.id.checkbox, true)
            }

            override fun unCheck(s: ClientInfo?, holder: MultiSelectAdapter.MultiSelectViewHolder) {
                holder.itemView.setBackgroundColor(-0x1) //白色
                holder.setChecked(R.id.checkbox, false)
            }
        })
        //添加默认数据
        //checkHelper.add(clientList[0])

        val adapter = MultiSelectAdapter(this, clientList, checkHelper)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter

        btnSelectList.setOnClickListener {
            val checked = checkHelper.getChecked(ClientInfo::class.java)
            println("==============选中的数据" + checked?.size)
        }
    }
}