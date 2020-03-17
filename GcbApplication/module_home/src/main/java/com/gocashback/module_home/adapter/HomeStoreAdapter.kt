package com.gocashback.module_home.adapter

import android.app.Activity
import android.graphics.Paint
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.network.model.deal.DealItemModel
import com.gocashback.lib_common.network.model.store.StoreItemModel
import com.gocashback.lib_common.utils.moneyFormat
import com.gocashback.lib_common.widget.GcbGoBuyView
import com.gocashback.module_home.R


/**
 * 首页热门商家列表适配器
 *
 * @Author 55HAITAO
 * @Date 2019-05-14 15:31
 */
class HomeStoreAdapter(private val mActivity: Activity?, list: List<StoreItemModel>) : BaseQuickAdapter<StoreItemModel, BaseViewHolder>(R.layout.item_hot_store, list) {

    override fun convert(helper: BaseViewHolder, item: StoreItemModel) {
        helper.setVisible(R.id.view_line, helper.adapterPosition != data.size) // 有头布局
        with(item) {
            helper.setText(R.id.tv_store_name, name)
                    .setText(R.id.tv_store_cash_back, rebate)
                    .addOnClickListener(R.id.left_discount)
                    .addOnClickListener(R.id.right_discount)
                    .addOnClickListener(R.id.iv_store_cover)
                    .addOnClickListener(R.id.tv_store_name)
                    .addOnClickListener(R.id.tv_store_cash_back)
//                    .addOnClickListener(R.id.tv_go_shop)
            // gobuy
            helper.getView<GcbGoBuyView>(R.id.tv_go_shop).apply {
                gotobuyUrl = gotobuy_url
                activity = mActivity
            }

            loadImage(originUrl = store_logo, targetView = helper.getView(R.id.iv_store_cover))

            if (deal.isNullOrEmpty()) {
                helper.setGone(R.id.left_discount, false)
                helper.setGone(R.id.right_discount, false)

            } else {
                val deals: List<DealItemModel> = deal as List<DealItemModel>
                if (deals.size > 1) {
                    helper.setVisible(R.id.left_discount, true)
                    helper.getView<View>(R.id.left_discount).run {
                        findViewById<TextView>(R.id.tv_discount_title).text = deals[0].title
                        if (java.lang.Float.valueOf(deals[0].sale_price) > 0) {
                            findViewById<TextView>(R.id.tv_discount_price).apply {
                                text = moneyFormat(deals[0].sale_price, deals[0].currency)
                                visibility = View.VISIBLE
                            }

                            findViewById<TextView>(R.id.tv_discount_origin_price).apply {
                                text = moneyFormat(deals[0].was_price, deals[0].currency)
                                paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
                                visibility = View.VISIBLE
                            }
                            findViewById<TextView>(R.id.tv_discount_discount).apply {
                                visibility = View.GONE
                            }
                        } else {
                            findViewById<TextView>(R.id.tv_discount_price).apply {
                                visibility = View.GONE
                            }

                            findViewById<TextView>(R.id.tv_discount_origin_price).apply {
                                visibility = View.GONE
                            }
                            findViewById<TextView>(R.id.tv_discount_discount).apply {
                                visibility = View.VISIBLE
                                text = deals[0].discount
                            }
                        }
                    }
                    loadImage(originUrl = deals[0].img, targetView = helper.getView<View>(R.id.left_discount).findViewById(R.id.iv_discount_cover))

                    helper.setVisible(R.id.right_discount, true)
                    helper.getView<View>(R.id.right_discount).run {
                        findViewById<TextView>(R.id.tv_discount_title).text = deals[1].title
                        if (java.lang.Float.valueOf(deals[1].sale_price) > 0) {
                            findViewById<TextView>(R.id.tv_discount_price).apply {
                                text = moneyFormat(deals[1].sale_price, deals[1].currency)
                                visibility = View.VISIBLE
                            }

                            findViewById<TextView>(R.id.tv_discount_origin_price).apply {
                                text = moneyFormat(deals[1].was_price, deals[1].currency)
                                paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
                                visibility = View.VISIBLE
                            }
                            findViewById<TextView>(R.id.tv_discount_discount).apply {
                                visibility = View.GONE
                            }
                        } else {
                            findViewById<TextView>(R.id.tv_discount_price).apply {
                                visibility = View.GONE
                            }

                            findViewById<TextView>(R.id.tv_discount_origin_price).apply {
                                visibility = View.GONE
                            }
                            findViewById<TextView>(R.id.tv_discount_discount).apply {
                                visibility = View.VISIBLE
                                text = deals[1].discount
                            }
                        }
                    }
                    loadImage(originUrl = deals[1].img, targetView = helper.getView<View>(R.id.right_discount).findViewById(R.id.iv_discount_cover))

                } else {

                    helper.setVisible(R.id.left_discount, true)
                    loadImage(originUrl = deals[0].img, targetView = helper.getView<View>(R.id.left_discount).findViewById(R.id.iv_discount_cover))
                    helper.getView<View>(R.id.left_discount).run {
                        findViewById<TextView>(R.id.tv_discount_title).text = deals[0].title
                        if (java.lang.Float.valueOf(deals[0].sale_price) > 0) {
                            findViewById<TextView>(R.id.tv_discount_price).apply {
                                text = moneyFormat(deals[0].sale_price, deals[0].currency)
                                visibility = View.VISIBLE
                            }

                            findViewById<TextView>(R.id.tv_discount_origin_price).apply {
                                text = moneyFormat(deals[0].was_price, deals[0].currency)
                                paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
                                visibility = View.VISIBLE
                            }
                            findViewById<TextView>(R.id.tv_discount_discount).apply {
                                visibility = View.GONE
                            }
                        } else {
                            findViewById<TextView>(R.id.tv_discount_price).apply {
                                visibility = View.GONE
                            }

                            findViewById<TextView>(R.id.tv_discount_origin_price).apply {
                                visibility = View.GONE
                            }
                            findViewById<TextView>(R.id.tv_discount_discount).apply {
                                visibility = View.VISIBLE
                                text = deals[0].discount
                            }
                        }
                    }
                    helper.setVisible(R.id.right_discount, false)

                }

            }


        }
    }
}