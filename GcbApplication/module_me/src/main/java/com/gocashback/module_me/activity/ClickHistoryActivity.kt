package com.gocashback.module_me.activity

import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_RECORD
import com.gocashback.lib_common.LOGIN_EXTRA
import com.gocashback.lib_common.base.BasePagerAdapter
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.makeClickHistoryDealsFragment
import com.gocashback.lib_common.makeClickHistoryStoresFragment
import com.gocashback.module_me.R
import kotlinx.android.synthetic.main.activity_my_order.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-13 09:49
 */
@Route(path = ACTIVITY_RECORD, extras = LOGIN_EXTRA)
class ClickHistoryActivity : GcbBaseActivity() {

    private lateinit var tabs: List<String>

    override fun setLayoutId(): Int {
        return R.layout.activity_click_history
    }

    override fun initVars() {
        tabs = listOf(resources.getString(R.string.click_history_tab_stores), resources.getString(R.string.click_history_tab_deals))

    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        val clickHistoryStoresFragment = makeClickHistoryStoresFragment()
        val clickHistoryDealsFragment = makeClickHistoryDealsFragment()
//        view_pager.setScrollable(false)
        view_pager.adapter = BasePagerAdapter(supportFragmentManager, listOf(clickHistoryStoresFragment, clickHistoryDealsFragment), tabs)
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