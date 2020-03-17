package com.gocashback.module_me.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.network.getUser
import com.gocashback.lib_common.network.model.giftCard.GiftCardDetailFaceIfModel
import com.gocashback.module_me.R

/**
 * @Author 55HAITAO
 * @Date 2019-06-17 19:36
 */
class GiftCardDetailAmountSelectAdapter(list: List<GiftCardDetailFaceIfModel>) : BaseQuickAdapter<GiftCardDetailFaceIfModel, BaseViewHolder>(R.layout.item_amount_select, list) {
    override fun convert(helper: BaseViewHolder, item: GiftCardDetailFaceIfModel) {
        with(item) {

            helper.getView<TextView>(R.id.tv_amount).apply {
                text = face_value

                if (java.lang.Float.valueOf(getUser()?.available
                                ?: "0") > java.lang.Float.valueOf(face_value)) {
                    isEnabled = true
                    isSelected = selected
                } else {
                    isEnabled = false
                    isSelected = false
                }
            }
        }
    }

    fun setSelectPosition(position: Int) {
        data.forEach {
            it.selected = false
        }
        data[position].selected = true
        notifyDataSetChanged()
    }
}