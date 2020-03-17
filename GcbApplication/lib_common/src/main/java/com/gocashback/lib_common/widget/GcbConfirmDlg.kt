package com.gocashback.lib_common.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.annotation.StringRes
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import com.gocashback.lib_common.R
import com.gocashback.lib_common.utils.dp2px
import com.gocashback.lib_common.utils.getScreenWidth
import kotlinx.android.synthetic.main.layout_confirm_dlg.*

/**
 * 通用确认对话框
 *
 * @Author 55HAITAO
 * @Date 2019-06-14 17:56
 */
class GcbConfirmDlg(context: Context, title: String?, content: String?, confirmText: String?,
                    cancelText: String?, private val mOnConfirmListener: OnConfirmListener?, private val mOnCancelListener: OnCancelListener?, private val autoDismiss: Boolean = true) : Dialog(context) {

    init {
        init(context, title, content, confirmText, cancelText)
    }

    private fun init(context: Context, title: String?, content: String?, confirmText: String?, cancelText: String?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (window != null) {
            // 顶部透明
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val layout = View.inflate(context, R.layout.layout_confirm_dlg, null)
        val margin = dp2px(context, 32f) // 32dp
        val dlgWidth = getScreenWidth(context) - margin * 2
        setContentView(layout, LinearLayout.LayoutParams(dlgWidth, LinearLayout.LayoutParams.WRAP_CONTENT))

        tv_title.text = title
        tv_content.text = content
        // 确定按钮文字
        if (!TextUtils.isEmpty(confirmText)) {
            tv_confirm.text = confirmText
        }
        // 关闭按钮文字
        if (!TextUtils.isEmpty(cancelText)) {
            tv_cancel.text = cancelText
        }

        // 是否有标题
        tv_title.visibility = if (TextUtils.isEmpty(title)) View.GONE else View.VISIBLE
        // 是否有取消按钮
        //        mTvCancel.setVisibility(mOnCancelListener == null ? View.GONE : View.VISIBLE);
        //        viewCenter.setVisibility(mOnCancelListener == null ? View.GONE : View.VISIBLE);
        /**
         * 取消
         */
        tv_cancel.setOnClickListener {
            mOnCancelListener?.onCancel(this)
            if (autoDismiss)
                dismiss()
        }

        /**
         * 确认
         */
        tv_confirm.setOnClickListener {
            mOnConfirmListener?.onConfirm(this)
            if (autoDismiss)
                dismiss()
        }
    }

    /**
     * 取消回调
     */
    interface OnConfirmListener {
        fun onConfirm(dlg: GcbConfirmDlg)
    }

    /**
     * 确认回调
     */
    interface OnCancelListener {
        fun onCancel(dlg: GcbConfirmDlg)
    }

    override fun show() {
        super.show()
        setCanceledOnTouchOutside(false)
    }

    class Builder(private val mContext: Context) {
        private var mTitle: String? = null
        private var mMessage: String? = null
        private var mConfirmText: String? = null
        private var mCancelText: String? = null
        private var mOnConfirmListener: OnConfirmListener? = null
        private var mOnCancelListener: OnCancelListener? = null
        private var autoDismiss: Boolean = true

        fun setTitle(title: String): Builder {
            mTitle = title
            return this
        }

        fun setTitle(@StringRes titleResId: Int): Builder {
            mTitle = mContext.getString(titleResId)
            return this
        }

        fun setMessage(message: String): Builder {
            mMessage = message
            return this
        }

        fun setMessage(@StringRes messageResId: Int): Builder {
            mMessage = mContext.getString(messageResId)
            return this
        }

        fun setConfirmListener(confirmText: String, onConfirmListener: OnConfirmListener): Builder {
            mConfirmText = confirmText
            mOnConfirmListener = onConfirmListener
            return this
        }

        fun setConfirmListener(@StringRes confirmTextResId: Int, onConfirmListener: OnConfirmListener): Builder {
            mConfirmText = mContext.getString(confirmTextResId)
            mOnConfirmListener = onConfirmListener
            return this
        }

        fun setCancelListener(cancelText: String, onCancelListener: OnCancelListener): Builder {
            mCancelText = cancelText
            mOnCancelListener = onCancelListener
            return this
        }

        fun setCancelListener(@StringRes cancelTextResId: Int, onCancelListener: OnCancelListener): Builder {
            mCancelText = mContext.getString(cancelTextResId)
            mOnCancelListener = onCancelListener
            return this
        }

        fun setAutoDismiss(autoDismiss: Boolean): Builder {
            this.autoDismiss = autoDismiss
            return this
        }

        fun create(): GcbConfirmDlg {
            return GcbConfirmDlg(mContext, mTitle, mMessage, mConfirmText, mCancelText, mOnConfirmListener, mOnCancelListener, autoDismiss)
        }
    }
}
