package com.gocashback.lib_common.widget

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.gocashback.lib_common.R
import com.gocashback.lib_common.startMessageActivity
import com.gocashback.lib_common.startSearchActivity
import kotlinx.android.synthetic.main.layout_confirm_dlg.view.*
import kotlinx.android.synthetic.main.view_gcb_search.view.*

/**
 * 通用搜索框
 *
 * @Author 55HAITAO
 * @Date 2019/4/3 5:50 PM
 */
class GcbSearchView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_gcb_search, this)

        context.obtainStyledAttributes(attrs, R.styleable.GcbSearchView).also {
            it.getString(R.styleable.GcbSearchView_sv_hint)?.let { hint ->
                tv_search_hint.text = hint
                etv_search.hint = hint
            }
            it.getBoolean(R.styleable.GcbSearchView_sv_editable, false).let { editable ->
                tv_search_hint.visibility = if (editable) View.GONE else View.VISIBLE
                etv_search.visibility = if (editable) View.VISIBLE else View.GONE
            }
            it.getBoolean(R.styleable.GcbSearchView_sv_show_message, false).let { show_message ->
                clyt_message.visibility = if (show_message) View.VISIBLE else View.GONE
//                tv_search_cancel.visibility = if (show_message) View.GONE else View.VISIBLE
            }
            it.getBoolean(R.styleable.GcbSearchView_sv_show_cancel, false).let { show_cancel ->
                if (show_cancel) {
                    clyt_message.visibility = View.VISIBLE
                    iv_message.visibility = View.GONE
                    tv_message_num.visibility = View.GONE
                    tv_search_cancel.visibility = View.VISIBLE

                } else if (clyt_message.visibility == View.VISIBLE) {

                } else {
                    clyt_message.visibility = View.GONE

                }
//                clyt_message.visibility = if (show_cancel || clyt_message.visibility == View.VISIBLE) View.VISIBLE else View.GONE
//                tv_search_cancel.visibility = if (show_message) View.GONE else View.VISIBLE
            }
//            it.getInt(R.styleable.GcbSearchView_android_imeOptions, IME_ACTION_SEARCH)?.let { imeOptions ->
//                etv_search.imeOptions = imeOptions
//            }
        }.recycle()

        tv_search_hint.setOnClickListener { startSearchActivity(context) }
        clyt_message.setOnClickListener { startMessageActivity(context) }
        tv_search_cancel.setOnClickListener { (context as Activity).finish() }
        etv_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(s)) {
                    iv_clear.visibility = View.GONE
                } else {
                    iv_clear.visibility = View.VISIBLE

                }
            }
        })

        iv_clear.setOnClickListener {
            etv_search.setText("")
            iv_clear.visibility = View.GONE
        }
    }

    fun setMessageNum(messageNum: Int) {
        tv_message_num.apply {
            if (messageNum > 0) {
                visibility = View.VISIBLE
                text = if (messageNum > 99) "99+" else messageNum.toString()
            } else {
                visibility = View.GONE

            }
        }
    }

    fun addTextChangedListener(textWatcher: TextWatcher) {
        etv_search.addTextChangedListener(textWatcher)
    }

    fun addOnKeyListener(onKeyListener: OnKeyListener) {
        etv_search.setOnKeyListener(onKeyListener)
    }

    fun addFocusChangeListener(onFocusChangeListener: OnFocusChangeListener) {
        etv_search.onFocusChangeListener = onFocusChangeListener
    }

    override fun clearFocus() {
        etv_search.clearFocus()
    }

    fun getEditText() = etv_search
    fun getText() = etv_search.text
}