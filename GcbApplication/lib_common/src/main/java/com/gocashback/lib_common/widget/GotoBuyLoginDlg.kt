package com.gocashback.lib_common.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import com.gocashback.lib_common.R
import com.gocashback.lib_common.network.model.store.StoreDetailIfModel
import com.gocashback.lib_common.utils.dp2px
import com.gocashback.lib_common.utils.getScreenWidth
import kotlinx.android.synthetic.main.layout_goto_buy_login_dlg.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-28 14:55
 */
class GotoBuyLoginDlg(context: Context, storeInfo: StoreDetailIfModel, private var onClickListener: OnClickListener?) : Dialog(context) {

    init {
        init(context, storeInfo)
    }

    private fun init(context: Context, storeInfo: StoreDetailIfModel) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (window != null) {
            // 顶部透明
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val layout = View.inflate(context, R.layout.layout_goto_buy_login_dlg, null)
        val margin = dp2px(context, 42f) // 32dp
        val dlgWidth = getScreenWidth(context) - margin * 2
        setContentView(layout, LinearLayout.LayoutParams(dlgWidth, LinearLayout.LayoutParams.WRAP_CONTENT))
        // store
        tv_store.text = storeInfo.name
        // rebate
        tv_rebate.text = storeInfo.reward

        tv_login.setOnClickListener {
            onClickListener?.onLoginClick(this)
            dismiss()
        }

        tv_not_login.setOnClickListener {
            onClickListener?.onUnLoginClick(this)
            dismiss()
        }
    }

    interface OnClickListener {
        fun onLoginClick(dlg: Dialog)
        fun onUnLoginClick(dlg: Dialog)
    }

//    override fun show() {
//        super.show()
//        setCanceledOnTouchOutside(false)
//    }
}
