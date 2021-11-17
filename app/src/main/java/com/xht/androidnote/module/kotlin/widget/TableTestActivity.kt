package com.xht.androidnote.module.kotlin.widget

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.bin.david.form.core.SmartTable
import com.bin.david.form.data.CellInfo
import com.bin.david.form.data.column.Column
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat
import com.bin.david.form.data.style.FontStyle
import com.bin.david.form.data.table.TableData
import com.bin.david.form.listener.OnColumnItemClickListener
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import com.xht.androidnote.module.kotlin.bean.EmergencyContactInfo
import com.xht.androidnote.module.kotlin.widget.adapter.CustomSpinnerArrayAdapter
import kotlinx.android.synthetic.main.activity_table_test.*
import kotlinx.android.synthetic.main.item_list_content.view.*


class TableTestActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return com.xht.androidnote.R.layout.activity_table_test
    }

    override fun initEventAndData() {


//        //普通列
//        //普通列
//        val city: Column<String> = Column("部门/渠道", "city")
//        val name: Column<Int> = Column("板块", "name")
//        val count: Column<Int> = Column("目标值", "count")
//        val restaurant: Column<Int> = Column("餐饮", "restaurant")
//        val ka: Column<Int> = Column("KA", "ka")
//        val wholesale: Column<Int> = Column("流通批发", "wholesale")
//        val industry: Column<Int> = Column("工业加工", "industry")
//        val other: Column<Int> = Column("其他", "other")
//        //设置该列当字段相同时自动合并
//        //设置该列当字段相同时自动合并
//        city.isAutoMerge = true
//
//
//        //设置单元格内容
//        //设置单元格内容
//        val list: MutableList<User> = ArrayList<User>()
//        list.add(User("沈阳", 100, 150, 50, 240, 1100, 450, 23458))
//        list.add(User("沈阳", 100, 150, 50, 240, 1100, 450, 23458))
//        list.add(User("沈阳", 100, 150, 50, 240, 1100, 450, 23458))
//        list.add(User("沈阳", 100, 150, 50, 240, 1100, 450, 23458))
//        list.add(User("乌鲁木齐", 100, 150, 50, 240, 1100, 450, 23458))
//        list.add(User("乌鲁木齐", 100, 150, 50, 240, 1100, 450, 23458))
//        list.add(User("乌鲁木齐", 100, 150, 50, 240, 1100, 450, 23458))
//        list.add(User("乌鲁木齐", 100, 150, 50, 240, 1100, 450, 23458))
//        list.add(User("沈阳", 100, 150, 50, 240, 1100, 450, 23458))
//        list.add(User("沈阳", 100, 150, 50, 240, 1100, 450, 23458))
//        list.add(User("沈阳", 100, 150, 50, 240, 1100, 450, 23458))
//        list.add(User("沈阳", 100, 150, 50, 240, 1100, 450, 23458))
//
//        //表格数据 datas 是需要填充的数据
//        //表格数据 datas 是需要填充的数据
//        var tableData: TableData<User> =
//            TableData<User>("表格名",
//                list,
//                city,
//                name,
//                count,
//                restaurant,
//                ka,
//                wholesale,
//                industry,
//                other)
//
////设置数据
//
////设置数据
//        val table = findViewById<SmartTable<User>>(R.id.table)
//        table.tableData = tableData
//        table.config.contentStyle = FontStyle(50, Color.BLUE)
        initData()
        testSpinner()
    }

    private fun testSpinner() {

        val stringArray = resources.getStringArray(R.array.languages)

        val customSpinnerArrayAdapter = CustomSpinnerArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            stringArray,
            12f
        )

//        spinner.adapter = customSpinnerArrayAdapter

        spinner.dropDownWidth = dp2px(this, 90F)
        spinner.dropDownVerticalOffset = dp2px(this, 30F)
        spinner.setBackgroundResource(R.drawable.spinner)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.e("xht", "sdafdasdfafd")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    private fun initData() {
        val number: Column<Int> = Column("序号", "number")
        val name: Column<String> = Column("姓名", "name")
        val relation: Column<String> = Column("关系", "relation")
        val mobile: Column<String> = Column("手机号", "mobile")
        val status: Column<String> = Column("手机号状态", "status")

        val list: MutableList<EmergencyContactInfo> = ArrayList()
        list.add(EmergencyContactInfo(1, "张三", "同事", "18888888888", "开机"))
        list.add(EmergencyContactInfo(2, "李四", "同事", "18888888888", "关机"))
        list.add(EmergencyContactInfo(3, "王五", "同事", "18888888888", "开机"))
        list.add(EmergencyContactInfo(4, "赵六", "同事", "18888888888", "关机"))

        var tableData: TableData<EmergencyContactInfo> =
            TableData("表格名", list, number, name, relation, mobile, status)

//        tableData.ySequenceFormat = object : NumberSequenceFormat() {
//            override fun format(position: Int): String {
//                return if (position == 1) {
//                    "序号"
//                } else super.format(position - 1)
//            }
//        }

        val table = findViewById<SmartTable<EmergencyContactInfo>>(R.id.table)


        table.tableData = tableData


        table.config.contentCellBackgroundFormat =
            object : BaseCellBackgroundFormat<CellInfo<*>>() {
                override fun getBackGroundColor(cellInfo: CellInfo<*>): Int {
                    return 0
                }

                override fun getTextColor(cellInfo: CellInfo<*>): Int {
                    if (cellInfo.column.columnName.equals("手机号")) {
                        return Color.parseColor("#00ff00");
                    }
                    return super.getTextColor(cellInfo)
                }
            }
        mobile.setOnColumnItemClickListener(OnColumnItemClickListener<String?> { column, value, integer, position ->
            Toast.makeText(
                this@TableTestActivity,
                "点击了$value",
                Toast.LENGTH_SHORT
            ).show()
        })
        table.config.contentStyle = FontStyle(50, Color.BLUE)
        table.config.isShowTableTitle = false
        table.config.isShowXSequence = false
        table.config.isShowYSequence = false

    }
}