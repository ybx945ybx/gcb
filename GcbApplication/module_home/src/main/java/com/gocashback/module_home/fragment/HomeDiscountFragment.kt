package com.gocashback.module_home.fragment

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.FRAGMENT_HOME_DISCOUNT
import com.gocashback.lib_common.base.BasePagerAdapter
import com.gocashback.lib_common.base.GcbBaseFragment
import com.gocashback.lib_common.makeHomeDiscountCateDealFragment
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.DealApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.deal.DealCateItemModel
import com.gocashback.lib_common.network.model.deal.DealListIfModel
import com.gocashback.lib_common.startDealDetailActivity
import com.gocashback.lib_common.startHotDealActivity
import com.gocashback.lib_common.widget.GcbSubTitleView
import com.gocashback.module_home.R
import com.gocashback.module_home.adapter.HomeDealHotAdapter
import kotlinx.android.synthetic.main.fragment_home_discount.*

/**
 * @Author 55HAITAO
 * @Date 2019/4/8 10:53 AM
 */
@Route(path = FRAGMENT_HOME_DISCOUNT)
class HomeDiscountFragment : GcbBaseFragment() {

    private lateinit var homeDealHotAdapter: HomeDealHotAdapter

    override fun setLayoutId(): Int {
        return R.layout.fragment_home_discount
    }

    override fun initVars() {

    }

    override fun initView() {
        rycv_hot_deals.apply {
            layoutManager = LinearLayoutManager(mActivity).apply { orientation = LinearLayoutManager.HORIZONTAL }
            homeDealHotAdapter = HomeDealHotAdapter(mActivity, listOf())
            adapter = homeDealHotAdapter
        }

    }

    override fun initEvent() {
        // 解决下拉刷新冲突
        appBar.addOnOffsetChangedListener { _, verticalOffset -> content_view.isEnabled = verticalOffset >= 0 }
        // 下拉刷新
        content_view.setOnRefreshListener {
            getHotDeals()
            getDealCates()
        }
        // 查看全部
        hot_deals_sub_title.onRightClickListener = object : GcbSubTitleView.OnRightClickListener {
            override fun onRightClick(view: View) {
                startHotDealActivity(mActivity)
            }
        }

        homeDealHotAdapter.setOnItemClickListener { _, _, position ->
            startDealDetailActivity(mActivity, homeDealHotAdapter.data[position].id)
        }
        msv.setOnRetryClickListener {
            initData()
        }
    }

    override fun initData() {
        msv.showLoading()
        getHotDeals()
        getDealCates()
    }

    /**
     * 获取热门优惠数据
     */
    private fun getHotDeals() {
        createService(DealApi::class.java)
//                .getDealList(0, 20)
                .getDealPopularityList(0, 20)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<DealListIfModel>(mActivity) {
                    override fun onSuccess(t: DealListIfModel) {
                        if (t.deal != null) {
                            hot_deals_sub_title.visibility = View.VISIBLE
                            rycv_hot_deals.visibility = View.VISIBLE
                            homeDealHotAdapter.setNewData(t.deal)
                        } else {
                            hot_deals_sub_title.visibility = View.GONE
                            rycv_hot_deals.visibility = View.GONE
                        }
                    }

                    override fun onFail(code: Int, msg: String) {
                        hot_deals_sub_title.visibility = View.GONE
                        rycv_hot_deals.visibility = View.GONE

                    }
                })
    }

    /**
     * 获取优惠分类Tab
     */
    private fun getDealCates() {
        createService(DealApi::class.java)
                .getDealCateList()
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<List<DealCateItemModel>>(mActivity) {
                    override fun onSuccess(t: List<DealCateItemModel>) {
                        content_view.isRefreshing = false
                        msv.showContent()
                        val list: MutableList<DealCateItemModel> = mutableListOf()
                        list.add(DealCateItemModel().apply {
                            name = resources.getString(R.string.home_sub_title_hot_deal)
                            id = "-1"
                        })
                        list.add(DealCateItemModel().apply { name = resources.getString(R.string.deal_cate_newest) })
                        list.addAll(t)
                        setViewPager(list)

                    }

                    override fun onFail(code: Int, msg: String) {
                        msv.showError()
                        content_view.isRefreshing = false
                    }
                })

    }

    private fun setViewPager(t: List<DealCateItemModel>) {
        val tabs = arrayListOf<String>()
        val fragments = arrayListOf<Fragment>()

        for (item in t) {
            tabs.add(item.name)
            fragments.add(makeHomeDiscountCateDealFragment(item.id))

        }

        viewpager_deal.setScrollable(false)
        viewpager_deal.adapter = BasePagerAdapter(childFragmentManager, fragments, tabs)
        tablayout_deal.setupWithViewPager(viewpager_deal)

        initTabView(tabs)
    }

    private fun initTabView(tabs: List<String>) {
        for (i in tabs.indices) {
            tablayout_deal.getTabAt(i)?.apply {
                setCustomView(R.layout.view_home_deal_tab)
                customView?.findViewById<TextView>(R.id.tab_title)?.text = tabs[i]

            }

        }
    }
}