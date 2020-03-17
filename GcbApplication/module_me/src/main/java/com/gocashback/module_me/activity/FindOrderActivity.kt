package com.gocashback.module_me.activity

import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.*
import com.gocashback.lib_common.base.BasePagerAdapter
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.widget.GcbHeadView
import com.gocashback.module_me.R
import kotlinx.android.synthetic.main.activity_find_order.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-10 15:06
 */
@Route(path = ACTIVITY_FINDORDER, extras = LOGIN_EXTRA)
class FindOrderActivity : GcbBaseActivity() {
    private lateinit var tabs: List<String>

    override fun setLayoutId(): Int {
        return R.layout.activity_find_order
    }

    override fun initVars() {
        tabs = listOf(resources.getString(R.string.find_my_order_tab_all)
                , resources.getString(R.string.find_my_order_tab_pending)
                , resources.getString(R.string.find_my_order_tab_reviewing)
                , resources.getString(R.string.find_my_order_tab_invaild)
                , resources.getString(R.string.find_my_order_tab_added)
                , resources.getString(R.string.find_my_order_tab_closed)
        )

    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()
//        0全部，1待审核,2待确认，3驳回，4已添加，5关闭
        val findOrderAllFragment = makeFindOrderFragment(0)
        val findOrderPendingFragment = makeFindOrderFragment(1)
        val findOrderReviewingFragment = makeFindOrderFragment(2)
        val findOrderInvaildFragment = makeFindOrderFragment(3)
        val findOrderAddedFragment = makeFindOrderFragment(4)
        val findOrderClosedFragment = makeFindOrderFragment(5)
//        view_pager.setScrollable(false)
        view_pager.adapter = BasePagerAdapter(supportFragmentManager, listOf(findOrderAllFragment, findOrderPendingFragment, findOrderReviewingFragment, findOrderInvaildFragment, findOrderAddedFragment, findOrderClosedFragment), tabs)
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

    override fun initEvent() {
        gcbHeadView.onRightClickListener = object : GcbHeadView.OnRightClickListener {
            override fun onRightClick() {
                startFindOrderSubmitActivity(this@FindOrderActivity)
            }
        }

    }
}