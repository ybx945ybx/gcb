package com.gocashback.module_home.adapter

import android.app.Activity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.language.LOCALE_ENGLISH
import com.gocashback.lib_common.language.getLanguage
import com.gocashback.lib_common.network.model.store.StoreItemModel
import com.gocashback.module_home.R

/**
 * @Author 55HAITAO
 * @Date 2019-05-15 14:16
 */
class HomeStoreRecommendAdapter(private val activity: Activity?, list: List<StoreItemModel>) : BaseQuickAdapter<StoreItemModel, BaseViewHolder>(R.layout.card_store, list) {

    override fun convert(helper: BaseViewHolder, item: StoreItemModel) {
        with(item) {
            helper.setText(R.id.tv_store_name, name)
                    .setText(R.id.tv_store_cash_back, if (LOCALE_ENGLISH == getLanguage(activity) && is_upto == 1) rebate.replace("Cash Back", "") else rebate)

            loadImage(originUrl = if (luxury_logo.isEmpty() || "false" == luxury_logo) store_logo else luxury_logo, targetView = helper.getView(R.id.iv_store_cover))
        }

    }
}