package com.gocashback.lib_common.utils

import android.app.Activity
import android.content.Context
import android.util.TypedValue

/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
fun dp2px(context: Context, value: Float) = (value * context.resources.displayMetrics.density + 0.5f).toInt()

/**
 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
fun px2dp(context: Context, value: Float) = (value / context.resources.displayMetrics.density + 0.5f).toInt()

/**
 * 将px值转换为sp值，保证文字大小不变
 */
fun px2sp(context: Context, value: Float) = (value / context.resources.displayMetrics.density + 0.5f).toInt()

/**
 * 将sp值转换为px值，保证文字大小不变
 */
fun sp2px(context: Context, value: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.resources.displayMetrics).toInt()

/**
 * 获取屏幕宽度
 */
fun getScreenWidth(context: Context) = (context as Activity).windowManager.defaultDisplay.width

/**
 * 获取屏幕高度
 */
fun getScreenHeight(context: Context) = (context as Activity).windowManager.defaultDisplay.height

/**
 * 获取状态栏高度
 */
fun getStatusBarHeight(context: Context) = context.resources.getIdentifier("status_bar_height", "dimen", "android").let {
    if (it > 0) context.resources.getDimensionPixelSize(it) else 0
}

/**
 * 获取导航栏高度
 */
fun getNavigationBarHeight(context: Context) = context.resources.getIdentifier("navigation_bar_height", "dimen", "android").let {
    if (it > 0) context.resources.getDimensionPixelSize(it) else 0
}