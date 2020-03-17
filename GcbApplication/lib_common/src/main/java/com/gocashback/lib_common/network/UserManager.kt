package com.gocashback.lib_common.network

import android.text.TextUtils
import com.gocashback.lib_common.base.GcbBaseApplication
import com.gocashback.lib_common.network.model.user.UserIfModel
import com.gocashback.lib_common.utils.getString
import com.gocashback.lib_common.utils.put
import com.gocashback.lib_common.utils.toJson
import com.gocashback.lib_common.utils.toObject

/**
 * @Author 55HAITAO
 * @Date 2019-06-03 20:49
 */
private const val USER = "user"
private const val USER_KEY = "user_key"
private const val USER_SECRET = "user_secret"

fun getUserKey() = getString(GcbBaseApplication.application, USER_KEY, "")

fun setUserKey(key: String) {
    put(GcbBaseApplication.application, USER_KEY, key)
}

fun getUserSecret() = getString(GcbBaseApplication.application, USER_SECRET, "")

fun setUserSecret(secret: String) {
    put(GcbBaseApplication.application, USER_SECRET, secret)
}
//    }

private var user: UserIfModel? = null

/**
 * 设置用户
 *
 * @param userObject
 */
fun setUser(userObject: UserIfModel?) {
    if (null == userObject) return
    user = userObject
    put(GcbBaseApplication.application, USER, toJson(userObject))
}

/**
 * 获取用户信息
 *
 * @return
 */
fun getUser(): UserIfModel? {
    if (null != user)
        return user
    val strUser = getString(GcbBaseApplication.application, USER, "")
    if (TextUtils.isEmpty(strUser))
        return null
    user = toObject(strUser, UserIfModel::class.java)
    return user

}

/**
 * 清空用户信息
 */
fun clearUser() {
    user = null
    put(GcbBaseApplication.application, USER, "")
    setUserKey("")
    setUserSecret("")
}

/**
 * 判断是否登录
 *
 * @return
 */
fun isLogin() = !(getUserKey().isNullOrEmpty() || getUserSecret().isNullOrEmpty() || getUser() == null)