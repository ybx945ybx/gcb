package com.gocashback.module_me.activity

import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_COLLECT
import com.gocashback.lib_common.LOGIN_EXTRA
import com.gocashback.lib_common.base.BasePagerAdapter
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.makeMyCollectionDealsFragment
import com.gocashback.lib_common.makeMyCollectionStoresFragment
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.widget.GcbHeadView
import com.gocashback.module_me.R
import com.gocashback.module_me.event.CheckAllSelectEvent
import com.gocashback.module_me.fragment.MyCollectionDealsFragment
import com.gocashback.module_me.fragment.MyCollectionStoresFragment
import kotlinx.android.synthetic.main.activity_my_collection.*
import org.greenrobot.eventbus.Subscribe

/**
 * @Author 55HAITAO
 * @Date 2019-06-12 16:54
 */
@Route(path = ACTIVITY_COLLECT, extras = LOGIN_EXTRA)
class MyCollectionActivity : GcbBaseActivity() {
    private lateinit var tabs: List<String>
    private lateinit var myCollectionStoresFragment: MyCollectionStoresFragment
    private lateinit var myCollectionDealsFragment: MyCollectionDealsFragment

    override fun setLayoutId(): Int {
        return R.layout.activity_my_collection
    }

    override fun initVars() {
        tabs = listOf(resources.getString(R.string.my_collection_tab_stores), resources.getString(R.string.my_collection_tab_deals))
        registerEventBus()
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        myCollectionStoresFragment = makeMyCollectionStoresFragment() as MyCollectionStoresFragment
        myCollectionDealsFragment = makeMyCollectionDealsFragment() as MyCollectionDealsFragment
//        view_pager.setScrollable(false)
        view_pager.adapter = BasePagerAdapter(supportFragmentManager, listOf(myCollectionStoresFragment, myCollectionDealsFragment), tabs)
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
                if (clyt_bottom.visibility == View.VISIBLE) {
                    clyt_bottom.visibility = View.GONE
                    gcbHeadView.setRightText(resources.getString(R.string.my_collection_edit))
                    myCollectionStoresFragment.setEditable(false)
                    myCollectionDealsFragment.setEditable(false)

                } else {
                    clyt_bottom.visibility = View.VISIBLE
                    gcbHeadView.setRightText(resources.getString(R.string.my_collection_cancel))
                    myCollectionStoresFragment.setEditable(true)
                    myCollectionDealsFragment.setEditable(true)

                }

            }
        }

        tv_select_all.setOnClickListener {
            if (tv_select_all.isSelected) {
                tv_select_all.isSelected = false
                if (view_pager.currentItem == 0) myCollectionStoresFragment.setAllSelect(false) else myCollectionDealsFragment.setAllSelect(false)
            } else {
                tv_select_all.isSelected = true
                if (view_pager.currentItem == 0) myCollectionStoresFragment.setAllSelect(true) else myCollectionDealsFragment.setAllSelect(true)

            }
        }

        tv_delete.setOnClickListener {
            val ids = if (view_pager.currentItem == 0) myCollectionStoresFragment.getSelectedIds() else myCollectionDealsFragment.getSelectedIds()
            if (TextUtils.isEmpty(ids)) return@setOnClickListener
            createService(UserApi::class.java)
                    .collectionDelete(ids)
                    .compose(ResponseTransformer.handleResult())
                    .compose(bindToLifecycle())
                    .subscribe(object : BaseObserver<Any>(this) {
                        override fun onSuccess(t: Any) {
                            clyt_bottom.visibility = View.GONE
                            gcbHeadView.setRightText(resources.getString(R.string.my_collection_edit))
                            myCollectionStoresFragment.setEditable(false)
                            myCollectionDealsFragment.setEditable(false)
                            tv_select_all.isSelected = false
                            myCollectionStoresFragment.refresh()
                            myCollectionDealsFragment.refresh()


//                get
                        }
                    })
        }

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (clyt_bottom.visibility == View.VISIBLE) {
                    tv_select_all.isSelected = false
                    myCollectionStoresFragment.setAllSelect(false)
                    myCollectionDealsFragment.setAllSelect(false)
                }
            }

        })

    }

    @Subscribe
    fun checkAllSelect(checkAllSelectEvent: CheckAllSelectEvent) {
        var isAllSelect = true
        for (item in
        if (view_pager.currentItem == 0) myCollectionStoresFragment.myCollectionStoresAdapter.data
        else myCollectionDealsFragment.myCollectionDealsAdapter.data) {
            if (!item.select) {
                isAllSelect = false
                break
            }
        }
        tv_select_all.isSelected = isAllSelect
    }
}