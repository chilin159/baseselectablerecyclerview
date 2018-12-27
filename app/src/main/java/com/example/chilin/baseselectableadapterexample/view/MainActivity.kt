package com.example.chilin.baseselectableadapterexample.view

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.example.baseselectableadapter.BaseSelectableAdapter
import com.example.chilin.baseselectableadapterexample.BR
import com.example.chilin.baseselectableadapterexample.view.model.MultiSelectableModel
import com.example.chilin.baseselectableadapterexample.R
import com.example.chilin.baseselectableadapterexample.databinding.ActivityMainBinding
import com.example.chilin.baseselectableadapterexample.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java).apply {
            observe(showToastEvent) { text ->
                text?.let {
                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                }
            }
            observe(singleSelectRootViewChange) { changeTo ->
                if (binding.singleSelectList.adapter is BaseSelectableAdapter<*>) {
                    changeTo?.let {
                        (binding.singleSelectList.adapter as BaseSelectableAdapter<*>).setRootViewStatus(it)
                    }
                }
            }
        }

        binding.run {
            viewmodel = viewModel

            clickableList.run {
                setBaseSelectableAdapter<String>(BR.data, R.layout.item_clickable) {
                    addHeaderRes(BR.data, R.layout.header, "Clickable list")
                    setAdapterItemClickListener(viewModel.clickableListListener)
                }
            }

            singleSelectList.run {
                setBaseSelectableAdapter<String>(BR.data, R.layout.item_selectable) {
                    addHeaderRes(BR.data, R.layout.header, "Single select list")
                    setSelectableRes(BaseSelectableAdapter.SINGLE_SELECT, BR.select)
                    setAdapterItemClickListener(viewModel.singleSelectListClickListener)
                    setAdapterItemSelectListener(viewModel.singleSelectListLIstener)
                    setHasFooter(BR.footer, R.layout.footer_selectable, viewModel.singleSelectFooter)
                    addSelectableItemId(R.id.radio_btn)
                    setRootViewStatus(viewModel.singleSelectRootViewStatus)
                }
            }

            multiSelectList.run {
                setBaseSelectableAdapter<MultiSelectableModel>(BR.data, R.layout.item_multi_selectable) {
                    addHeaderRes(BR.data, R.layout.header, "Multi select list")
                    setSelectableRes(BaseSelectableAdapter.MULTI_SELECT, BR.select)
                    setAdapterItemClickListener(viewModel.multiSelectListClickListener)
                    setAdapterItemSelectListener(viewModel.multiSelectListListener)
                    setHasFooter(BR.footer, R.layout.footer_selectable, viewModel.multiSelectFooter)
                }
            }

            clickableSelectList.run {
                setBaseSelectableAdapter<String>(BR.data, R.layout.item_clickable_select) {
                    addHeaderRes(BR.data, R.layout.header, "Item clickable select list")
                    setSelectableRes(BaseSelectableAdapter.SINGLE_SELECT, BR.select)
                    setAdapterItemClickListener(viewModel.clickableSelectListClickListener)
                    setAdapterItemSelectListener(viewModel.clickableSelectListListener)
                    addClickableItemId(R.id.clickable_item)
                    setHasFooter(BR.footer, R.layout.footer_selectable, viewModel.clickableSelectFooter)
                }
            }
        }

        lifecycle.addObserver(viewModel)
    }

    private fun <T> BaseSelectableAdapter<T>.setRootViewStatus(style: Int) {
        when (style) {
            viewModel.SELECTABLE_ROOT_VIEW -> setRootViewStatus(true, true)
            viewModel.UN_SELECTABLE_ROOT_VIEW -> setRootViewStatus(true, false)
            viewModel.UN_CLICKABLE_ROOT_VIEW -> setRootViewStatus(false, false)
        }
    }

    inline fun <T> FragmentActivity.observe(data: LiveData<T>, crossinline action: (T?) -> Unit) {
        data.observe(this, Observer { it -> action(it) })
    }

    fun <T> RecyclerView.setBaseSelectableAdapter(br: Int, @LayoutRes layout: Int, init: BaseSelectableAdapter<T>.() -> Unit) {
        adapter = BaseSelectableAdapter<T>(br, layout).apply { init() }
    }
}
