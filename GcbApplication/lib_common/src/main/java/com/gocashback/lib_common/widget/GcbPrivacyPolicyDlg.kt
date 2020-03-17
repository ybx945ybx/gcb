package com.gocashback.lib_common.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import com.gocashback.lib_common.R
import com.gocashback.lib_common.utils.dp2px
import com.gocashback.lib_common.utils.getScreenWidth
import kotlinx.android.synthetic.main.layout_confirm_dlg.*

/**
 * @Author 55HAITAO
 * @Date 2020-01-16 17:00
 */

class GcbPrivacyPolicyDlg(context: Context, private val mOnConfirmListener: OnConfirmListener?, private val mOnCancelListener: OnCancelListener?) : Dialog(context) {

    init {
        init(context)
    }

    private fun init(context: Context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (window != null) {
            // 顶部透明
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val layout = View.inflate(context, R.layout.layout_privacy_policy_dlg, null)
        val margin = dp2px(context, 32f) // 32dp
        val dlgWidth = getScreenWidth(context) - margin * 2
        setContentView(layout, LinearLayout.LayoutParams(dlgWidth, LinearLayout.LayoutParams.WRAP_CONTENT))

        /**
         * 取消
         */
        tv_cancel.setOnClickListener {
            mOnCancelListener?.onCancel(this)
            dismiss()
        }

        /**
         * 确认
         */
        tv_confirm.setOnClickListener {
            mOnConfirmListener?.onConfirm(this)
            dismiss()
        }
    }

    /**
     * 取消回调
     */
    interface OnConfirmListener {
        fun onConfirm(dlg: GcbPrivacyPolicyDlg)
    }

    /**
     * 确认回调
     */
    interface OnCancelListener {
        fun onCancel(dlg: GcbPrivacyPolicyDlg)
    }

    override fun show() {
        super.show()
        setCanceledOnTouchOutside(false)
    }

}
