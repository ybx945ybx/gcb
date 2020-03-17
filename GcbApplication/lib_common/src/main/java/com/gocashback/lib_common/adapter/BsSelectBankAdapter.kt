package com.gocashback.lib_common.adapter

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.R
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.language.LOCALE_CHINESE
import com.gocashback.lib_common.language.getLanguage
import com.gocashback.lib_common.model.CurrencyItemModel
import com.gocashback.lib_common.network.model.index.IndexHelipayBankItemModel

/**
 * @Author 55HAITAO
 * @Date 2019-06-28 17:14
 */
class BsSelectBankAdapter(list: List<IndexHelipayBankItemModel>) : BaseQuickAdapter<IndexHelipayBankItemModel, BaseViewHolder>(R.layout.item_bs_select_bank, list) {
    override fun convert(helper: BaseViewHolder, item: IndexHelipayBankItemModel) {
        with(item) {
            helper.setText(R.id.tv_bank, if (getLanguage(mContext) == LOCALE_CHINESE) name else name_en)
            loadImage(originUrl = icon, targetView = helper.getView(R.id.iv_bank))
            helper.getView<ImageView>(R.id.iv_select).visibility = if (isSelected) View.VISIBLE else View.GONE
        }

    }

    fun setSelectPosition(position: Int) {
        data.forEach { it.isSelected = false }
        data[position].isSelected = true
        notifyDataSetChanged()
    }
}