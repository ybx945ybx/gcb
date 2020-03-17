package com.gocashback.module_me.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.network.model.user.OrderItemModel
import com.gocashback.lib_common.utils.dateFormat
import com.gocashback.lib_common.utils.moneyFormat
import com.gocashback.lib_common.widget.GcbStatusView
import com.gocashback.lib_common.widget.GcbStatusView.StatusCategory.Companion.FINDORDER
import com.gocashback.lib_common.widget.GcbStatusView.StatusCategory.Companion.ORDER
import com.gocashback.module_me.R

/**
 * @Author 55HAITAO
 * @Date 2019-06-10 11:03
 */
class MyOrderAdapter(list: List<OrderItemModel>, private val type: Int = 0) : BaseQuickAdapter<OrderItemModel, BaseViewHolder>(R.layout.item_my_order, list) {
    override fun convert(helper: BaseViewHolder, item: OrderItemModel) {   // type = 1 是找回订单
        with(item) {
            helper.setText(R.id.tv_store_name, store)
                    .setText(R.id.tv_order_date, if (type == 1) order_date else dateFormat(dateline * 1000))
//                    .setText(R.id.tv_order_date, dateFormat(inputtime*1000))
                    .setText(R.id.tv_order_rebate, if (type == 1) rebate else moneyFormat(rebate))
                    .setText(R.id.tv_order_amount, if (type == 0) moneyFormat(amount) else amount)

            loadImage(originUrl = store_logo, targetView = helper.getView(R.id.iv_store_cover))
            helper.getView<GcbStatusView>(R.id.gsv_order).setViewStatus(if (type == 0) ORDER else FINDORDER, status)
        }

    }
}