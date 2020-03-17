package com.gocashback.lib_common.widget

import android.app.Activity
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.gocashback.lib_common.R
import com.gocashback.lib_common.adapter.FilterTagAdapter

/**
 *
 * 顶部弹出筛选Popupwindow
 *
 * @Author 55HAITAO
 * @Date 2019-06-05 14:21
 */
class GcbFilterPopupWindow(val context: Context, private val listStatus: List<FilterTagModel>, private val listType: List<FilterTagModel>) : PopupWindow() {
    private var statusFilterTagAdapter: FilterTagAdapter
    private var typeFilterTagAdapter: FilterTagAdapter

    var onConfirmListener: OnConfirmListener? = null
    private var status = 0
    private var type = 0

    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_filter_popupwindow, null)
//        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isOutsideTouchable = true
        isTouchable = true
        this.contentView = contentView
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        update()

        contentView.findViewById<RecyclerView>(R.id.rycv_status).apply {
            layoutManager = GridLayoutManager(context, 3)
            statusFilterTagAdapter = FilterTagAdapter(listStatus)
            statusFilterTagAdapter.setOnItemClickListener { _, _, position ->
                doTagClick(statusFilterTagAdapter, position)
                status = statusFilterTagAdapter.data[position].tagTypeValue
            }
            adapter = statusFilterTagAdapter
        }

        contentView.findViewById<RecyclerView>(R.id.rycv_type).apply {
            layoutManager = GridLayoutManager(context, 3)
            typeFilterTagAdapter = FilterTagAdapter(listType)
            typeFilterTagAdapter.setOnItemClickListener { _, _, position ->
                doTagClick(typeFilterTagAdapter, position)
                type = typeFilterTagAdapter.data[position].tagTypeValue

            }
            adapter = typeFilterTagAdapter
        }

        contentView.findViewById<TextView>(R.id.tv_confirm).setOnClickListener {
            onConfirmListener?.onConfirm(status, type)
            dismiss()
        }
    }

    private fun doTagClick(adapter: FilterTagAdapter, position: Int) {
        adapter.apply {
            data.forEach {
                it.isSelected = false
            }
            getItem(position)?.isSelected = true
            notifyDataSetChanged()
        }

    }


    fun show(anchor: View, xoff: Int, yoff: Int) {
        context as Activity
        val lp = context.window.attributes
        lp.alpha = 0.5f //0.0-1.0
        context.window.attributes = lp

        this.showAsDropDown(anchor, xoff, yoff)
    }

    override fun dismiss() {
        super.dismiss()
        context as Activity
        val lp = context.window.attributes
        lp.alpha = 1.0f //0.0-1.0
        context.window.attributes = lp
    }

    interface OnConfirmListener {
        fun onConfirm(statusValue: Int, typeValue: Int)
    }

    class FilterTagModel(val tagName: String, val tagTypeValue: Int, var isSelected: Boolean = false)
}