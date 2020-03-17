package com.gocashback.lib_common.network

import com.gocashback.lib_common.network.model.APIException
import com.gocashback.lib_common.network.model.ApiModel
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/**
 * @Author 55HAITAO
 * @Date 2019-05-09 15:22
 */
class ResponseTransformer {
    companion object {
        fun <T> handleResult(): ObservableTransformer<ApiModel<T>, T> {
            return ObservableTransformer { upstream ->
                upstream
                        .flatMap(ResponseFunction())
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun <T> handleLoginResult(): ObservableTransformer<Response<T>, T> {

//            ObservableTransformer<Response<T>, T> { upstream1 ->
//                upstream1
//                        .flatMap(LoginResponseFunction())
//                        .subscribeOn(Schedulers.io())
//                        .unsubscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//            }

            return ObservableTransformer { upstream ->
                upstream
                        .flatMap(LoginResponseFunction())
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }


    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
    </T> */
    private class ResponseFunction<T> : Function<ApiModel<T>, ObservableSource<T>> {

        @Throws(Exception::class)
        override fun apply(tResponse: ApiModel<T>): ObservableSource<T> {
            val code = tResponse.code
            val message = tResponse.msg
            return if (code == 200) {
                Observable.just(tResponse.getData())
            } else {
                Observable.error(APIException(code, message))
            }
        }
    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
    </T> */
    private class LoginResponseFunction<T> : Function<Response<T>, ObservableSource<T>> {

        @Throws(Exception::class)
        override fun apply(tResponse: Response<T>): ObservableSource<T> {
            val code = tResponse.code()
            val message = tResponse.message()
            var headers = tResponse.headers()
            return if (code == 200) {   //  这一步是返回数据成功了，还要判断接口返回的code是不是200
                headers.get("key")?.let {
                    setUserKey(it)
                }
                headers.get("secret")?.let {
                    setUserSecret(it)
                }
                Observable.just(tResponse.body())
            } else {
                Observable.error(APIException(code, message))
            }
        }
    }
}