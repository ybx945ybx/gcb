package com.gocashback.module_home.fragment

import android.support.v4.view.ViewPager
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.FRAGMENT_HOME
import com.gocashback.lib_common.base.BasePagerAdapter
import com.gocashback.lib_common.base.GcbBaseFragment
import com.gocashback.lib_common.event.NoticeRefreshEvent
import com.gocashback.lib_common.makeHomeDiscountFragment
import com.gocashback.lib_common.makeHomeStoreFragment
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.isLogin
import com.gocashback.lib_common.network.model.user.NoticeUnreadIfModel
import com.gocashback.module_home.R
import kotlinx.android.synthetic.main.fragment_home.*
import org.greenrobot.eventbus.Subscribe

/**
 * @Author 55HAITAO
 * @Date 2019/4/8 10:52 AM
 */
@Route(path = FRAGMENT_HOME)
class HomeFragment : GcbBaseFragment() {

    private lateinit var tabs: List<String>

    override fun setLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initVars() {
        tabs = listOf(resources.getString(com.gocashback.module_home.R.string.home_tab_stores), resources.getString(R.string.home_tab_deals))
        registerEventBus()
    }

    override fun initView() {
        val homeStoreFragment = makeHomeStoreFragment()
        val homeDealFragment = makeHomeDiscountFragment()
        view_pager.setScrollable(false)
        view_pager.adapter = BasePagerAdapter(childFragmentManager, listOf(homeStoreFragment, homeDealFragment), tabs)
        tablayout.setupWithViewPager(view_pager)

        initTabView()
    }

    private fun initTabView() {
        for (i in tabs.indices) {
            tablayout.getTabAt(i)?.apply {
                setCustomView(R.layout.view_home_tab)
                customView?.findViewById<TextView>(R.id.tab_title)?.text = tabs[i]

            }

        }
    }

    override fun initEvent() {
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                view_bottom_shadow.visibility = if (position == 0) View.GONE else View.VISIBLE
            }
        })

    }

    override fun initData() {
        getNoticeUnread()

    }

    private fun getNoticeUnread() {
        if (isLogin()) {
            createService(UserApi::class.java)
                    .noticeUnread()
                    .compose(ResponseTransformer.handleResult())
                    .compose(bindToLifecycle())
                    .subscribe(object : BaseObserver<NoticeUnreadIfModel>(mActivity) {
                        override fun onSuccess(t: NoticeUnreadIfModel) {
                            gcb_search_view.setMessageNum(t.total)

                        }
                    })
        } else {
            gcb_search_view.setMessageNum(0)
        }
    }


    @Subscribe
    fun onNoticeRefresh(noticeRefreshEvent: NoticeRefreshEvent) {
        getNoticeUnread()
    }
}