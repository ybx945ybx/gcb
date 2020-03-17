package com.gocashback.module_search

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @Author 55HAITAO
 * @Date 2019/4/9 7:16 PM
 */
class SearchFilterAdapter(list: MutableList<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_search_filter, list) {
    override fun convert(helper: BaseViewHolder, item: String?) {
        helper.setText(R.id.tv_word, item)

    }
}