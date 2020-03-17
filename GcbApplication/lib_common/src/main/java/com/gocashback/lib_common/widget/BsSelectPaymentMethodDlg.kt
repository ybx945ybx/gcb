package com.gocashback.lib_common.widget

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.gocashback.lib_common.R
import com.gocashback.lib_common.adapter.BsSelectBankAdapter
import com.gocashback.lib_common.adapter.BsSelectPaymentMethodAdapter
import com.gocashback.lib_common.model.interfaces.IpaymentModel
import com.gocashback.lib_common.network.model.index.IndexHelipayBankItemModel
import com.gocashback.lib_common.startPaymentMethodActivity
import kotlinx.android.synthetic.main.layout_bs_select_payment_method_dlg.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-28 16:35
 */
class BsSelectPaymentMethodDlg(context: Context, data: List<IpaymentModel>, listener: OnDlgItemClickListener?) : BottomSheetDialog(context) {

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
    private fun init(context: Context, data: List<IpaymentModel>, listener: OnDlgItemClickListener?) {
        val layout = View.inflate(context, R.layout.layout_bs_select_payment_method_dlg, null)
        setContentView(layout)

        val bsSelectpaymentMethodAdapter = BsSelectPaymentMethodAdapter(data)
        rycv_payment_method.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bsSelectpaymentMethodAdapter
        }
        if (listener != null) {
            bsSelectpaymentMethodAdapter.setOnItemClickListener { _, _, position ->
                if (position == bsSelectpaymentMethodAdapter.itemCount - 1) {
                    startPaymentMethodActivity(context)
                } else {
                    bsSelectpaymentMethodAdapter.setSelectPosition(position)
                    listener.onDlgItemClick(bsSelectpaymentMethodAdapter.data[position])
                }
                dismiss()
            }
        }
        iv_close.setOnClickListener { dismiss() }

    }

    interface OnDlgItemClickListener {
        fun onDlgItemClick(ipaymentModel: IpaymentModel)
    }

}
