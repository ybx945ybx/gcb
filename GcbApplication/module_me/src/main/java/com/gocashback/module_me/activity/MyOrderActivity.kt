package com.gocashback.module_me.activity

import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.*
import com.gocashback.lib_common.base.BasePagerAdapter
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.module_me.R
import kotlinx.android.synthetic.main.activity_my_order.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-10 09:55
 */
@Route(path = ACTIVITY_MYORDER, extras = LOGIN_EXTRA)
class MyOrderActivity : GcbBaseActivity() {
    private lateinit var tabs: List<String>

    override fun setLayoutId(): Int {
        return R.layout.activity_my_order
    }

    override fun initVars() {
        tabs = listOf(resources.getString(R.string.my_orders_tab_all), resources.getString(R.string.my_orders_tab_available), resources.getString(R.string.my_orders_tab_pending))

    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        val myOrderAllFragment = makeMyOrderFragment(0)
        val myOrderAvailableFragment = makeMyOrderFragment(1)
        val myOrderPendingFragment = makeMyOrderFragment(2)
//        view_pager.setScrollable(false)
        view_pager.adapter = BasePagerAdapter(supportFragmentManager, listOf(myOrderAllFragment, myOrderAvailableFragment, myOrderPendingFragment), tabs)
        tablayout.setupWithViewPager(view_pager)

        initTabView()

    }

    private fun initTabView() {
        for (i in tabs.indices) {
            tablayout.getTabAt(i)?.apply {
                setCustomView(R.layout.view_order_tab)
                customView?.findViewById<TextView>(R.id.tab_title)?.text = tabs[i]

            }

        }
    }
}