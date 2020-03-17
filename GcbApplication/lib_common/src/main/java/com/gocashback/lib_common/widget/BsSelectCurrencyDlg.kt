package com.gocashback.lib_common.widget

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.gocashback.lib_common.R
import com.gocashback.lib_common.adapter.BsSelectCurrencyAdapter
import com.gocashback.lib_common.model.CurrencyItemModel
import com.gocashback.lib_common.network.model.user.LostOrderShowCurrencyIfModel
import kotlinx.android.synthetic.main.layout_bottomsheet_list_dlg.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-28 16:35
 */
class BsSelectCurrencyDlg(context: Context, data: LostOrderShowCurrencyIfModel, listener: OnDlgItemClickListener?) : BottomSheetDialog(context) {

    init {
        init(context, data, listener)
    }

    /**
     * 初始化
     *
     * @param context  mContext
     * @param data     数据
     * @param listener 点击回调
     */
    private fun init(context: Context, data: LostOrderShowCurrencyIfModel, listener: OnDlgItemClickListener?) {
        val layout = View.inflate(context, R.layout.layout_bottomsheet_list_dlg, null)
        setContentView(layout)

        val list: ArrayList<CurrencyItemModel> = arrayListOf()
        if (!TextUtils.isEmpty(data.USD)) {
            list.add(CurrencyItemModel().apply {
                currency = "USD (\$)"
                currency_value = "USD"
            })
        }
        if (!TextUtils.isEmpty(data.CAD)) {
            list.add(CurrencyItemModel().apply {
                currency = "CAD (\$)"
                currency_value = "CAD"
            })
        }
        if (!TextUtils.isEmpty(data.EUR)) {
            list.add(CurrencyItemModel().apply {
                currency = "EUR (€)"
                currency_value = "EUR"
            })
        }
        if (!TextUtils.isEmpty(data.GBP)) {
            list.add(CurrencyItemModel().apply {
                currency = "GBP (£)"
                currency_value = "GBP"
            })
        }

        if (!TextUtils.isEmpty(data.CNY)) {
            list.add(CurrencyItemModel().apply {
                currency = "CNY (¥)"
                currency_value = "CNY"
            })
        }

        val bsSelectCurrencyAdapter = BsSelectCurrencyAdapter(list)
        rv_content.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bsSelectCurrencyAdapter
        }
        if (listener != null) {
            bsSelectCurrencyAdapter.setOnItemClickListener { _, _, position ->
                bsSelectCurrencyAdapter.setSelectPosition(position)
                dismiss()
                listener.onDlgItemClick(bsSelectCurrencyAdapter.data[position].currency_value)
            }
        }
    }

    interface OnDlgItemClickListener {
        fun onDlgItemClick(currencyValue: String)
    }

}
