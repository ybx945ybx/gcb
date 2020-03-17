package com.gocashback.lib_common.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.gocashback.lib_common.R
import kotlinx.android.synthetic.main.view_gcb_simple_info.view.*

/**
 * key value   样式简单文本
 *
 * @Author 55HAITAO
 * @Date 2019-06-06 16:00
 */
class GcbSimpeInfoTextview(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_gcb_simple_info, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.GcbSimpleInfoTextView)
        val text = ta.getString(R.styleable.GcbSimpleInfoTextView_gsitv_text)
        val subText = ta.getString(R.styleable.GcbSimpleInfoTextView_gsitv_sub_text)
        // 文本
        tv_simple_info_text.text = text
        // sub文本
        if (!TextUtils.isEmpty(subText)) {
            tv_simple_info_subtext.text = subText
        }
        ta.recycle()
    }

    /**
     * 设置标题文本
     *
     * @param title 标题文本
     */
    fun setText(title: String) {
        tv_simple_info_text.text = title
    }

    /**
     * 设置内容文本
     *
     * @param content 内容文本
     */
    fun setSubText(content: String) {
        tv_simple_info_subtext.text = content
    }

    fun getSubText() = tv_simple_info_subtext.text.toString()
}