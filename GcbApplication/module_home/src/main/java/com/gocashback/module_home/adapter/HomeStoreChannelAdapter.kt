package com.gocashback.module_home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.network.model.index.IndexChannelItemModel
import com.gocashback.module_home.R

/**
 * @Author 55HAITAO
 * @Date 2019-05-15 14:00
 */
class HomeStoreChannelAdapter(list: List<IndexChannelItemModel>) : BaseQuickAdapter<IndexChannelItemModel, BaseViewHolder>(R.layout.item_home_store_channel, list) {
    override fun convert(helper: BaseViewHolder, item: IndexChannelItemModel) {
        helper.setText(R.id.tv_channel_text, item.name)
        loadImage(originUrl = item.img, targetView = helper.getView(R.id.iv_channel_logo))

    }
}