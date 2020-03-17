package com.gocashback.module_store.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.network.model.store.StoreDetailSpecialItemModel
import com.gocashback.module_store.R

/**
 * @Author 55HAITAO
 * @Date 2019-06-18 14:33
 */
class CashBackCateAdapter(list: List<StoreDetailSpecialItemModel>) : BaseQuickAdapter<StoreDetailSpecialItemModel, BaseViewHolder>(R.layout.item_cash_back_categories, list) {
    override fun convert(helper: BaseViewHolder, item: StoreDetailSpecialItemModel) {
        with(item) {
            helper.setText(R.id.tv_cate, cate_name)
                    .setText(R.id.tv_cate_value, cate_rebate)
                    .setVisible(R.id.iv_line, helper.layoutPosition != data.size - 1)
        }
    }
}