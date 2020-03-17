package com.gocashback.lib_common.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.gocashback.lib_common.R
import com.gocashback.lib_common.utils.goToBuy
import kotlinx.android.synthetic.main.view_gcb_gobuy.view.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-30 15:16
 */
class GcbGoBuyView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    var gotobuyUrl: String = ""
    var activity: Activity? = null
    var coupon_code: String = ""

    init {
        // 初始化
        LayoutInflater.from(context).inflate(R.layout.view_gcb_gobuy, this)

        setOnClickListener {
            activity?.let {
                goToBuy(activity, gotobuyUrl, coupon_code)
            }
        }
    }

    fun setInvalid(invalid: Boolean) {
        this.isEnabled = invalid
        tv_go_buy.isEnabled = invalid
        tv_go_buy.text = if (invalid) "Shop" else "Invalid"
    }


}