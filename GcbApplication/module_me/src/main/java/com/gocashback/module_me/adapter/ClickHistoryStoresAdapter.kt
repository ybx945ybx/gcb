package com.gocashback.module_me.adapter

import android.app.Activity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.network.model.user.BrowsingHistoryItemModel
import com.gocashback.lib_common.widget.GcbGoBuyView
import com.gocashback.module_me.R

/**
 * @Author 55HAITAO
 * @Date 2019-06-13 10:11
 */
class ClickHistoryStoresAdapter(list: List<BrowsingHistoryItemModel>, private val mActivity: Activity?) : BaseQuickAdapter<BrowsingHistoryItemModel, BaseViewHolder>(R.layout.item_click_history_stores, list) {
    override fun convert(helper: BaseViewHolder, item: BrowsingHistoryItemModel) {
        with(item) {
            helper.setText(R.id.tv_store_name, name)
                    .setText(R.id.tv_store_cash_back, rebate)
                    .setText(R.id.tv_store_favourites, collect_num + " " + mActivity!!.resources.getString(R.string.store_collect))
//                    .addOnClickListener(R.id.iv_select)
            // gobuy
            helper.getView<GcbGoBuyView>(R.id.tv_go_shop).apply {
                gotobuyUrl = gotobuy_url
                activity = mActivity
            }

            loadImage(originUrl = store_logo, targetView = helper.getView(R.id.iv_store_cover))
            loadImage(originUrl = country_img, targetView = helper.getView(R.id.iv_store_country))

        }

    }
}