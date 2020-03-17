package com.gocashback.module_me.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.network.model.user.RebateRecordItemModel
import com.gocashback.lib_common.utils.moneyFormat
import com.gocashback.lib_common.widget.GcbStatusView
import com.gocashback.lib_common.widget.GcbStatusView.StatusCategory.Companion.REBATE
import com.gocashback.module_me.R

/**
 * @Author 55HAITAO
 * @Date 2019-06-04 19:20
 */
class MyRebateAdapter(private val context: Context, list: List<RebateRecordItemModel>) : BaseQuickAdapter<RebateRecordItemModel, BaseViewHolder>(R.layout.item_my_rebate, list) {

    override fun convert(helper: BaseViewHolder, item: RebateRecordItemModel) {
        with(item) {
            when (type) { // 1订单返利，2朋友订单返利，3注册奖励，4邀请奖励，6活动奖励，7线下活动
                1 -> helper.setImageResource(R.id.iv_rebate_type, R.mipmap.ic_rebate_type_order)
                        .setText(R.id.tv_rebate_type, context.getString(R.string.rebate_type_order))

                2 -> helper.setImageResource(R.id.iv_rebate_type, R.mipmap.ic_rebate_type_cash)
                        .setText(R.id.tv_rebate_type, context.getString(R.string.rebate_type_friend_order))

                3 -> helper.setImageResource(R.id.iv_rebate_type, R.mipmap.ic_rebate_type_register)
                        .setText(R.id.tv_rebate_type, context.getString(R.string.rebate_type_register))

                4 -> helper.setImageResource(R.id.iv_rebate_type, R.mipmap.ic_rebate_type_invite)
                        .setText(R.id.tv_rebate_type, context.getString(R.string.rebate_type_invite))

                6 -> helper.setImageResource(R.id.iv_rebate_type, R.mipmap.ic_rebate_type_activity)
                        .setText(R.id.tv_rebate_type, context.getString(R.string.rebate_type_activity))

                7 -> helper.setImageResource(R.id.iv_rebate_type, R.mipmap.ic_rebate_type_underline)
                        .setText(R.id.tv_rebate_type, context.getString(R.string.rebate_type_offline))

            }
            helper.setText(R.id.tv_rebate_date, dateline)
                    .setText(R.id.tv_rebate_amount, moneyFormat(amount))
            helper.getView<GcbStatusView>(R.id.gsv_rebate).setViewStatus(REBATE, status)
        }

    }
}