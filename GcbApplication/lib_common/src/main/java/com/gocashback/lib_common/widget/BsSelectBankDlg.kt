package com.gocashback.lib_common.widget

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.gocashback.lib_common.R
import com.gocashback.lib_common.adapter.BsSelectBankAdapter
import com.gocashback.lib_common.network.model.index.IndexHelipayBankItemModel
import kotlinx.android.synthetic.main.layout_bs_select_bank_dlg.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-28 16:35
 */
class BsSelectBankDlg(context: Context, data: List<IndexHelipayBankItemModel>, listener: OnDlgItemClickListener?) : BottomSheetDialog(context) {

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
    private fun init(context: Context, data: List<IndexHelipayBankItemModel>, listener: OnDlgItemClickListener?) {
        val layout = View.inflate(context, R.layout.layout_bs_select_bank_dlg, null)
        setContentView(layout)

        val bsSelectBankAdapter = BsSelectBankAdapter(data)
        rycv_bank.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bsSelectBankAdapter
        }
        if (listener != null) {
            bsSelectBankAdapter.setOnItemClickListener { _, _, position ->
                bsSelectBankAdapter.setSelectPosition(position)
                dismiss()
                listener.onDlgItemClick(bsSelectBankAdapter.data[position])
            }
        }

        iv_close.setOnClickListener { dismiss() }
    }

    interface OnDlgItemClickListener {
        fun onDlgItemClick(indexHelipayBankItemModel: IndexHelipayBankItemModel)
    }

}
