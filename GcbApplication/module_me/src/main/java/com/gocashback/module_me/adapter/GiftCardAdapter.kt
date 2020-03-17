package com.gocashback.module_me.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.network.model.giftCard.GiftCardItemModel
import com.gocashback.module_me.R

/**
 * @Author 55HAITAO
 * @Date 2019-06-17 11:29
 */
class GiftCardAdapter(list: List<GiftCardItemModel>) : BaseQuickAdapter<GiftCardItemModel, BaseViewHolder>(R.layout.item_gift_card, list) {
    override fun convert(helper: BaseViewHolder, item: GiftCardItemModel) {
        with(item) {
            helper.setText(R.id.tv_brand_name, brand_name)
            helper.setText(R.id.tv_bonus_percent, bonus_percent)

            loadImage(originUrl = image_url, targetView = helper.getView(R.id.iv_img))
        }
    }
}