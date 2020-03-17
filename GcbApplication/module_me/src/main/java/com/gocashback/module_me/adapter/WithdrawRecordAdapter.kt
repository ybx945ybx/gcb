package com.gocashback.module_me.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.network.model.user.WithdrawRecordItemModel
import com.gocashback.lib_common.widget.GcbStatusView
import com.gocashback.module_me.R

/**
 * @Author 55HAITAO
 * @Date 2019-06-11 19:15
 */
class WithdrawRecordAdapter(list: List<WithdrawRecordItemModel>, private val context: Context) : BaseQuickAdapter<WithdrawRecordItemModel, BaseViewHolder>(R.layout.item_withdraw_recorder, list) {
    override fun convert(helper: BaseViewHolder, item: WithdrawRecordItemModel) {
        with(item) {
            when (pay_type) { //   	0全部 1paypal 3支票 5支付宝 6礼品卡
                1 -> helper.setImageResource(R.id.iv_withdraw_pay_type, R.mipmap.ic_pay_type_paypal)
                        .setText(R.id.tv_withdraw_title, context.getString(R.string.withdraw_recorder_item_paypal))

                3 -> helper.setImageResource(R.id.iv_withdraw_pay_type, R.mipmap.ic_pay_type_check)
                        .setText(R.id.tv_withdraw_title, context.getString(R.string.withdraw_recorder_item_check))

                5 -> helper.setImageResource(R.id.iv_withdraw_pay_type, R.mipmap.ic_pay_type_ali)
                        .setText(R.id.tv_withdraw_title, context.getString(R.string.withdraw_recorder_item_alipay))

                6 -> helper.setImageResource(R.id.iv_withdraw_pay_type, R.mipmap.ic_pay_type_gift)
                        .setText(R.id.tv_withdraw_title, context.getString(R.string.withdraw_recorder_item_gift))

            }
            helper.setText(R.id.tv_withdraw_date, dateline)
                    .setText(R.id.tv_withdraw_amount, amount)
            helper.getView<GcbStatusView>(R.id.gsv_withdraw).setViewStatus(GcbStatusView.StatusCategory.WITHDRAW, status)

        }
    }
}