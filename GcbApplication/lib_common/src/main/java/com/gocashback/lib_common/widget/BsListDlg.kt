package com.gocashback.lib_common.widget

import android.app.Dialog
import android.content.Context
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gocashback.lib_common.R
import com.gocashback.lib_common.adapter.ListBsDlgAdapter
import kotlinx.android.synthetic.main.layout_bottomsheet_list_dlg.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-13 19:24
 */
class BsListDlg private constructor(context: Context, data: List<ListDlgItem>, listener: OnDlgItemClickListener?) : BottomSheetDialog(context) {

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
    private fun init(context: Context, data: List<ListDlgItem>, listener: OnDlgItemClickListener?) {
        val layout = View.inflate(context, R.layout.layout_bottomsheet_list_dlg, null)
        setContentView(layout)

        val listBsDlgAdapter = ListBsDlgAdapter(data)
        rv_content.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listBsDlgAdapter
        }
        if (listener != null) {
            listBsDlgAdapter.setOnItemClickListener { adapter1, view, position -> listener.onDlgItemClick(adapter1, view, position, this@BsListDlg) }
        }
    }

    class Builder(private val mContext: Context) {

        private val mData: MutableList<ListDlgItem> = mutableListOf()
        private var mListener: OnDlgItemClickListener? = null

        /**
         * 添加数据
         *
         * @param imgRes 图片resId
         * @param strRes 文本resId
         * @return this
         */
        fun addData(@DrawableRes imgRes: Int, @StringRes strRes: Int): Builder {
            mData.add(ListDlgItem(imgRes, mContext.getString(strRes)))
            return this
        }

        /**
         * 添加数据
         *
         * @param imgRes 图片resId
         * @param str    文本
         * @return this
         */
        fun addData(@DrawableRes imgRes: Int, str: String): Builder {
            mData.add(ListDlgItem(imgRes, str))
            return this
        }

        /**
         * 设置条目点击事件
         *
         * @param listener 条目点击事件
         * @return this
         */
        fun setListener(listener: OnDlgItemClickListener): Builder {
            mListener = listener
            return this
        }

        fun create(): BsListDlg {
            return BsListDlg(mContext, mData, mListener)
        }
    }

    interface OnDlgItemClickListener {
        fun onDlgItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int, dlg: Dialog)
    }

    class ListDlgItem(val iconRes: Int, val text: String)
}
