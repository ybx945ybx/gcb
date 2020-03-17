package com.gocashback.lib_common.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import com.gocashback.lib_common.R
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.startPaymentPasswordActivity
import com.gocashback.lib_common.utils.dp2px
import com.gocashback.lib_common.utils.getScreenWidth
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.layout_check_pwd_dlg.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-15 10:58
 */
class CheckPwdDlg(private var activity: GcbBaseActivity, private var checkPwdListener: CheckPwdListener?, private var needCheckSuccess: Boolean = true) : Dialog(activity) {
    var checkSuccess = false

    init {
        init(activity)
    }

    private fun init(activity: GcbBaseActivity) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (window != null) {
            // 顶部透明
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val layout = View.inflate(activity, R.layout.layout_check_pwd_dlg, null)
        val margin = dp2px(activity, 32f) // 32dp
        val dlgWidth = getScreenWidth(activity) - margin * 2
        setContentView(layout, LinearLayout.LayoutParams(dlgWidth, LinearLayout.LayoutParams.WRAP_CONTENT))

        et_pwd.isFocusable = true
        et_pwd.isFocusableInTouchMode = true
        llyt_pwd.setOnClickListener {
            et_pwd.requestFocus()
            context.getSystemService(Context.INPUT_METHOD_SERVICE)?.run {
                (this as InputMethodManager).showSoftInput(et_pwd, 0)
            }
        }
        // edittext监听
        et_pwd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Logger.d("CheckPwdDlg------$s")
                tv_1.isSelected = false
                tv_2.isSelected = false
                tv_3.isSelected = false
                tv_4.isSelected = false
                tv_5.isSelected = false
                tv_6.isSelected = false

//                if (TextUtils.isEmpty(s)) {
//                    tv_1.setText("")
//                    tv_2.setText("")
//                    tv_3.setText("")
//                    tv_4.setText("")
//                    tv_5.setText("")
//                    tv_6.setText("")
//                } else {

                when (s.toString().length) {
                    0 -> {
                        tv_1.text = ""
                        tv_2.text = ""
                        tv_3.text = ""
                        tv_4.text = ""
                        tv_5.text = ""
                        tv_6.text = ""
                    }
                    1 -> {
                        tv_1.isSelected = true
                        tv_1.text = "*"
                        tv_2.text = ""
                        tv_3.text = ""
                        tv_4.text = ""
                        tv_5.text = ""
                        tv_6.text = ""
                    }
                    2 -> {
                        tv_2.isSelected = true
                        tv_1.text = "*"
                        tv_2.text = "*"

                        tv_3.text = ""
                        tv_4.text = ""
                        tv_5.text = ""
                        tv_6.text = ""
                    }
                    3 -> {
                        tv_3.isSelected = true
                        tv_1.text = "*"
                        tv_2.text = "*"
                        tv_3.text = "*"

                        tv_4.text = ""
                        tv_5.text = ""
                        tv_6.text = ""
                    }
                    4 -> {
                        tv_4.isSelected = true
                        tv_1.text = "*"
                        tv_2.text = "*"
                        tv_3.text = "*"
                        tv_4.text = "*"
                        tv_5.text = ""
                        tv_6.text = ""
                    }
                    5 -> {
                        tv_5.isSelected = true
                        tv_1.text = "*"
                        tv_2.text = "*"
                        tv_3.text = "*"
                        tv_4.text = "*"
                        tv_5.text = "*"
                        tv_6.text = ""
                    }
                    6 -> {
                        tv_6.isSelected = true
                        tv_1.text = "*"
                        tv_2.text = "*"
                        tv_3.text = "*"
                        tv_4.text = "*"
                        tv_5.text = "*"
                        tv_6.text = "*"
                    }
                }
//                    for ((i, char) in s.toString().withIndex()) { //i 即角标数，item即list的对应的条目
//                        when (i) {
//                            0 -> tv_1.setText(char.toString())
//                            1 -> tv_2.setText(char.toString())
//                            2 -> tv_3.setText(char.toString())
//                            3 -> tv_4.setText(char.toString())
//                            4 -> tv_5.setText(char.toString())
//                            5 -> tv_6.setText(char.toString())
//
//                        }
//
//                    }

                if (s.toString().length == 6)
                    checkPwdListener?.onInputCompleteL(s.toString())
//                }
            }
        })

        //  忘记密码
        tv_forget_pwd.setOnClickListener {
            startPaymentPasswordActivity(activity, true)
        }
//        tv_title.text = title
//        tv_content.text = content
//        // 确定按钮文字
//        if (!TextUtils.isEmpty(confirmText)) {
//            tv_confirm.text = confirmText
//        }
//        // 关闭按钮文字
//        if (!TextUtils.isEmpty(cancelText)) {
//            tv_cancel.text = cancelText
//        }
//
//        // 是否有标题
//        tv_title.visibility = if (TextUtils.isEmpty(title)) View.GONE else View.VISIBLE
        // 是否有取消按钮
        //        mTvCancel.setVisibility(mOnCancelListener == null ? View.GONE : View.VISIBLE);
        //        viewCenter.setVisibility(mOnCancelListener == null ? View.GONE : View.VISIBLE);
    }

    interface CheckPwdListener {
        fun onInputCompleteL(pwd: String)
    }

//    override fun show() {
//        super.show()
//        setCanceledOnTouchOutside(false)
//    }

    override fun dismiss() {
        if (needCheckSuccess && !checkSuccess) {
//            activity.hideSoftInput()
            activity.getSystemService(Context.INPUT_METHOD_SERVICE)?.run {
                (this as InputMethodManager).hideSoftInputFromWindow(et_pwd.windowToken, 2)
            }
            super.dismiss()
            activity.finish()
//            Observable.timer(150, TimeUnit.MILLISECONDS)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .doAfterTerminate {
//                        super.dismiss()
//                        activity.finish()
//                    }
//                    .subscribe()
        } else {
//            activity.hideSoftInputDely()
            activity.getSystemService(Context.INPUT_METHOD_SERVICE)?.run {
                (this as InputMethodManager).hideSoftInputFromWindow(et_pwd.windowToken, 2)
            }
            super.dismiss()

        }

    }


    fun setText(text: String) {
        et_pwd.setText(text)
    }

    fun clearContent() {
        et_pwd.setText("")
        tv_1.text = ""
        tv_2.text = ""
        tv_3.text = ""
        tv_4.text = ""
        tv_5.text = ""
        tv_6.text = ""
    }
}