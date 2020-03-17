package com.gocashback.lib_common.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import com.gocashback.lib_common.R
import kotlinx.android.synthetic.main.view_gcb_head.view.*

/**
 * 通用标题栏
 *
 * @property mContext Context
 * @property onLeftClickListener OnLeftClickListener?
 * @property onRightClickListener OnRightClickListener?
 * @constructor
 */
class GcbHeadView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private val mContext: Context = context

    var onLeftClickListener: OnLeftClickListener? = null
    var onRightClickListener: OnRightClickListener? = null

    init {
        // 初始化
        LayoutInflater.from(context).inflate(R.layout.view_gcb_head, this)

        context.obtainStyledAttributes(attrs, R.styleable.GcbHeadView).also {
            it.getString(R.styleable.GcbHeadView_hv_center_text)?.let { centerText -> tv_center_title.text = centerText }
            it.getString(R.styleable.GcbHeadView_hv_right_text)?.let { rightText -> tv_right.text = rightText }
            it.getDrawable(R.styleable.GcbHeadView_hv_right_img).let { rightImg ->
                if (rightImg != null) iv_right.apply {
                    setImageDrawable(rightImg)
                    visibility = View.VISIBLE
                }
                else iv_right.visibility = View.GONE

            }
            it.getBoolean(R.styleable.GcbHeadView_hv_show_left_img, true).let { show_left_img ->
                iv_left.visibility = if (show_left_img) View.VISIBLE else View.GONE
            }

        }.recycle()

        iv_left.setOnClickListener {
            if (onLeftClickListener != null) {
                onLeftClickListener?.onLeftClick()
            } else {
                // 隐藏输入法键盘
                (mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(iv_left.windowToken, 0)
//                (mContext as Activity).onBackPressed()
                (mContext as Activity).finish()
            }
        }

        iv_right.setOnClickListener {
            onRightClickListener?.onRightClick()
        }

        tv_right.setOnClickListener {
            onRightClickListener?.onRightClick()
        }


    }

    fun setRightText(rightText: String) {
        tv_right.text = rightText
    }

    fun setCenterText(centerText: String) {
        tv_center_title.text = centerText
    }

    interface OnLeftClickListener {
        fun onLeftClick()
    }

    interface OnRightClickListener {
        fun onRightClick()
    }

}