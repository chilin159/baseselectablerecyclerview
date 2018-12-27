package com.example.baseselectableadapter

abstract class SelectableModel {
    private var mIsSelected: Boolean? = null

    var isSelected: Boolean
        get() = mIsSelected!!
        set(isSelected) {
            mIsSelected = isSelected
        }
}
