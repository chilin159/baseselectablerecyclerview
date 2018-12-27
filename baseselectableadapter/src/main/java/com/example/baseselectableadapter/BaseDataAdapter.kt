package com.example.baseselectableadapter

import android.support.v7.widget.RecyclerView

abstract class BaseDataAdapter<V : RecyclerView.ViewHolder, E : List<*>> : RecyclerView.Adapter<V>() {
    abstract fun setData(dataList: E?)
}
