package com.gocashback.lib_common.adapter

import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.R
import com.gocashback.lib_common.widget.BsListDlg

/**
 * @Author 55HAITAO
 * @Date 2019-06-13 19:31
 */
class ListBsDlgAdapter(data: List<BsListDlg.ListDlgItem>?) : BaseQuickAdapter<BsListDlg.ListDlgItem, BaseViewHolder>(R.layout.item_list_dlg, data) {

    override fun convert(helper: BaseViewHolder, item: BsListDlg.ListDlgItem) {
        // 文本
        helper.setText(R.id.tv_item, item.text)
        // 图标
        val drIcon = ContextCompat.getDrawable(mContext, item.iconRes)
        helper.getView<TextView>(R.id.tv_item).setCompoundDrawablesWithIntrinsicBounds(drIcon, null, null, null)
    }
}