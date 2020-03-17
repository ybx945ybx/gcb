package com.gocashback.module_home.adapter

import android.app.Activity
import android.graphics.Paint
import android.text.TextUtils
import android.widget.TextView
import com.bigkoo.convenientbanner.utils.ScreenUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.network.model.deal.DealItemModel
import com.gocashback.lib_common.utils.moneyFormat
import com.gocashback.module_home.R

/**
 * @Author 55HAITAO
 * @Date 2019-05-16 11:52
 */
class HomeDealHotAdapter(var activity: Activity?, list: List<DealItemModel>) : BaseQuickAdapter<DealItemModel, BaseViewHolder>(R.layout.card_discount, list) {
    override fun convert(helper: BaseViewHolder, item: DealItemModel) {
        with(item) {
            helper.setText(R.id.tv_discount_title, title)
                    .setText(R.id.tv_discount_brand, store)
                    .setText(R.id.tv_discount_price, moneyFormat(sale_price, currency))
            helper.getView<TextView>(R.id.tv_discount_origin_price).apply {
                text = moneyFormat(was_price, currency)
                paint.flags = Paint.STRIKE_THRU_TEXT_FLAG

            }

            loadImage(originUrl = if (TextUtils.isEmpty(img)) "" else img, targetView = helper.getView(R.id.iv_discount_cover), radius = 2)
        }

        helper.itemView.apply {
            layoutParams.width = ScreenUtil.dip2px(activity, 140f)
        }

    }
}