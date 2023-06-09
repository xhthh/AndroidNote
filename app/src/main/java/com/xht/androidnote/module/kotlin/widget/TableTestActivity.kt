package com.xht.androidnote.module.kotlin.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import com.bin.david.form.core.SmartTable
import com.bin.david.form.data.CellInfo
import com.bin.david.form.data.column.Column
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat
import com.bin.david.form.data.style.FontStyle
import com.bin.david.form.data.table.TableData
import com.bin.david.form.listener.OnColumnItemClickListener
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseViewActivity
import com.xht.androidnote.databinding.ActivityTableTestBinding
import com.xht.androidnote.module.kotlin.bean.EmergencyContactInfo
import com.xht.androidnote.module.kotlin.widget.adapter.CustomSpinnerArrayAdapter
import com.xht.androidnote.module.kotlin.widget.treeView.Node
import com.xht.androidnote.module.kotlin.widget.treeView.TreeListView


class TableTestActivity : BaseViewActivity<ActivityTableTestBinding>() {

    lateinit var dialogFragment: TreeFragment
    override fun getViewBinding(): ActivityTableTestBinding {
        return ActivityTableTestBinding.inflate(layoutInflater)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_table_test
    }

    override fun initEventAndData() {
        initData()
        testSpinner()
        binding.btnDialog.setOnClickListener {
//            showDialogFragment()
            showDialog()
        }
    }

    var list: MutableList<Node> = mutableListOf()

    private var listView: TreeListView? = null

    private var rl: RelativeLayout? = null

    private var dialog: Dialog? = null

    private fun showDialog() {
        val view = layoutInflater.inflate(R.layout.fragment_tree, null)

        var content = view.findViewById<RelativeLayout>(R.id.content)
        rl = RelativeLayout(this)
        rl!!.layoutParams =
            RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        listView = TreeListView(this, initNodeTree())
        listView!!.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        content.addView(listView)


        if (dialog == null) {
            dialog = AlertDialog.Builder(this)
                .setView(view)
//                .setNegativeButton(
//                    "取消"
//                ) { dialog, which -> Toast.makeText(mContext, "点击取消", Toast.LENGTH_SHORT).show() }
//                .setPositiveButton(
//                    "确定"
//                ) { dialog, which -> Toast.makeText(mContext, "点击确定", Toast.LENGTH_SHORT).show() }
                .create()
        }
        setDialogWidthAndHeight(dialog, 500, 400)
        dialog?.show()


    }

    fun setDialogWidthAndHeight(dialog: Dialog?, widthDp: Int, heightDp: Int) {
        val wm = this.getSystemService(WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels // 屏幕宽度（像素）
        val height = dm.heightPixels // 屏幕高度（像素）
        val density = dm.density //屏幕密度（0.75 / 1.0 / 1.5）
        val densityDpi = dm.densityDpi //屏幕密度dpi（120 / 160 / 240）
        val layoutParams = dialog?.window!!.attributes
        layoutParams.width = (widthDp * densityDpi / 160)
        layoutParams.height = (heightDp * densityDpi / 160)
        dialog?.window!!.attributes = layoutParams
    }


    fun initNodeTree(): List<Node?>? {
        val member_list: MutableList<Node> = java.util.ArrayList()
        //        -1表示为根节点,id的作用为
        member_list.add(Node("" + -1, "1", "111"))
        member_list.add(Node("" + 1, "2", "222"))
        member_list.add(Node("" + -1, "3", "333"))
        member_list.add(Node("" + 1, "4", "444"))
        member_list.add(Node("" + 4, "5", "555"))
        member_list.add(Node("" + 4, "6", "666"))
        member_list.add(Node("" + 4, "7", "777"))
        member_list.add(Node("" + 7, "8", "888"))
        member_list.add(Node("" + 8, "9", "999"))
        member_list.add(Node("" + 8, "10", "101010"))
        list.addAll(member_list)
        return list
    }

    private fun showDialogFragment() {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        dialogFragment = TreeFragment()
        dialogFragment.show(ft, "dialog")
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

        binding.spinner.dropDownWidth = dp2px(this, 90F)
        binding.spinner.dropDownVerticalOffset = dp2px(this, 30F)
        binding.spinner.setBackgroundResource(R.drawable.spinner)

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
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