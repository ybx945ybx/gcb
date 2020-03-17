package com.gocashback.module_discover.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.network.model.index.IndexAdvertItemModel
import com.gocashback.module_discover.R

/**
 * @Author 55HAITAO
 * @Date 2019/4/8 4:46 PM
 */
class DiscoverAdapter(list: List<IndexAdvertItemModel>) : BaseQuickAdapter<IndexAdvertItemModel, BaseViewHolder>(R.layout.item_discover, list) {
    override fun convert(helper: BaseViewHolder, item: IndexAdvertItemModel) {
        with(item) {
            helper.setText(R.id.tv_discover_title, banner_title)
                    .setText(R.id.tv_discover_desc, banner_title_two)
            loadImage(img_url, targetView = helper.getView(R.id.iv_discover_cover), centerCrop = true)
        }
    }


}