package com.gocashback.lib_common.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.gocashback.lib_common.R
import kotlinx.android.synthetic.main.view_gcb_setting_item_text.view.*

/**
 *
 * 设置页面 消息展示
 *
 * @Author 55HAITAO
 * @Date 2019-06-13 11:03
 */
class GcbSettingItemTextView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_gcb_setting_item_text, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.GcbSettingItemTextView)
        val text = ta.getString(R.styleable.GcbSettingItemTextView_gseitv_text)
        val subText = ta.getString(R.styleable.GcbSettingItemTextView_gseitv_sub_text)
        // 文本
        tv_item_text.text = text
        // sub文本
        if (!TextUtils.isEmpty(subText)) {
            tv_sub_text.text = subText
        }
        ta.recycle()
    }

    /**
     * 设置标题文本
     *
     * @param title 标题文本
     */
    fun setText(title: String) {
        tv_item_text.text = title
    }

    /**
     * 设置内容文本
     *
     * @param content 内容文本
     */
    fun setSubText(content: String) {
        tv_sub_text.text = content
    }

    fun getSubText() = tv_sub_text.text.toString()
}