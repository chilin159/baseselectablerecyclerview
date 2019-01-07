package com.example.chilin.baseselectableadapterexample.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.view.View
import android.widget.TextView
import com.example.baseselectableadapter.RecyclerViewListListener
import com.example.baseselectableadapter.RecyclerViewListSelectListener
import com.example.chilin.baseselectableadapterexample.R
import com.example.chilin.baseselectableadapterexample.architecture.SingleLiveEvent
import com.example.chilin.baseselectableadapterexample.model.ClickableFooter
import com.example.chilin.baseselectableadapterexample.model.MultiContentModel
import com.example.chilin.baseselectableadapterexample.model.MultiContentSelectModel
import com.example.chilin.baseselectableadapterexample.model.MultiSelectableModel

class MainViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    val SELECTABLE_ROOT_VIEW = 0
    val UN_SELECTABLE_ROOT_VIEW = 1
    val UN_CLICKABLE_ROOT_VIEW = 2

    val showToastEvent = SingleLiveEvent<String>()
    val singleSelectRootViewChange = SingleLiveEvent<Int>()

    /**===========Clickable list=================*/
    val clickableList = ObservableArrayList<String>()
    val clickableListListener = object : RecyclerViewListListener {
        override fun onItemClick(viewType: Int, view: View, adapterPos: Int) {
            if (view is TextView) {
                showToastEvent.postValue(view.text.toString())
            }
        }
    }
    /**===========Single selected list=================*/
    var singleSelectRootViewStatus = SELECTABLE_ROOT_VIEW
    val singleSelectList = ObservableArrayList<String>()
    val singleSelectedPos = ObservableInt(-1)
    val singleSelectFooter = object : ClickableFooter() {
        override var footerText = ObservableField<String>("Show selected item")
        override var switchBtnText = ObservableField<String?>(getSingleFooterSwitchBtnText())

        override fun onBtnClick() {
            if (singleSelectedPos.get() >= 0) {
                showToastEvent.postValue(singleSelectList[singleSelectedPos.get()])
            }
        }

        override fun onSwitchBtnClick() {
            when (singleSelectRootViewStatus) {
                SELECTABLE_ROOT_VIEW -> singleSelectRootViewStatus = UN_SELECTABLE_ROOT_VIEW
                UN_SELECTABLE_ROOT_VIEW -> singleSelectRootViewStatus = UN_CLICKABLE_ROOT_VIEW
                UN_CLICKABLE_ROOT_VIEW -> singleSelectRootViewStatus = SELECTABLE_ROOT_VIEW
            }
            singleSelectRootViewChange.postValue(singleSelectRootViewStatus)
            switchBtnText.set(getSingleFooterSwitchBtnText())
            for (i in singleSelectList.indices) {
                singleSelectList[i] = getSingleListText(i + 1)
            }
        }
    }
    val singleSelectListClickListener = object : RecyclerViewListListener {
        override fun onItemClick(viewType: Int, view: View, adapterPos: Int) {
            showToastEvent.postValue("${singleSelectList[adapterPos]} is Click")
        }
    }
    val singleSelectListLIstener = object : RecyclerViewListSelectListener {
        override fun onItemSelect(viewType: Int, view: View, adapterPos: Int) {
            singleSelectedPos.set(adapterPos)
        }
    }

    private fun getSingleFooterSwitchBtnText(): String {
        return when (singleSelectRootViewStatus) {
            SELECTABLE_ROOT_VIEW -> "Set rootview unselectable"
            UN_SELECTABLE_ROOT_VIEW -> "Set rootview unclickable"
            UN_CLICKABLE_ROOT_VIEW -> "Set rootview selectable"
            else -> ""
        }
    }

    private fun getSingleListText(index: Int): String {
        return when (singleSelectRootViewStatus) {
            SELECTABLE_ROOT_VIEW -> "RootView Selectable List $index"
            UN_SELECTABLE_ROOT_VIEW -> "RootView UnSelectable List $index"
            UN_CLICKABLE_ROOT_VIEW -> "RootView UnClickable List $index"
            else -> ""
        }
    }

    /**===========Multi selected list=================*/
    val multiSelectableList = ObservableArrayList<MultiSelectableModel>()
    val multiSelectFooter = object : ClickableFooter() {
        override var footerText = ObservableField<String>("Show selected items")
        override var switchBtnText = ObservableField<String?>()

        override fun onBtnClick() {
            var text: String = ""
            for (item in multiSelectableList) {
                if (item.isSelected) {
                    text += "${item.text} , "
                }
            }
            showToastEvent.postValue("$text is selected")
        }

        override fun onSwitchBtnClick() {

        }
    }
    val multiSelectListClickListener = object : RecyclerViewListListener {
        override fun onItemClick(viewType: Int, view: View, adapterPos: Int) {
        }
    }

    val multiSelectListListener = object : RecyclerViewListSelectListener {

        override fun onItemSelect(viewType: Int, view: View, adapterPos: Int) {
            multiSelectableList[adapterPos].isSelected = !multiSelectableList[adapterPos].isSelected
        }
    }

    /**===========Clickable selected list=================*/
    val clickableSelectList = ObservableArrayList<String>()
    val clickableSelectedPos = ObservableInt(-1)
    val clickableSelectFooter = object : ClickableFooter() {
        override var footerText = ObservableField<String>("Show selected item")
        override var switchBtnText = ObservableField<String?>()

        override fun onBtnClick() {
            if (clickableSelectedPos.get() >= 0) {
                showToastEvent.postValue(clickableSelectList[clickableSelectedPos.get()])
            }
        }

        override fun onSwitchBtnClick() {

        }
    }
    val clickableSelectListClickListener = object : RecyclerViewListListener {
        override fun onItemClick(viewType: Int, view: View, adapterPos: Int) {
            when (view.id) {
                R.id.clickable_item -> showToastEvent.postValue("${clickableSelectList[adapterPos]} is Click")
            }
        }
    }

    val clickableSelectListListener = object : RecyclerViewListSelectListener {

        override fun onItemSelect(viewType: Int, view: View, adapterPos: Int) {
            clickableSelectedPos.set(adapterPos)
        }
    }

    /**===========multi content list=================*/
    val multiContentList = ObservableArrayList<MultiContentModel>()
    val multiContentSelectedPos = ObservableInt(-1)
    val multiContentListClickListener = object : RecyclerViewListListener {
        override fun onItemClick(viewType: Int, view: View, adapterPos: Int) {
            showToastEvent.postValue(multiContentList[adapterPos].title)
        }
    }

    val multiContentListListener = object : RecyclerViewListSelectListener {

        override fun onItemSelect(viewType: Int, view: View, adapterPos: Int) {
            multiContentSelectedPos.set(adapterPos)
        }
    }

    val multiContentSelectFooter = object : ClickableFooter() {
        override var footerText = ObservableField<String>("Show selected item")
        override var switchBtnText = ObservableField<String?>()

        override fun onBtnClick() {
            if (multiContentSelectedPos.get() >= 0) {
                showToastEvent.postValue(multiContentList[multiContentSelectedPos.get()].title)
            }
        }

        override fun onSwitchBtnClick() {

        }
    }

    private fun initMultiContentList() {
        multiContentList.clear()
        multiContentList.add(MultiContentModel("Multi content 1"))
        multiContentList.add(MultiContentSelectModel("Multi content 2"))
        multiContentList.add(MultiContentModel("Multi content 3"))
        multiContentList.add(MultiContentSelectModel("Multi content 4"))
        multiContentList.add(MultiContentSelectModel("Multi content 5"))
    }

    init {
        for (i in 1..5) {
            clickableList.add("Clickable List $i")
            singleSelectList.add(getSingleListText(i))
            multiSelectableList.add(MultiSelectableModel("Multi list $i").apply {
                isSelected = false
            })
            clickableSelectList.add("item clickable list $i")
            initMultiContentList()
        }
    }

}