package com.gocashback.lib_common.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.gocashback.lib_common.R
import kotlinx.android.synthetic.main.view_gcb_sub_title.view.*

/**
 *
 * 通用版块标题栏
 *
 * @Author 55HAITAO
 * @Date 2019/4/3 8:16 PM
 */

class GcbSubTitleView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    val mContext: Context = context
    var onRightClickListener: OnRightClickListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_gcb_sub_title, this)
        context.obtainStyledAttributes(attrs, R.styleable.GcbSubTitleView).also {
            it.getDrawable(R.styleable.GcbSubTitleView_stv_left_img).let { left_img ->
                iv_sub_title_left_img.apply {
                    if (left_img == null) visibility = View.GONE
                    else {
                        visibility = View.VISIBLE
                        setImageDrawable(left_img)
                    }
                }
            }
            it.getString(R.styleable.GcbSubTitleView_stv_title)?.let { sub_title -> tv_sub_title.text = sub_title }
            it.getString(R.styleable.GcbSubTitleView_stv_right_text).let { right_text ->
                tv_sub_title_right_text.apply {
                    if (TextUtils.isEmpty(right_text)) visibility = View.GONE
                    else {
                        visibility = View.VISIBLE
                        text = right_text
                    }
                }
            }

        }.recycle()

        tv_sub_title_right_text.setOnClickListener { view ->
            onRightClickListener?.onRightClick(view)
        }
    }

    interface OnRightClickListener {
        fun onRightClick(view: View)
    }
}