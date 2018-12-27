package com.example.baseselectableadapter

import android.view.View

interface RecyclerViewListListener {
    fun onItemClick(viewType: Int, view: View, adapterPos: Int)
}
