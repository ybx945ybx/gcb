package com.gocashback.lib_common.utils

import android.app.Activity
import com.gocashback.lib_common.R
import com.gocashback.lib_common.base.GcbBaseApplication
import com.gocashback.lib_common.language.getLocale
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-10 14:08
 */
fun dateFormat(date: Long): String {
    return dateFormat(date, "MM/dd/yyyy")
//    return dateFormat(date, "yyyy-MM-dd HH:mm:ss")
}

fun dateFormat(timeMillis: Long, pattern: String, withLimit: Boolean = false, activity: Activity? = null): String {
    val format = SimpleDateFormat("MM/dd/yyyy", getLocale(GcbBaseApplication.application))
    return if (withLimit) activity?.resources?.getString(R.string.deal_expires) + " " + format.format(Date(timeMillis)) else format.format(Date(timeMillis))
}

fun dateFormatNormal(date: Long): String {
    return dateFormat(date, "yyyy-MM-dd")
//    return dateFormat(date, "yyyy-MM-dd HH:mm:ss")
}