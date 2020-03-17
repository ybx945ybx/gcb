package com.gocashback.module_store.fragment

import android.support.annotation.StringDef
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.FRAGMENT_STORES
import com.gocashback.lib_common.base.GcbBaseFragment
import com.gocashback.lib_common.event.NoticeRefreshEvent
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.StoreApi
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.isLogin
import com.gocashback.lib_common.network.model.store.StoreCateItemModel
import com.gocashback.lib_common.network.model.store.StoreCateStoreIfModel
import com.gocashback.lib_common.network.model.user.NoticeUnreadIfModel
import com.gocashback.lib_common.startStoreDetailActivity
import com.gocashback.module_store.R
import com.gocashback.module_store.adapter.CateStoreAdapter
import com.gocashback.module_store.adapter.StoreCateAdapter
import com.gocashback.module_store.fragment.StoreFragment.OrderType.Companion.DEFAULT
import com.gocashback.module_store.fragment.StoreFragment.OrderType.Companion.HOT
import com.gocashback.module_store.fragment.StoreFragment.OrderType.Companion.REBATE
import kotlinx.android.synthetic.main.fragment_store.*
import org.greenrobot.eventbus.Subscribe

/**
 * @Author 55HAITAO
 * @Date 2019/4/8 2:39 PM
 */
@Route(path = FRAGMENT_STORES)
class StoreFragment : GcbBaseFragment(), View.OnClickListener {

    lateinit var storeCateAdapter: StoreCateAdapter
    lateinit var cateStoreAdapter: CateStoreAdapter
    private var orderBy = DEFAULT
    var cateId = ""

    @StringDef(DEFAULT, REBATE, HOT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class OrderType {
        companion object {
            const val DEFAULT = "orders desc" // 默认
            const val REBATE = "rebate desc"  // 返利
            const val HOT = "is_hot desc"     // 热门

        }
    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_store
    }

    override fun initVars() {
        registerEventBus()
    }

    override fun initView() {
        rycv_store_cate.apply {
            layoutManager = LinearLayoutManager(mActivity)
            storeCateAdapter = StoreCateAdapter(mutableListOf())
            adapter = storeCateAdapter
        }

        rycv_store_list.apply {
            layoutManager = LinearLayoutManager(mActivity)
            cateStoreAdapter = CateStoreAdapter(mActivity, listOf())
            adapter = cateStoreAdapter
        }

        tv_cate_default.isSelected = true
        orderBy = DEFAULT
    }

    override fun initEvent() {
        storeCateAdapter.setOnItemClickListener { _, _, position ->
            if (storeCateAdapter.curPosition == position) return@setOnItemClickListener
            page = 0
            cateId = storeCateAdapter.data[position].id
            rycv_store_list.scrollToPosition(0)
            getCateStoreList()
            storeCateAdapter.setSelectPosition(position)

        }

        cateStoreAdapter.setOnItemClickListener { _, _, position ->
            startStoreDetailActivity(mActivity, cateStoreAdapter.data[position].flag)

        }
        cateStoreAdapter.setOnLoadMoreListener({
            page++
            getCateStoreList()
        }, rycv_store_list)

        tv_cate_default.setOnClickListener(this)
        tv_cate_cash_back.setOnClickListener(this)
        tv_cate_hot.setOnClickListener(this)

    }

    override fun initData() {
        getNoticeUnread()

        createService(StoreApi::class.java)
                .getStoreCateList()
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<List<StoreCateItemModel>>(mActivity) {
                    override fun onSuccess(t: List<StoreCateItemModel>) {
                        t.let {
                            it[0].is_selected = true
                            storeCateAdapter.setNewData(it)
                            cateId = it[0].id
                            getCateStoreList()
                        }
                    }
                })

    }

    private fun getCateStoreList() {
        createService(StoreApi::class.java)
                .getCateStoreList(cateId, page, perPage, orderBy)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<StoreCateStoreIfModel>(mActivity) {
                    override fun onSuccess(t: StoreCateStoreIfModel) {
                        t.store?.let {
                            if (page == 0) {
                                cateStoreAdapter.setNewData(t.store)
                            } else {
                                cateStoreAdapter.addData(it)
                            }
                            if (page < t.page_count) cateStoreAdapter.loadMoreComplete() else cateStoreAdapter.loadMoreEnd(true)
                        }

                    }
                })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_cate_default -> if (tv_cate_default.isSelected) return else {
                orderBy = DEFAULT
                tv_cate_default.isSelected = true
                tv_cate_cash_back.isSelected = false
                tv_cate_hot.isSelected = false
            }
            R.id.tv_cate_cash_back -> if (tv_cate_cash_back.isSelected) return else {
                orderBy = REBATE
                tv_cate_default.isSelected = false
                tv_cate_cash_back.isSelected = true
                tv_cate_hot.isSelected = false
            }
            R.id.tv_cate_hot -> if (tv_cate_hot.isSelected) return else {
                orderBy = HOT
                tv_cate_default.isSelected = false
                tv_cate_cash_back.isSelected = false
                tv_cate_hot.isSelected = true
            }
        }
        page = 0
        rycv_store_list.scrollToPosition(0)
        getCateStoreList()

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