package com.gocashback.lib_common.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import com.gocashback.lib_common.R
import com.gocashback.lib_common.language.LOCALE_CHINESE
import com.gocashback.lib_common.language.getLanguage
import com.gocashback.lib_common.utils.dp2px
import com.gocashback.lib_common.utils.getScreenWidth
import kotlinx.android.synthetic.main.layout_welfare_get_dlg.*

/**
 * @Author 55HAITAO
 * @Date 2019-08-01 18:48
 */
class WelfareGetDlg(context: Context, private var onShareListener: OnShareListener?) : Dialog(context) {

    init {
        init(context)
    }

    private fun init(context: Context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (window != null) {
            // 顶部透明
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val layout = View.inflate(context, R.layout.layout_welfare_get_dlg, null)
        val margin = dp2px(context, 42f) // 32dp
        val dlgWidth = getScreenWidth(context) - margin * 2
        setContentView(layout, LinearLayout.LayoutParams(dlgWidth, LinearLayout.LayoutParams.WRAP_CONTENT))

        iv_success.setImageResource(if (LOCALE_CHINESE == getLanguage(context)) R.mipmap.ic_welfare_sucess else R.mipmap.ic_welfare_success_en)
        tv_share.setOnClickListener {
            onShareListener?.onShare(this)
            dismiss()
        }
    }

    interface OnShareListener {
        fun onShare(dlg: Dialog)
    }

}
