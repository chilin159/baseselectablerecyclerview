package com.example.baseselectableadapter

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View

class BaseBindingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var binding: ViewDataBinding? = null

    val context: Context?
        get() = if (binding != null) binding!!.root.context else null

    companion object {

        fun newInstance(binding: ViewDataBinding): BaseBindingHolder {
            val holder = BaseBindingHolder(binding.root)
            holder.binding = binding
            return holder
        }
    }
}