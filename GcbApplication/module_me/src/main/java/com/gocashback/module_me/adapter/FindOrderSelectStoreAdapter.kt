package com.gocashback.module_me.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.network.model.user.LostOrderShowStoreItemModel
import com.gocashback.module_me.R

/**
 * @Author 55HAITAO
 * @Date 2019-06-10 20:31
 */
class FindOrderSelectStoreAdapter(list: List<LostOrderShowStoreItemModel>) : BaseQuickAdapter<LostOrderShowStoreItemModel, BaseViewHolder>(R.layout.item_select_store, list) {
    override fun convert(helper: BaseViewHolder, item: LostOrderShowStoreItemModel) {
        with(item) {
            helper.setText(R.id.tv_store_name, name)
                    .setVisible(R.id.view_line, helper.adapterPosition != itemCount-1)
            helper.getView<ImageView>(R.id.iv_select).isSelected = isSelected
        }

    }
}