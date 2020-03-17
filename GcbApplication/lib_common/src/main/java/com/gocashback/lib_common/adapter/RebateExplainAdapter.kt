package com.gocashback.lib_common.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.R

/**
 * @Author 55HAITAO
 * @Date 2019-06-28 17:14
 */
class RebateExplainAdapter(list: List<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_rebate_explain, list) {
    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.tv_rebate, item)

    }
}