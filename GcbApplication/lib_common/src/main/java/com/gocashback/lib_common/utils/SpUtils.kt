package com.gocashback.lib_common.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

const val SP_NAME = "gcb_sp"


const val SP_KEY_FIRST_OPEN = "SP_KEY_FIRST_OPEN"
const val SP_KEY_LOCALE = "SP_KEY_LOCALE"
const val SP_KEY_LANGUAGE = "SP_KEY_LANGUAGE"
const val SP_KEY_FORGET_VERIFY_MAIL = "SP_KEY_FORGET_VERIFY_MAIL"   //  忘记密码邮箱验证
const val SP_KEY_FORGET_MAIL = "SP_KEY_FORGET_MAIL"                 //  忘记密码的邮箱
const val SP_KEY_PAY_VERIFY_MAIL = "SP_KEY_PAY_VERIFY_MAIL"         //  支付密码邮箱验证
const val SP_KEY_UPDATE_VERIFY_MAIL = "SP_KEY_UPDATE_VERIFY_MAIL"   //  修改密码邮箱验证       修改密码不需要邮箱验证了
const val SP_KEY_INVITE_REWARD = "SP_KEY_INVITE_REWARD"             //  邀请奖励invite_reward
const val SP_KEY_REGISTER_REWARD = "SP_KEY_REGISTER_REWARD"         //  邀请注册奖励register_reward
const val SP_KEY_TAOBAO_PID = "SP_KEY_TAOBAO_PID"                   //  淘宝pid

fun put(context: Context?, key: String, value: Any) {
    context?.getSharedPreferences(SP_NAME, MODE_PRIVATE)?.edit()?.apply {
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            else -> putString(key, value.toString())
        }
    }?.let {
        SharedPreferencesCompat.apply(it)
    }
}

fun getString(context: Context?, key: String, default: String) = context?.getSharedPreferences(SP_NAME, MODE_PRIVATE)?.getString(key, default)
fun getInt(context: Context?, key: String, default: Int) = context?.getSharedPreferences(SP_NAME, MODE_PRIVATE)?.getInt(key, default)
fun getBoolean(context: Context?, key: String, default: Boolean) = context?.getSharedPreferences(SP_NAME, MODE_PRIVATE)?.getBoolean(key, default)
fun getFloat(context: Context?, key: String, default: Float) = context?.getSharedPreferences(SP_NAME, MODE_PRIVATE)?.getFloat(key, default)
fun getLong(context: Context?, key: String, default: Long) = context?.getSharedPreferences(SP_NAME, MODE_PRIVATE)?.getLong(key, default)

fun remove(context: Context, key: String) {
    context.getSharedPreferences(SP_NAME, MODE_PRIVATE).edit().remove(key).let {
        SharedPreferencesCompat.apply(it)
    }
}

fun clear(context: Context) {
    context.getSharedPreferences(SP_NAME, MODE_PRIVATE).edit().clear().let {
        SharedPreferencesCompat.apply(it)
    }
}

fun contains(context: Context, key: String) = context.getSharedPreferences(SP_NAME, MODE_PRIVATE).contains(key)

class SharedPreferencesCompat {

    companion object {
        private val applyMethod: Method? = findApplyMethod()

        private fun findApplyMethod(): Method? {

            try {
                val clazz: Class<SharedPreferences.Editor> = SharedPreferences.Editor::class.java
                return clazz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }
            return null

        }

        fun apply(editor: SharedPreferences.Editor) {
            try {
                applyMethod?.run {
                    invoke(editor)
                    return
                }
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            }
            editor.commit()
        }
    }

}