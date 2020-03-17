package com.gocashback.lib_common.base

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.gocashback.lib_common.inject
import com.gocashback.lib_common.language.ContextWrapper
import com.gocashback.lib_common.language.getLocale
import com.gyf.barlibrary.ImmersionBar
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

abstract class GcbBaseActivity : RxAppCompatActivity() {
    protected var activityTag = javaClass.simpleName
    protected var page = 0
    protected val perPage = 20
    private var mImmersionBar: ImmersionBar? = null

    override fun attachBaseContext(newBase: Context) {
        val context = ContextWrapper.wrap(newBase, getLocale(GcbBaseApplication.application))
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(this)
        setContentView(setLayoutId())
        if (isImmersionBarEnabled()) {
            initImmersionBar()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }  // 主题设置了白色背景，状态栏字体颜色改成黑色
        }
        initVars()
        initViews()
        initEvent()
        initData()
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    open fun isImmersionBarEnabled(): Boolean {
        return true
    }

    private fun initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this)?.apply {
            init()
        }
    }

    protected fun getImmersionBar() = mImmersionBar

    open fun initVars() {}

    open fun initViews() {}

    open fun initEvent() {}

    open fun initData() {}

    abstract fun setLayoutId(): Int

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
        mImmersionBar?.destroy()

    }

    fun hideSoftInput() {
        getSystemService(Context.INPUT_METHOD_SERVICE)?.run {
            (this as InputMethodManager).hideSoftInputFromWindow(currentFocus?.windowToken, 2)
        }
    }

    fun hideSoftInput(view: View? = null) {
        view?.let {
            getSystemService(Context.INPUT_METHOD_SERVICE)?.run {
                (this as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 2)
            }
            return@let
        }


    }

    fun hideSoftInputDely(view: View? = null) {
        view?.let {
            view.postDelayed(Runnable {
                getSystemService(Context.INPUT_METHOD_SERVICE)?.run {
                    (this as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 2)
                }
            }, 150)
            return@let
        }
        currentFocus?.postDelayed({
            getSystemService(Context.INPUT_METHOD_SERVICE)?.run {
                (this as InputMethodManager).hideSoftInputFromWindow(currentFocus?.windowToken, 2)
            }
        }, 150)

    }

    protected fun registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    /**
     * 回收dlg
     */
    protected fun dismissDlgs(vararg dlgs: Dialog?) {
        for (dlg in dlgs) {
            dlg?.dismiss()
        }
    }

    fun finishDelay(delay: Long = 1500) {
        Observable.timer(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate {
                    finish()
                }
                .subscribe()
    }
}