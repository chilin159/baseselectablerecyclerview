@file:JvmName("BindingUtils")

package com.example.baseselectableadapter

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView

@SuppressWarnings("unchecked")
@BindingAdapter("app:dataSource")
fun <T> setDataSource(recyclerView: RecyclerView, dataList: List<T>?) {
    val adapter = recyclerView.adapter
    if (adapter != null) {
        (adapter as BaseSelectableAdapter<T>).setData(dataList)
    }
}

@BindingAdapter("app:singleSelectedPosition")
fun <T> setSingleSelectedPosition(recyclerView: RecyclerView, position: Int) {
    val adapter = recyclerView.adapter
    if (adapter != null) {
        (adapter as BaseSelectableAdapter<T>).setSingleSelectedPosition(position)
    }
}