@file:Suppress("UNCHECKED_CAST")

package com.gocashback.module_me.adapter

import android.text.TextUtils
import android.widget.Filter
import android.widget.Filterable
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.model.appConfig.PaypalAreaModel
import com.gocashback.lib_common.utils.TextPinyinUtil
import com.gocashback.module_me.R
import com.gocashback.module_me.activity.PayPalAreaSelectActivity
import java.util.ArrayList

/**
 * @Author 55HAITAO
 * @Date 2019-12-02 10:25
 */
class PayPalAreaSelectAdapter(val activity: GcbBaseActivity, list: List<PaypalAreaModel>) : BaseQuickAdapter<PaypalAreaModel, BaseViewHolder>(R.layout.item_paypal_area_select, list), Filterable {
    private lateinit var origionData: List<PaypalAreaModel>

    override fun getFilter(): Filter {
        return filter
    }

    override fun convert(helper: BaseViewHolder, item: PaypalAreaModel) {
        with(item) {
            helper.setText(R.id.tv_area, name)
                    .setVisible(R.id.view_line, helper.adapterPosition != data.size - 1)
        }

    }

    fun setOrigionData(list: List<PaypalAreaModel>) {
        origionData = list
    }

    private var filter: Filter = object : Filter() {
        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val filterResults = FilterResults()

            if (origionData == null) {
                return filterResults
            }

            if (prefix == null || prefix.isEmpty()) {
                filterResults.values = origionData
                filterResults.count = origionData.size
            } else {
                val prefixString = prefix.toString().toLowerCase()
                val values = ArrayList<PaypalAreaModel>()
                val count = origionData.size
                for (i in 0 until count) {
                    val outPlaceCity = origionData[i]

                    if (TextPinyinUtil.isChinaString(prefixString)) {   // 是汉字
                        if (outPlaceCity.name.startsWith(prefixString)) {
                            values.add(outPlaceCity)
                        }
                    } else {
                        val namePinyin = TextPinyinUtil.getInstance().getPinyin(outPlaceCity.name).toLowerCase()
                        val prefixPinyin = TextPinyinUtil.getInstance().getPinyin(prefixString).toLowerCase()
                        if (namePinyin.startsWith(prefixPinyin)) {
                            values.add(outPlaceCity)
                        } else if (!TextUtils.isEmpty(outPlaceCity.simple_nme)) {
                            val simpleCode = outPlaceCity.simple_nme.toLowerCase()
                            if (simpleCode.startsWith(prefixPinyin)) {
                                values.add(outPlaceCity)
                            }
                        }
                    }

                }

                filterResults.values = values
                filterResults.count = values.size
            }

            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            setNewData(results.values as ArrayList<PaypalAreaModel>)
            (activity as PayPalAreaSelectActivity).setSearchResultView(results.values as ArrayList<PaypalAreaModel>)
        }
    }
}