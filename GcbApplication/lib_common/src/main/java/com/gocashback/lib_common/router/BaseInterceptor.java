package com.gocashback.lib_common.router;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;

import static com.gocashback.lib_common.RouterPathKt.ACTIVITY_LOGIN;
import static com.gocashback.lib_common.RouterPathKt.LOGIN_EXTRA;
import static com.gocashback.lib_common.network.UserManagerKt.isLogin;

/**
 * @Author 55HAITAO
 * @Date 2019-06-27 14:17
 */
@Interceptor(priority = 1, name = "重新分组进行拦截")
public class BaseInterceptor implements IInterceptor {

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        if (postcard.getExtra() == LOGIN_EXTRA) {
            if (isLogin()) {
                callback.onContinue(postcard);
            } else {
                callback.onInterrupt(null);
                ARouter.getInstance().build(ACTIVITY_LOGIN).navigation();
            }
        } else {
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {

    }
}
