package com.gocashback.module_me.adapter

import android.app.Activity
import android.graphics.Paint
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.network.model.user.CollectionItemModel
import com.gocashback.lib_common.utils.dateFormat
import com.gocashback.lib_common.utils.moneyFormat
import com.gocashback.lib_common.widget.GcbGoBuyView
import com.gocashback.module_me.R
import com.gocashback.module_me.event.CheckAllSelectEvent
import org.greenrobot.eventbus.EventBus

/**
 * @Author 55HAITAO
 * @Date 2019-06-12 17:39
 */
class MyCollectionDealsAdapter(private val mActivity: Activity?, list: List<CollectionItemModel>, private var editable: Boolean = false) : BaseQuickAdapter<CollectionItemModel, BaseViewHolder>(R.layout.item_my_collection_deals, list) {
    override fun convert(helper: BaseViewHolder, item: CollectionItemModel) {
        with(item) {
            helper.setText(R.id.tv_discount_country, store)
                    .setText(R.id.tv_discount_title, title)
                    .setText(R.id.tv_discount_code, coupon_code)
                    .setText(R.id.tv_discount_off, discount)
                    .setText(R.id.tv_rebate, store_info?.rebate)
                    .setGone(R.id.iv_select, editable)
                    .setText(R.id.tv_sale_price, moneyFormat(sale_price))
                    .setText(R.id.tv_was_price, moneyFormat(was_price))
                    .setGone(R.id.tv_sale_price, java.lang.Float.valueOf(sale_price) > 0)
                    .setGone(R.id.tv_was_price, java.lang.Float.valueOf(was_price) > 0)

            helper.getView<TextView>(R.id.tv_was_price).paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            // end_time
            val endTime = end_time?.time ?: 0
            val timePattern = "yyyy-MM-dd"
            if (endTime > 0) {  // 有有效期
                helper.setText(R.id.tv_discount_limit, dateFormat(endTime * 1000, timePattern, true, mActivity))
                        .setVisible(R.id.tv_discount_limit, true)

                // 是否过期
                if (System.currentTimeMillis() / 1000 > end_time?.time ?: 0L) {  // 已过期
                    helper.getView<GcbGoBuyView>(R.id.tv_go_shop).apply {
                        setInvalid(false)
                        gotobuyUrl = gotobuy_url
                        activity = mActivity
                        coupon_code = item.coupon_code
                    }
                    helper.getView<TextView>(R.id.tv_sale_price).isEnabled = false
                    helper.getView<TextView>(R.id.tv_was_price).isEnabled = false
                    helper.getView<TextView>(R.id.tv_discount_title).isEnabled = false
                    helper.getView<TextView>(R.id.tv_discount_off).isEnabled = false
                    helper.getView<TextView>(R.id.tv_rebate).isEnabled = false

                } else {    // 未过期
                    helper.getView<GcbGoBuyView>(R.id.tv_go_shop).apply {
                        setInvalid(true)
                        gotobuyUrl = gotobuy_url
                        activity = mActivity
                        coupon_code = item.coupon_code
                        isEnabled = !editable
                    }
                    helper.getView<TextView>(R.id.tv_sale_price).isEnabled = true
                    helper.getView<TextView>(R.id.tv_was_price).isEnabled = true
                    helper.getView<TextView>(R.id.tv_discount_title).isEnabled = true
                    helper.getView<TextView>(R.id.tv_discount_off).isEnabled = true
                    helper.getView<TextView>(R.id.tv_rebate).isEnabled = true
                }

            } else {     // 无有效期
                helper.setVisible(R.id.tv_discount_limit, false)
                helper.getView<GcbGoBuyView>(R.id.tv_go_shop).apply {
                    setInvalid(true)
                    gotobuyUrl = gotobuy_url
                    activity = mActivity
                    coupon_code = item.coupon_code
                    isEnabled = !editable
                }
                helper.getView<TextView>(R.id.tv_sale_price).isEnabled = true
                helper.getView<TextView>(R.id.tv_was_price).isEnabled = true
                helper.getView<TextView>(R.id.tv_discount_title).isEnabled = true
                helper.getView<TextView>(R.id.tv_discount_off).isEnabled = true
                helper.getView<TextView>(R.id.tv_rebate).isEnabled = true
            }

            // coupon_code
            helper.getView<android.support.constraint.Group>(R.id.group_code).apply {
                visibility = if (TextUtils.isEmpty(coupon_code)) View.GONE else View.VISIBLE
            }

            loadImage(originUrl = img, targetView = helper.getView(R.id.iv_discount_cover))
            store_info?.let {
                loadImage(originUrl = it.country_img, targetView = helper.getView(R.id.iv_discount_country))
            }

            helper.getView<ImageView>(R.id.iv_select).apply {
                isSelected = select
                setOnClickListener {
                    select = !it.isSelected
                    notifyDataSetChanged()
                    checkAllSelect()
                }
            }
        }

    }


    /**
     * 每次点选以后检查是否是全选，同步底部全选按钮
     */
    private fun checkAllSelect() {
        EventBus.getDefault().post(CheckAllSelectEvent())
    }

    fun setEditable(editable: Boolean) {
        this.editable = editable
        notifyDataSetChanged()
    }

    fun getEditable() = editable

    fun setSelectPosition(position: Int) {
        data[position].select = !data[position].select
        notifyItemChanged(position)
        checkAllSelect()
    }

    fun setAllSelect(allSelect: Boolean) {
        data.forEach { it.select = allSelect }
        notifyDataSetChanged()
    }

    fun getSelectedIds(): String {
        var ids = ""
        data.forEach {
            if (it.select) {
                ids = if (ids.isEmpty()) {
                    it.collect_id
                } else {
                    ids + "," + it.collect_id
                }
            }
        }

        return ids
    }

}