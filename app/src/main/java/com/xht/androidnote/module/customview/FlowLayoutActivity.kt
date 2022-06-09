package com.xht.androidnote.module.customview

import android.widget.Button
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import com.xht.androidnote.utils.DimenUtil
import kotlinx.android.synthetic.main.activity_flow_layout.*

class FlowLayoutActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_flow_layout
    }

    override fun initEventAndData() {

        val arrayListOf = arrayOf(
            "Hello", "Android", "Weclome Hi ", "最近由于项目需求，需要呈现出流式布局的搜索关键词。", "TextView", "Hello",
            "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
            "Android", "Weclome Hello", "Button Text", "TextView"
        )

        for ((index, item) in arrayListOf.withIndex()) {
            val button = Button(this)
            //要先addView，然后才能 getLayoutParams，否则获取为 null，addView() 中会生成一个默认的 LayoutParams，宽高参数和父View有关
            flowLayout.addView(button)

            if (index == 1) {
                val layoutParams = button.layoutParams
                layoutParams.height = DimenUtil.dp2px(this, 100f)
                button.layoutParams = layoutParams
            }
            button.text = item
        }

    }
}