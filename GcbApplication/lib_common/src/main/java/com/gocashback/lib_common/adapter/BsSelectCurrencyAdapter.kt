package com.gocashback.lib_common.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.R
import com.gocashback.lib_common.model.CurrencyItemModel

/**
 * @Author 55HAITAO
 * @Date 2019-06-28 17:14
 */
class BsSelectCurrencyAdapter(list: List<CurrencyItemModel>) : BaseQuickAdapter<CurrencyItemModel, BaseViewHolder>(R.layout.item_bs_select_currency, list) {
    override fun convert(helper: BaseViewHolder, item: CurrencyItemModel) {
        with(item) {
            helper.setText(R.id.tv_currency, currency)
            helper.getView<ImageView>(R.id.iv_select).isSelected = isSelected
        }

    }

    fun setSelectPosition(position: Int) {
        data.forEach { it.isSelected = false }
        data[position].isSelected = true
        notifyDataSetChanged()
    }
}