package com.gocashback.lib_common.network

import android.app.Activity
import android.net.ParseException
import android.text.TextUtils
import com.gocashback.lib_common.network.model.APIException
import com.gocashback.lib_common.utils.show
import com.google.gson.JsonParseException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * @Author 55HAITAO
 * @Date 2019-05-09 15:28
 */
abstract class BaseObserver<T>(private var mActivity: Activity?) : Observer<T> {
//    private var mActivity: GcbBaseActivity? = null
//    private var mContext: Context? = null

//    fun BaseObserver(activity: GcbBaseActivity): ??? {
//        mActivity = activity
//    }
//
//    fun BaseObserver(context: Context): ??? {
//        mContext = context
//    }

    override fun onSubscribe(disposable: Disposable) {

    }

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onError(throwable: Throwable) {
        if (throwable is APIException) {
            // 服务器异常处理
            val t = throwable as APIException
            apiExceptionHandle(t)
            onFail(t.code, t.msg)
        } else if (throwable is JsonParseException
                || throwable is JSONException
                || throwable is ParseException) {
            // 解析异常
            showToast("数据解析异常")
            onFail(-1, "数据解析异常")

        } else if (throwable is UnknownHostException
                || throwable is SocketTimeoutException
                || throwable is TimeoutException
                || throwable is ConnectException) {
            // 网络连接异常
            showToast("网络异常，请稍后重试")
            onFail(-1, "网络异常，请稍后重试")

        } else if (throwable is javax.net.ssl.SSLException) {
            // 证书异常
            showToast("网络证书有问题，请稍后重试")
            onFail(-1, "网络证书有问题，请稍后重试")

        } else {
            // 未知异常
//            Logger.e(throwable, throwable.message)
            showToast("出了一点儿问题")
            onFail(-1, "出了一点儿问题")

        }
    }

    override fun onComplete() {

    }

    /**
     * 针对接口返回code进行统一处理
     *
     * @param e 接口异常
     */
    private fun apiExceptionHandle(e: APIException) {
        if (!TextUtils.isEmpty(e.msg)) {
            showToast(e.msg)
        }
//        when (e.code) {
//            APICodeConstant.LOGIN_INVALID // 登录失效
//            -> {
//                EventBus.getDefault().post(LoginStateChangedEvent(false))
//                UserManager.getInstance().clearUser()
//            }
//            APICodeConstant.NEED_LOGIN // 需要登录
//            -> if (mActivity != null) {
//                QuickLoginActivity.launch(mActivity)
//            } else if (mContext != null) {
//                QuickLoginActivity.launch(mContext)
//            }
//            APICodeConstant.NEED_BIND_PHONE // 需要绑定手机号
//            -> FirstBindPhoneActivity.launch(mActivity)
//        }
    }

    abstract fun onSuccess(t: T)

    open fun onFail(code: Int, msg: String) {

    }

    //    /**
//     * 如果Activity不为空，则使用ToastPopupWindow
//     * 否则，使用普通toast
//     *
//     * @param toastType 类型
//     * @param msg       文本
//     */
    private fun showToast(msg: String) {
//        if (mActivity != null) {
        mActivity?.let { show(mActivity, msg) }
//        }
//        else if (mContext != null) {
//            show(mContext, msg)
//        }
    }
}