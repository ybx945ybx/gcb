package com.gocashback.module_me.adapter

import android.content.Context
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.network.model.user.HeliPaymentIfModel
import com.gocashback.module_me.R

/**
 * @Author 55HAITAO
 * @Date 2020-02-17 16:02
 */
class PaymentMethodHeliAdapter(list: List<HeliPaymentIfModel>) : BaseQuickAdapter<HeliPaymentIfModel, BaseViewHolder>(R.layout.item_payment_method_heli, list) {
    override fun convert(helper: BaseViewHolder, item: HeliPaymentIfModel) {
        with(item) {
            helper.setText(R.id.tv_payment, name)
                    .setText(R.id.tv_payment_account, bank_account_no)
                    .addOnClickListener(R.id.iv_more)

            loadImage(originUrl = icon, targetView = helper.getView(R.id.iv_payment))
        }
    }
}