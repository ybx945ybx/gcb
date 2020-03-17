package com.gocashback.lib_common.widget

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.gocashback.lib_common.R
import kotlinx.android.synthetic.main.view_gcb_payment_method_edit.view.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-14 09:49
 */
class GcbPaymentMethodEditView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_gcb_payment_method_edit, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.GcbPaymentMethodEditView)
        val titleText = ta.getString(R.styleable.GcbPaymentMethodEditView_gpmev_title_text)
        val hint = ta.getString(R.styleable.GcbPaymentMethodEditView_android_hint)
        // 文本
        if (titleText.isNullOrEmpty()) {
            title_text.visibility = View.GONE
        } else {
            title_text.text = titleText
            title_text.visibility = View.VISIBLE
        }
        // hint文本
        edit_text.hint = hint
        ta.recycle()

    }


    fun addTextChangedListener(textWatcher: TextWatcher) {
        edit_text.addTextChangedListener(textWatcher)
    }

    /**
     * 设置标题文本
     *
     * @param title 标题文本
     */
    fun setTitleText(title: String) {
        title_text.text = title
    }

    /**
     * 设置内容文本
     *
     * @param hint 内容文本
     */
    fun sethintText(hint: String) {
        edit_text.hint = hint
    }

    /**
     * 设置输入框内容
     * @return String
     */
    fun setText(text: String) {
        if (disable_edit_text.visibility == View.VISIBLE) {
            disable_edit_text.text = text
            disable_edit_text.setTextColor(resources.getColor(R.color.grey333333))
        }
        edit_text.setText(text)
    }

    /**
     * 设置输入框内容
     * @return String
     */
    fun setdisablehint(hint: String) {
        disable_edit_text.text = hint

    }

    /**
     * 获取输入框内容
     * @return String
     */
    fun getText() = edit_text.text.toString()

    fun setEditEnable(editEnable: Boolean) {
//        edit_text.isEnabled = editEnable
        edit_text.visibility = if (editEnable) View.VISIBLE else View.GONE
        disable_edit_text.visibility = if (editEnable) View.GONE else View.VISIBLE
    }
}