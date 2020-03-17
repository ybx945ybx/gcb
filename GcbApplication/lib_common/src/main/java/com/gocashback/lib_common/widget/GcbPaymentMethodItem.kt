package com.gocashback.lib_common.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.gocashback.lib_common.R
import kotlinx.android.synthetic.main.view_gcb_payment_method.view.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-13 17:02
 */
class GcbPaymentMethodItem(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    var onRightClickListener: OnRightClickListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_gcb_payment_method, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.GcbPaymentMethodItem)
        val text = ta.getString(R.styleable.GcbPaymentMethodItem_gpm_text)
        val subText = ta.getString(R.styleable.GcbPaymentMethodItem_gpm_sub_text)
        val leftImg = ta.getDrawable(R.styleable.GcbPaymentMethodItem_gpm_left_img)
        leftImg?.run { iv_payment.setImageDrawable(leftImg) }
        // 文本
        tv_payment.text = text
        // sub文本
        if (!TextUtils.isEmpty(subText)) {
            tv_payment_account.text = subText
            tv_payment_account.visibility = View.VISIBLE
        } else {
            tv_payment_account.visibility = View.GONE

        }
        ta.recycle()

        iv_more.setOnClickListener { onRightClickListener?.onRightClick() }
    }

    interface OnRightClickListener {
        fun onRightClick()
    }

    /**
     * 设置标题文本
     *
     * @param title 标题文本
     */
    fun setText(title: String) {
        tv_payment.text = title
    }

    /**
     * 设置内容文本
     *
     * @param content 内容文本
     */
    fun setSubText(content: String) {
        if (content.isEmpty()) {
            tv_payment_account.visibility = View.GONE
        } else {
            tv_payment_account.text = content
            tv_payment_account.visibility = View.VISIBLE
        }
    }

    fun getSubText() = tv_payment_account.text.toString()
}