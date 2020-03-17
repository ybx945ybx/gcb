package com.gocashback.module_search

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @Author 55HAITAO
 * @Date 2019/4/9 7:17 PM
 */
class SearchResultAdapter(list: MutableList<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_search_filter, list) {
    override fun convert(helper: BaseViewHolder?, item: String?) {


    }
}