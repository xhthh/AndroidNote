package com.xht.androidnote.module.kotlin.widget

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import com.xht.androidnote.R
import com.xht.androidnote.module.kotlin.widget.treeView.Node
import com.xht.androidnote.module.kotlin.widget.treeView.TreeListView
import kotlinx.android.synthetic.main.fragment_tree.*
import java.util.*


class TreeFragment() : DialogFragment() {

    var list: MutableList<Node> = mutableListOf()

    private var listView: TreeListView? = null

    private var rl: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val params = dialog.window?.attributes
//        params?.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
//        params?.windowAnimations = com.xht.androidnote.R.style.bottomSheet_animation
//        dialog.window!!.attributes = params
//        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCanceledOnTouchOutside(true)
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return inflater.inflate(com.xht.androidnote.R.layout.fragment_tree, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        rl = RelativeLayout(context)
        rl!!.layoutParams =
            RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        listView = TreeListView(context, initNodeTree())
        listView!!.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        content.addView(listView)

    }

    override fun onStart() {
        super.onStart()
        val dialogWidth = 600
        val dialogHeight = 300
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
        val window: Window? = dialog!!.window
        val lp: WindowManager.LayoutParams? = window?.attributes
        lp?.dimAmount = 0F
        lp?.flags = lp?.flags?.or(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window?.attributes = lp

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)

    }

    fun initNodeTree(): List<Node?>? {
        val member_list: MutableList<Node> = ArrayList()
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

    override fun onDismiss(dialog: DialogInterface) {

    }

}