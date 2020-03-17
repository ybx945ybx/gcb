package com.gocashback.lib_common.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gocashback.lib_common.inject
import com.trello.rxlifecycle2.components.support.RxFragment
import org.greenrobot.eventbus.EventBus

/**
 * @Author 55HAITAO
 * @Date 2019/4/8 11:10 AM
 */
abstract class GcbBaseFragment : RxFragment() {
    var mActivity: Activity? = null
    private var mRootView: View? = null
    var fragmentTag = javaClass.simpleName
    var page = 0
    val perPage = 20

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(setLayoutId(), container, false)
        initVars()
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
    }

    protected abstract fun setLayoutId(): Int

    open fun initView() {}

    open fun initVars() {}

    open fun initEvent() {}

    open fun initData() {}

    protected fun registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }
}