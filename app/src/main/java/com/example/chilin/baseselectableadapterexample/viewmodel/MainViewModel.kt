package com.example.chilin.baseselectableadapterexample.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.databinding.ObservableArrayList
import android.databinding.ObservableInt
import android.view.View
import android.widget.TextView
import com.example.baseselectableadapter.RecyclerViewListListener
import com.example.chilin.baseselectableadapterexample.ClickableFooter
import com.example.chilin.baseselectableadapterexample.architecture.SingleLiveEvent

class MainViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    val showToastEvent = SingleLiveEvent<String>()
    val clickableList = ObservableArrayList<String>()
    val clickableListListener = object : RecyclerViewListListener {
        override fun onItemClick(viewType: Int, view: View, adapterPos: Int) {
            if (view is TextView) {
                showToastEvent.postValue(view.text.toString())
            }
        }
    }

    val singleSelectedPos = ObservableInt(-1)
    val singleSelectFooter = object : ClickableFooter() {
        override fun footerText(): String {
            return "Show selected item"
        }

        override fun onBtnClick() {
            if (singleSelectedPos.get() >= 0) {
                showToastEvent.postValue(clickableList[singleSelectedPos.get()])
            }
        }
    }
    val singleSelectListListener = object : RecyclerViewListListener {
        override fun onItemClick(viewType: Int, view: View, adapterPos: Int) {
            singleSelectedPos.set(adapterPos)
        }
    }

    init {
        for (i in 1..5) {
            clickableList.add("Clickable List $i")
        }
    }
}