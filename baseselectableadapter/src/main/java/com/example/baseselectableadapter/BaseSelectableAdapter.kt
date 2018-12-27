package com.example.baseselectableadapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

class BaseSelectableAdapter<T>(@IdRes bindingVariable: Int, @LayoutRes res: Int) : RecyclerView.Adapter<BaseBindingHolder>() {
    private val PAYLAOD_SELECTED = "PAYLOAD_SELECTED"

    private val mData = ArrayList<T>()
    @IdRes
    private var mBindingVariable = 0
    @LayoutRes
    private var mRes = 0

    private val mHeaderData = ArrayList<Any?>()
    @IdRes
    private val mHeaderBindingVariable = ArrayList<Int>()
    @LayoutRes
    private val mHeaderRes = ArrayList<Int>()
    private var mViewTypeForOutside: MutableList<Int> = ArrayList()

    private var mHasFooter = false
    @IdRes
    private var mFooterBindingVariable = 0
    @LayoutRes
    private var mFooterRes = 0
    private var mFooterData: Any? = null

    @IdRes
    private var mSelectableBindingVariable = 0
    private var mType = NO_SELECT
    private val mSelectableList = ArrayList<Boolean>()
    var selectedPosition = -1
        private set

    private var mRecyclerViewListClickListener: RecyclerViewListListener? = null
    private var mRootViewClickable = true
    private var mRootViewSelectable = true
    private val mClickableItemIdList = ArrayList<Int>()
    private val mSelectableItemIdList = ArrayList<Int>()

    private val headerCount: Int
        get() = mHeaderBindingVariable.size

    /***********************************************************************************************
     * Below are necessary methods
     */
    /**
     * mBindingVariable  is  binding  variable in resourse
     * mRes is xml resource of item
     * for example:  BaseSelectableAdapter(BR.data, R.layout.item_school);
     */
    init {
        mBindingVariable = bindingVariable
        mRes = res
    }

    /**
     * You can use BindingUtils setDataSource to call this method
     */
    fun setData(dataList: Collection<T>?) {
        if (dataList == null) {
            return
        }
        mData.clear()
        mData.addAll(dataList)

        /**
         * Setting selected states:
         * SINGLE_SELECT: need to update last position,  see setSingleSelectedPosition(int position)
         * MULTI_SELECT: dataList need to extends SelectableModel
         */
        when (mType) {
            SINGLE_SELECT -> {
                mSelectableList.clear()
                for (position in mData.indices) {
                    if (selectedPosition == position) {
                        mSelectableList.add(true)
                    } else {
                        mSelectableList.add(false)
                    }
                }
            }
            MULTI_SELECT -> {
                mSelectableList.clear()
                for (i in mData.indices) {
                    if (mData[i] is SelectableModel) {
                        val data = mData[i] as SelectableModel
                        mSelectableList.add(data.isSelected)
                    }
                }
            }
        }

        notifyDataSetChanged()
    }
    /***********************************************************************************************
     * Below are optional methods
     */
    /**
     * Set header resources , data and viewType by below methods
     * mViewTypeForOutside : you can customized header viewType in outside, just remember not to define same value as TYPE_MAIN_CONTENT for header viewtype
     * for example:
     * adapter.addHeaderRes(BR.viewModel, R.layout.holder_header_favourite_tutors, mViewModel);
     * adapter.addHeaderRes(mVBR.viewModel, R.layout.holder_header_favourite_tutors, mViewModel, HEADER_TYPE);
     */
    fun addHeaderRes(@IdRes bindingVariable: Int, @LayoutRes res: Int, data: Any?) {
        addHeaderRes(bindingVariable, res, data, TYPE_DEFAULT_HEADER)
    }

    /**
     * mViewTypeForOutside : you can customized header viewType in outside, just remember not to define the same value as TYPE_MAIN_CONTENT and TYPE_FOOTER for header viewType
     */
    fun addHeaderRes(@IdRes bindingVariable: Int, @LayoutRes res: Int, data: Any?, viewType: Int) {
        mHeaderBindingVariable.add(bindingVariable)
        mHeaderRes.add(res)
        mHeaderData.add(data)
        mViewTypeForOutside.add(viewType)
    }


    /**
     * setFooter
     */
    fun setHasFooter(@IdRes bindingVariable: Int, @LayoutRes res: Int, data: Any?) {
        mHasFooter = true
        mFooterBindingVariable = bindingVariable
        mFooterRes = res
        mFooterData = data
    }

    /**
     * You can call this function to set view selected states
     * type : SINGLE_SELECT and MULTI_SELECT
     * bindingVariable: selected status in xml
     * If you set SINGLE_SELECT, you need to set the last selected position, see setSingleSelectedPosition(int position)
     * If you set MULTI_SELECT, your data model need to extends SelectableModel and init selected states
     */
    fun setSelectableRes(type: Int, @IdRes bindingVariable: Int) {
        mType = type
        mSelectableBindingVariable = bindingVariable
    }

    /**
     * For single select, you just need to send the last selected position to init state.
     * You can use BindingUtils setSingleSelectedPosition to call this method
     */
    fun setSingleSelectedPosition(position: Int) {
        selectedPosition = if (position < 0) 0 else position
    }

    /**
     * You can call this function to listen view item click event
     */
    fun setAdapterItemClickListener(adapterItemClickListener: RecyclerViewListListener) {
        this.mRecyclerViewListClickListener = adapterItemClickListener
    }

    /**
     * Set false to make root view unClickable and unSelectable
     * To make root view clickable but unSelectable, you can just listen the click event and will not change view's selected state
     */
    fun setRootViewStatus(clickable: Boolean, selectable: Boolean) {
        mRootViewClickable = clickable
        mRootViewSelectable = selectable
    }

    /**
     * You can set an item into clickListener, it will just return view and position but won't change selected state
     */
    fun addClickableItemId(@IdRes id: Int) {
        mClickableItemIdList.add(id)
    }

    /**
     * You can set multiple items into clickListener, it will just return view and position but won't change selected state
     */
    fun setClickableItemIdList(idList: List<Int>?) {
        if (idList == null) {
            return
        }
        mClickableItemIdList.clear()
        mClickableItemIdList.addAll(idList)
    }

    /**
     * You can set an item into clickListener, it will  return view and position and change selected state
     */
    fun addSelectableItemId(@IdRes id: Int) {
        mSelectableItemIdList.add(id)
    }

    /**
     * You can set multiple items into clickListener, it will  return view and position and change selected state
     */
    fun setSelectableItemIdList(idList: List<Int>?) {
        if (idList == null) {
            return
        }
        mSelectableItemIdList.clear()
        mSelectableItemIdList.addAll(idList)
    }

    /***********************************************************************************************
     * Below are Override and private methods
     */
    private fun onClick(v: View, adapterPos: Int) {
        mRecyclerViewListClickListener?.let {
            var viewType = TYPE_MAIN_CONTENT
            if (adapterPos < headerCount) {
                viewType = mViewTypeForOutside[adapterPos]
            }
            it.onItemClick(viewType, v, adapterPos - headerCount)
        }
    }

    private fun onSelectedClick(v: View, adapterPos: Int) {
        val selectPos = adapterPos - headerCount
        if (mSelectableList.isNotEmpty()) {
            when (mType) {
                SINGLE_SELECT -> {
                    if (selectedPosition >= 0 && selectedPosition != selectPos) {
                        mSelectableList[selectedPosition] = false
                    }
                    mSelectableList[selectPos] = true
                    notifyItemChanged(adapterPos, PAYLAOD_SELECTED)
                    notifyItemChanged(selectedPosition + headerCount, PAYLAOD_SELECTED)
                    selectedPosition = selectPos
                }
                MULTI_SELECT -> {
                    mSelectableList[selectPos] = !mSelectableList[selectPos]
                    notifyItemChanged(adapterPos, PAYLAOD_SELECTED)
                }
            }
        }

        onClick(v, adapterPos)
    }


    private fun initClickListener(holder: BaseBindingHolder) {
        holder.binding?.root?.let { root ->
            if (mRootViewClickable) {
                root.setOnClickListener { v ->
                    val adapterPos = holder.adapterPosition
                    if (adapterPos != RecyclerView.NO_POSITION) {
                        if (mRootViewSelectable) {
                            onSelectedClick(v, adapterPos)
                        } else {
                            onClick(v, adapterPos)
                        }
                    }
                }
            }

            for (i in mClickableItemIdList.indices) {
                root.findViewById<View>(mClickableItemIdList[i])?.setOnClickListener { v ->
                    if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                        onClick(v, holder.adapterPosition)
                    }
                }
            }

            for (i in mSelectableItemIdList.indices) {
                root.findViewById<View>(mSelectableItemIdList[i])?.setOnClickListener { v ->
                    if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                        onSelectedClick(v, holder.adapterPosition)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            TYPE_MAIN_CONTENT -> {
                val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, mRes, parent, false)
                val holder = BaseBindingHolder.newInstance(binding)
                initClickListener(holder)
                return holder
            }
            TYPE_FOOTER -> {
                val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, mFooterRes, parent, false)
                return BaseBindingHolder.newInstance(binding)
            }
            else -> {
                val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, mHeaderRes[viewType], parent, false)
                return BaseBindingHolder.newInstance(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseBindingHolder, position: Int) {
        holder.binding?.run {
            when (getItemViewType(position)) {
                TYPE_MAIN_CONTENT -> {
                    val contentPos = position - headerCount
                    val data = mData[contentPos]
                    setVariable(mBindingVariable, data)
                    if (mType != NO_SELECT && mSelectableList.isNotEmpty()) {
                        setVariable(mSelectableBindingVariable, mSelectableList[contentPos])
                    }
                }
                TYPE_FOOTER -> {
                    setVariable(mFooterBindingVariable, mFooterData)
                }
                else -> {
                    setVariable(mHeaderBindingVariable[position], mHeaderData[position])
                }
            }
            executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: BaseBindingHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            if (payloads.contains(PAYLAOD_SELECTED)) {
                if (mType != NO_SELECT && mSelectableList.isNotEmpty()) {
                    holder.binding?.setVariable(mSelectableBindingVariable, mSelectableList[position - headerCount])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (mHasFooter) mData.size + headerCount + 1 else mData.size + headerCount
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < headerCount) {
            //return position because in onCreateViewHolder, it need correct position to call mHeaderRes.get(viewType)
            position
        } else if (mHasFooter && position == itemCount - 1) {
            TYPE_FOOTER
        } else {
            TYPE_MAIN_CONTENT
        }
    }

    companion object {
        val NO_SELECT = 0
        val SINGLE_SELECT = 1
        val MULTI_SELECT = 2
        val TYPE_DEFAULT_HEADER = -1
        val TYPE_MAIN_CONTENT = 100
        val TYPE_FOOTER = 200
    }
}
