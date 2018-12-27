package com.example.baseselectableadapter

import android.view.View

interface RecyclerViewListSelectListener {
    fun onItemSelect(viewType: Int, view: View, adapterPos: Int)
}