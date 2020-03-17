package com.gocashback.module_search.adapter

import android.app.Activity
import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.network.model.store.StoreItemModel
import com.gocashback.lib_common.widget.GcbGoBuyView
import com.gocashback.module_search.R

/**
 * @Author 55HAITAO
 * @Date 2019-05-21 20:09
 */
class SearchStoreAdapter(private val mActivity: Activity?, list: List<StoreItemModel> ) : BaseQuickAdapter<StoreItemModel, BaseViewHolder>(R.layout.item_search_store, list) {
    override fun convert(helper: BaseViewHolder, item: StoreItemModel) {
        with(item) {
            helper.setText(R.id.tv_store_name, name)
                    .setText(R.id.tv_store_cash_back, rebate)
                    .setText(R.id.tv_store_favourites, collect_num + " " + mActivity!!.resources?.getString(R.string.store_collect))
                    .setVisible(R.id.view_line, data.size - 1 != helper.layoutPosition)

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