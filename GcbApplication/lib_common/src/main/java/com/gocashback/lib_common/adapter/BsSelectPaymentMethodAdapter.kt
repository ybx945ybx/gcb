package com.gocashback.lib_common.adapter

import android.view.View
import android.widget.ImageView
import com.ali.auth.third.core.context.KernelContext.resources
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.R
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.imageload.loadMipmapImg
import com.gocashback.lib_common.model.CurrencyItemModel
import com.gocashback.lib_common.model.interfaces.IpaymentModel
import com.gocashback.lib_common.network.model.index.IndexHelipayBankItemModel
import com.gocashback.lib_common.network.model.user.HeliPaymentIfModel
import com.gocashback.lib_common.network.model.user.PaymentIfModel
import com.gocashback.lib_common.utils.getFuzzyString

/**
 * @Author 55HAITAO
 * @Date 2019-06-28 17:14
 */
class BsSelectPaymentMethodAdapter(list: List<IpaymentModel>) : BaseQuickAdapter<IpaymentModel, BaseViewHolder>(R.layout.item_bs_select_bank, list) {
    override fun convert(helper: BaseViewHolder, item: IpaymentModel) {
        if (item is PaymentIfModel) {
            with(item) {
                if (paymentType == -1) {
                    helper.setText(R.id.tv_bank, mContext.resources.getString(R.string.withdraw_payment_method_add))
                    loadMipmapImg(R.mipmap.ic_heli_add, helper.getView(R.id.iv_bank))
                    helper.getView<ImageView>(R.id.iv_select).visibility = View.GONE

                } else {
                    if (paymentType == 1) {
                        helper.setText(R.id.tv_bank, mContext.resources.getString(R.string.payment_method_paypal)+ " · " + getFuzzyString(paypal))
                        loadMipmapImg(R.mipmap.ic_paymethed_paypal, helper.getView(R.id.iv_bank))
                    } else {
                        helper.setText(R.id.tv_bank, mContext.resources.getString(R.string.payment_method_check)+ " · " + getFuzzyString(firstname + lastname))
                        loadMipmapImg(R.mipmap.ic_paymethed_check, helper.getView(R.id.iv_bank))
                    }
                    helper.getView<ImageView>(R.id.iv_select).visibility = if (isSelected) View.VISIBLE else View.GONE
                }
            }
        }
        if (item is HeliPaymentIfModel) {
            with(item) {
                helper.setText(R.id.tv_bank, name + " · " + getFuzzyString(bank_account_no))
                loadImage(originUrl = icon, targetView = helper.getView(R.id.iv_bank))
//            helper.getView<ImageView>(R.id.iv_select).isSelected = isSelected
                helper.getView<ImageView>(R.id.iv_select).visibility = if (isSelected) View.VISIBLE else View.GONE
            }


        }

    }

    fun setSelectPosition(position: Int) {
        data.forEach {
            if (it is HeliPaymentIfModel)
                it.isSelected = false
            if (it is PaymentIfModel)
                it.isSelected = false

        }
        data[position].let {
            if (it is HeliPaymentIfModel)
                it.isSelected = true
            if (it is PaymentIfModel)
                it.isSelected = true
        }
        notifyDataSetChanged()
    }
}