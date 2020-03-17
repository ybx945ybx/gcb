package com.gocashback.module_me.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.network.model.user.NoticeItemModel
import com.gocashback.lib_common.utils.dateFormat
import com.gocashback.module_me.R

/**
 * @Author 55HAITAO
 * @Date 2019-06-12 16:12
 */
class MessageSystemAdapter(list: List<NoticeItemModel>) : BaseQuickAdapter<NoticeItemModel, BaseViewHolder>(R.layout.item_message_system, list) {
    override fun convert(helper: BaseViewHolder, item: NoticeItemModel) {
        with(item) {
            helper.setText(R.id.tv_message_title, title)
                    .setText(R.id.tv_message_date, dateFormat(createline.toLong() * 1000))

            helper.getView<TextView>(R.id.tv_message_title).isSelected = status == 1
        }

    }
}