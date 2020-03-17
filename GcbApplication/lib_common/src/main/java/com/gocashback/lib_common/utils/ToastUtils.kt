package com.gocashback.lib_common.utils

import android.content.Context
import android.widget.Toast

var mToast: Toast? = null
fun show(ctx: Context?, text: String) {
    mToast = mToast?.apply { setText(text) } ?: Toast.makeText(ctx, text, Toast.LENGTH_SHORT)
    mToast?.show()
}

fun show(ctx: Context?, resId: Int) {
    mToast = mToast?.apply { setText(ctx?.resources?.getString(resId)) }
            ?: Toast.makeText(ctx, ctx?.resources?.getString(resId), Toast.LENGTH_SHORT)
    mToast?.show()
}