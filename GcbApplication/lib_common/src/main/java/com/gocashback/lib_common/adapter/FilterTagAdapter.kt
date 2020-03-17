package com.gocashback.lib_common.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.R
import com.gocashback.lib_common.widget.GcbFilterPopupWindow

/**
 * @Author 55HAITAO
 * @Date 2019-06-05 14:40
 */
class FilterTagAdapter(list: List<GcbFilterPopupWindow.FilterTagModel>) : BaseQuickAdapter<GcbFilterPopupWindow.FilterTagModel, BaseViewHolder>(R.layout.item_filter_popupwindow_tag, list) {
    override fun convert(helper: BaseViewHolder, item: GcbFilterPopupWindow.FilterTagModel) {
        with(item) {
            helper.setText(R.id.tv_filter_tag_name, tagName)
            helper.getView<TextView>(R.id.tv_filter_tag_name).isSelected = isSelected
        }

    }
}