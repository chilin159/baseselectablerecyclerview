package com.example.chilin.baseselectableadapterexample.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.IdRes
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.baseselectableadapter.BaseBindingHolder
import com.example.baseselectableadapter.BaseSelectableAdapter
import com.example.chilin.baseselectableadapterexample.R
import com.example.chilin.baseselectableadapterexample.model.MultiContentSelectModel

class MultiContentAdapter<MultiContentModel>(@IdRes bindingVariable: Int) : BaseSelectableAdapter<com.example.chilin.baseselectableadapterexample.model.MultiContentModel>(bindingVariable) {

    override fun getMainContentItemViewType(position: Int): Int {
        if (mData.size > position && mData[position] is MultiContentSelectModel) {
            return TYPE_SELECT
        } else {
            return TYPE_CLICK
        }
    }

    override fun onCreateMainContentViewHolder(parent: ViewGroup, viewType: Int): BaseBindingHolder {
        val inflater = LayoutInflater.from(parent.context)
        lateinit var binding: ViewDataBinding
        when (viewType) {
            TYPE_CLICK -> {
                binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.item_multi_content, parent, false)
                val holder = BaseBindingHolder.newInstance(binding)
                initClickListener(holder)
                return holder

            }
            else -> {
                binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.item_multi_content_selectable, parent, false)
                val holder = BaseBindingHolder.newInstance(binding)
                initClickAndSelectListener(holder)
                return holder
            }
        }
    }

    companion object {
        val TYPE_CLICK = 101
        val TYPE_SELECT = 102
    }
}