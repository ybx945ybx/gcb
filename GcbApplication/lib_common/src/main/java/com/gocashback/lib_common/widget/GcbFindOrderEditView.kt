package com.gocashback.lib_common.widget

import android.content.Context
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.gocashback.lib_common.R
import kotlinx.android.synthetic.main.view_gcb_find_order_edit.view.*

/**
 * 找回订单页面信息输入框
 *
 * @Author 55HAITAO
 * @Date 2019-06-10 17:47
 */
class GcbFindOrderEditView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    var onCurrencyClickListener: OnCurrencyClickListener? = null
    var onRightClickListener: OnRightClickListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_gcb_find_order_edit, this)

        context.obtainStyledAttributes(attrs, R.styleable.GcbFindOrderEditView).also {
            it.getString(R.styleable.GcbFindOrderEditView_android_hint)?.let { hint ->
                edit_text.hint = hint
                edit_text_hint.text = hint
            }
            it.getString(R.styleable.GcbFindOrderEditView_gfoev_left_text)?.let { leftText -> tv_left.text = leftText }
            it.getDrawable(R.styleable.GcbFindOrderEditView_gfoev_right_img).let { rightImg ->
                if (rightImg != null) iv_right_arrow_down.apply {
                    setImageDrawable(rightImg)
                    visibility = View.VISIBLE
                } else iv_right_arrow_down.visibility = View.GONE

            }
            it.getBoolean(R.styleable.GcbFindOrderEditView_gfoev_show_currency, false).let { show_currency ->
                tv_currency.visibility = if (show_currency) View.VISIBLE else View.GONE
            }

            it.getBoolean(R.styleable.GcbFindOrderEditView_gfoev_edit_enable, true).let { edit_enable ->
                edit_text.isEnabled = edit_enable
                edit_text.visibility = if (edit_enable) View.VISIBLE else View.GONE
                edit_text_hint.visibility = if (edit_enable) View.GONE else View.VISIBLE

            }
            it.getInt(R.styleable.GcbFindOrderEditView_android_inputType, InputType.TYPE_NULL).let { inputType ->
                edit_text.inputType = inputType
            }
        }.recycle()

        tv_currency.setOnClickListener {
            onCurrencyClickListener?.onCurrencyClick()
        }

        iv_right_arrow_down.setOnClickListener {
            onRightClickListener?.onRightClick()
        }
    }

    fun setCurrency(currency: String) {
        tv_currency.text = currency
    }

    fun setText(text: String) {
        if (edit_text_hint.visibility == View.VISIBLE) {
            edit_text_hint.text = text
            edit_text_hint.setTextColor(resources.getColor(R.color.grey333333))
        }
//        else {
        edit_text.setText(text)

//        }

    }

    fun clearEditFocus(){
        edit_text.clearFocus()
    }

    fun getText() = edit_text.text

    fun addTextChangedListener(textWatcher: TextWatcher) {
        edit_text.addTextChangedListener(textWatcher)
    }

    interface OnCurrencyClickListener {
        fun onCurrencyClick()
    }

    interface OnRightClickListener {
        fun onRightClick()
    }
}