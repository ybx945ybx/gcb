package com.gocashback.lib_common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.gocashback.lib_common.R
import kotlinx.android.synthetic.main.view_gcb_me_fragment_item_text.view.*

/**
 *
 * 我的页面
 * 表单 - 图标 + 文字
 *
 * @Author 55HAITAO
 * @Date 2019-05-22 14:48
 */
class GcbMeFragmentItemTextView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    init {
        init(context, attrs)
    }

    /**
     * 初始化
     *
     * @param context context
     * @param attrs   attrs
     */
    private fun init(context: Context, attrs: AttributeSet) {
        LayoutInflater.from(context).inflate(R.layout.view_gcb_me_fragment_item_text, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.GcbMeFragmentItemTextView)
        val leftImgRes = ta.getResourceId(R.styleable.GcbMeFragmentItemTextView_gmfitv_img, 0)
        val title = ta.getString(R.styleable.GcbMeFragmentItemTextView_gmfitv_title)
        // 左侧图标
        if (leftImgRes != 0) {
            img_left.visibility = View.VISIBLE
            img_left.setImageResource(leftImgRes)
        } else {
            img_left.visibility = View.GONE
        }
        // 标题
        tv_title.text = title
        ta.recycle()
    }

    /**
     * 下划线是否可见
     *
     * @param visible 是否可见
     */
    fun setUnderlineVisible(visible: Boolean) {
        view_divider.visibility = if (visible) View.VISIBLE else View.GONE
    }
}
