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
 * @Date 2019-06-12 15:41
 */
@Route(path = ACTIVITY_MESSAGE, extras = LOGIN_EXTRA)
class MessageActivity : GcbBaseActivity() {

    private lateinit var tabs: List<String>

    override fun setLayoutId(): Int {
        return R.layout.activity_message
    }

    override fun initVars() {
//        tabs = listOf(resources.getString(R.string.message_tab_deal), resources.getString(R.string.message_tab_system))
        tabs = listOf(resources.getString(R.string.message_tab_system))

    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

//        val messageDealFragment = makeMessageDealFragment()
        val messageSystemFragment = makeMessageSystemFragment()
        view_pager.adapter = BasePagerAdapter(supportFragmentManager, listOf(messageSystemFragment), tabs)
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