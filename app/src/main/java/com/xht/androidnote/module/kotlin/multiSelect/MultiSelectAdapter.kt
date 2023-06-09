package com.xht.androidnote.module.kotlin.multiSelect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.xht.androidnote.R
import com.xht.androidnote.databinding.ItemMultiSelectBinding
import com.xht.androidnote.module.kotlin.multiSelect.lib.MultiCheckHelper

/**
 * Created by xht on 2021/7/24
 */
class MultiSelectAdapter(
    val context: Context,
    var list: List<ClientInfo>,
    var checkHelper: MultiCheckHelper,
) : RecyclerView.Adapter<MultiSelectAdapter.MultiSelectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiSelectViewHolder {
        val binding = ItemMultiSelectBinding.inflate(LayoutInflater.from(context), parent, false)
        return MultiSelectViewHolder(
            binding.root
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MultiSelectAdapter.MultiSelectViewHolder, position: Int) {
        val clientInfo = list[position]

        holder.tvTitle.text = clientInfo.title

        checkHelper.bind(clientInfo, holder, holder.itemView)
    }

    class MultiSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)

        fun setChecked(@IdRes viewId: Int, checked: Boolean) {
            checkBox.isChecked = checked
        }
    }

}