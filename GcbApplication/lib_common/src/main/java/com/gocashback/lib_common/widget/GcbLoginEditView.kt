package com.gocashback.lib_common.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.gocashback.lib_common.R
import kotlinx.android.synthetic.main.view_gcb_login_edit.view.*
import android.text.InputFilter


/**
 * 登录注册页面信息输入框
 *
 * @Author 55HAITAO
 * @Date 2019-06-03 11:56
 */
class GcbLoginEditView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    init {
        // 初始化
        LayoutInflater.from(context).inflate(R.layout.view_gcb_login_edit, this)

        var changeUnderLine: Boolean
        context.obtainStyledAttributes(attrs, R.styleable.GcbLoginEditView).also {
            it.getString(R.styleable.GcbLoginEditView_android_hint)?.let { hintText -> et_content.hint = hintText }
            it.getBoolean(R.styleable.GcbLoginEditView_glev_show_password_ic, false).let { show_password_ic ->
                if (show_password_ic) {
                    iv_show_password.visibility = View.VISIBLE
                    iv_show_password.isSelected = false
                } else {
                    iv_show_password.visibility = View.GONE

                }
            }
            changeUnderLine = it.getBoolean(R.styleable.GcbLoginEditView_glev_change_under_line, true)
            it.getInt(R.styleable.GcbLoginEditView_android_inputType, InputType.TYPE_NULL).let { inputType ->
                et_content.inputType = inputType
            }
            it.getInt(R.styleable.GcbLoginEditView_android_maxLength, 6666).let { maxLength ->
                et_content.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
            }

        }.recycle()

        iv_show_password.setOnClickListener {
            if (iv_show_password.isSelected) {
                //如果选中，显示密码
                et_content.transformationMethod = PasswordTransformationMethod.getInstance()
                et_content.setSelection(et_content.text.length)

                iv_show_password.isSelected = false
            } else {
                //否则隐藏密码
                et_content.transformationMethod = HideReturnsTransformationMethod.getInstance()
                et_content.setSelection(et_content.text.length)

                iv_show_password.isSelected = true
            }
        }
        et_content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (changeUnderLine)
                    if (s.isNullOrBlank()) {
                        view_underline.setBackgroundColor(ContextCompat.getColor(context, R.color.greyDEDEDE))
                    } else {
                        view_underline.setBackgroundColor(ContextCompat.getColor(context, R.color.redFF4E4E))
                    }
            }
        })
    }

    fun addTextChangedListener(textWatcher: TextWatcher) {
        et_content.addTextChangedListener(textWatcher)
    }

    fun getText() = et_content.text

}