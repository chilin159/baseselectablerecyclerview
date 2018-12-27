@file:JvmName("BindingUtils")

package com.example.baseselectableadapter

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView

@BindingAdapter("app:baseSelectableDataSource")
fun <T> setDataSource(recyclerView: RecyclerView, dataList: List<T>?) {
    recyclerView.adapter.takeIf { it is BaseSelectableAdapter<*> }?.let {
        (it as BaseSelectableAdapter<T>).setData(dataList)
    }
}

@BindingAdapter("app:singleSelectedPosition")
fun <T> setSingleSelectedPosition(recyclerView: RecyclerView, position: Int) {
    recyclerView.adapter.takeIf { it is BaseSelectableAdapter<*> }?.let {
        (it as BaseSelectableAdapter<T>).setSingleSelectedPosition(position)
    }
}