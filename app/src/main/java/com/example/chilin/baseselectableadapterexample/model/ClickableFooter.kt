package com.example.chilin.baseselectableadapterexample.model

import android.databinding.ObservableField

abstract class ClickableFooter {
    abstract var footerText: ObservableField<String>
    abstract var switchBtnText: ObservableField<String?>
    abstract fun onBtnClick()
    abstract fun onSwitchBtnClick()
}