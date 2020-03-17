package com.gocashback

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.*
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.event.NoticeRefreshEvent
import com.gocashback.lib_common.event.UpdateLocaleEvent
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.IndexApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.index.IndexIfModel
import com.gocashback.lib_common.share.ShareUtils
import com.gocashback.lib_common.utils.SP_KEY_INVITE_REWARD
import com.gocashback.lib_common.utils.SP_KEY_REGISTER_REWARD
import com.gocashback.lib_common.utils.SP_KEY_TAOBAO_PID
import com.gocashback.lib_common.utils.put
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@Route(path = ACTIVITY_MAIN)
class MainActivity : GcbBaseActivity(), View.OnClickListener {

    private var homeFragment: Fragment? = null
    private var storesFragment: Fragment? = null
    private var discoverFragment: Fragment? = null
    private var meFragment: Fragment? = null


//    companion object {
//
//        var isForeground = false
//
//    }

    @JvmField
    @Autowired(name = "position")
    var position = 0

    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initVars() {
        registerEventBus()
    }

    override fun initViews() {
        selectedFragment(position)
    }

    override fun initEvent() {
        tv_home.setOnClickListener(this)
        tv_store.setOnClickListener(this)
        tv_discover.setOnClickListener(this)
        tv_me.setOnClickListener(this)
    }

    override fun initData() {
        // 公共信息接口
        createService(IndexApi::class.java)
                .getIndex()
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<IndexIfModel>(this) {
                    override fun onSuccess(t: IndexIfModel) {
                        t.give?.let {
                            put(this@MainActivity, SP_KEY_INVITE_REWARD, it.invite_reward)
                            put(this@MainActivity, SP_KEY_REGISTER_REWARD, it.register_reward)
                        }
                        put(this@MainActivity, SP_KEY_TAOBAO_PID, if (TextUtils.isEmpty(t.pid)) "" else t.pid )

                    }
                })

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_home -> selectedFragment(0)
            R.id.tv_store -> selectedFragment(1)
            R.id.tv_discover -> selectedFragment(2)
            R.id.tv_me -> selectedFragment(3)
        }

    }

    @Synchronized
    fun selectedFragment(position: Int) {
        supportFragmentManager.beginTransaction().let { transaction ->
            hideFragment(transaction)
            tv_home.isSelected = false
            tv_store.isSelected = false
            tv_discover.isSelected = false
            tv_me.isSelected = false
            when (position) {
                0 -> {
                    getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()
                    if (homeFragment == null) {
                        homeFragment = makeHomeFragment().also {
                            transaction.add(R.id.content, it)
                        }
                    } else transaction.show(homeFragment)
                    tv_home.isSelected = true

                }
                1 -> {
                    getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()
                    if (storesFragment == null) {
                        storesFragment = makeStoresFragment().also {
                            transaction.add(R.id.content, it)
                        }
                    } else transaction.show(storesFragment)
                    tv_store.isSelected = true
                }
                2 -> {
                    getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()
                    if (discoverFragment == null) {
                        discoverFragment = makeDiscoverFragment().also {
                            transaction.add(R.id.content, it)
                        }
                    } else transaction.show(discoverFragment)
                    tv_discover.isSelected = true
                }
                3 -> {
                    getImmersionBar()?.fitsSystemWindows(false)?.transparentStatusBar()?.statusBarDarkFont(false)?.init()
                    if (meFragment == null) {
                        meFragment = makeMeFragment().also {
                            transaction.add(R.id.content, it)
                        }
                    } else transaction.show(meFragment)
                    tv_me.isSelected = true
                }
            }
            transaction.commit()

        }


    }

    private fun hideFragment(transaction: FragmentTransaction) {
        homeFragment?.let {
            transaction.hide(it)

        }
        storesFragment?.let {
            transaction.hide(it)

        }
        discoverFragment?.let {
            transaction.hide(it)

        }
        meFragment?.let {
            transaction.hide(it)

        }
    }


    override fun onResume() {
        super.onResume()
        EventBus.getDefault().post(NoticeRefreshEvent())
//        isForeground = true
    }

//    override fun onPause() {
//        isForeground = false
//        super.onPause()
//    }

    /**
     *
     * 粗暴解决fragment重叠问题
     *
     */
    override fun onSaveInstanceState(outState: Bundle?) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ShareUtils.onActivityResult(this, requestCode, resultCode, data)
    }

    @Subscribe
    fun updateLocale(updateLocaleEvent: UpdateLocaleEvent) {
        recreate()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        selectedFragment(0)
    }
}
