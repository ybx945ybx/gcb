package com.gocashback.module_me.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_GIFTCARD
import com.gocashback.lib_common.LOGIN_EXTRA
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.user.UserGiftCardIfModel
import com.gocashback.lib_common.startRedeemGiftCardActivity
import com.gocashback.lib_common.utils.show
import com.gocashback.module_me.adapter.MyGiftCardAdapter
import kotlinx.android.synthetic.main.activity_gift_card.*
import android.support.v7.widget.SimpleItemAnimator
import com.gocashback.module_me.R


/**
 * @Author 55HAITAO
 * @Date 2019-06-15 14:55
 */
@Route(path = ACTIVITY_GIFTCARD, extras = LOGIN_EXTRA)
class GiftCardActivity : GcbBaseActivity() {

    private lateinit var myGiftCardAdapter: MyGiftCardAdapter

    override fun setLayoutId(): Int {
        return R.layout.activity_gift_card
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(com.gocashback.module_me.R.color.white)?.statusBarDarkFont(true)?.init()

        rycv_my_gift_card.apply {
            ( itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false  //
            layoutManager = LinearLayoutManager(this@GiftCardActivity)
            myGiftCardAdapter = MyGiftCardAdapter(this@GiftCardActivity, listOf())
            adapter = myGiftCardAdapter
        }

    }

    override fun initEvent() {
        // 刷新
        content_view.setOnRefreshListener {
            page = 0
            getMyGiftCard()
        }
        // 列表item点击
        myGiftCardAdapter.setOnItemChildClickListener { _, view, position ->
            if (view.id == R.id.tv_mark_as_spent) {
                createService(UserApi::class.java)
                        .giftCardUse(myGiftCardAdapter.data[position].id)
                        .compose(ResponseTransformer.handleResult())
                        .compose(bindToLifecycle())
                        .subscribe(object : BaseObserver<Any>(this) {
                            override fun onSuccess(t: Any) {
                                myGiftCardAdapter.data[position].is_available = 1
                                myGiftCardAdapter.notifyItemChanged(position)

                            }
                        })
            } else {   // tv_gift_cards_resend
                if (!myGiftCardAdapter.data[position].isOnCount)
                    createService(UserApi::class.java)
                            .giftCardSendEmail(myGiftCardAdapter.data[position].id)
                            .compose(ResponseTransformer.handleResult())
                            .compose(bindToLifecycle())
                            .subscribe(object : BaseObserver<Any>(this) {
                                override fun onSuccess(t: Any) {
                                    show(this@GiftCardActivity, "The E-Gift Card Code has been sent!")
                                    myGiftCardAdapter.startCount(position)
                                }
                            })
            }
        }
//        myGiftCardAdapter.setOnItemClickListener { adapter, view, position ->
//            startGiftCardDetailActivity(this, myGiftCardAdapter.data[position].id)
//        }
        // 加载更多
        myGiftCardAdapter.setOnLoadMoreListener({
            page++
            getMyGiftCard()
        }, rycv_my_gift_card)
        // 兑换礼品卡
        tv_redeem_a_gift_card.setOnClickListener {
            startRedeemGiftCardActivity(this)
        }

        msv.setOnRetryClickListener { startRedeemGiftCardActivity(this@GiftCardActivity) }
    }

    override fun initData() {
        msv.showLoading()
        page = 0
        getMyGiftCard()
    }

    /**
     * 获取返利列表数据
     */
    private fun getMyGiftCard() {
        createService(UserApi::class.java)
                .giftCard(page, perPage)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<UserGiftCardIfModel>(this) {
                    override fun onSuccess(t: UserGiftCardIfModel) {
                        msv.showContent()
                        tv_redeem_a_gift_card.visibility = View.VISIBLE
                        content_view.isRefreshing = false
                        t.data?.let {
                            if (page == 0) {
                                // 列表数据
                                myGiftCardAdapter.setNewData(it)
                            } else {
                                myGiftCardAdapter.addData(it)
                            }

                            if (page + 1 < t.page_count) myGiftCardAdapter.loadMoreComplete() else myGiftCardAdapter.loadMoreEnd(true)

                        }

                        if (myGiftCardAdapter.data.isNullOrEmpty()) {
                            msv.showEmpty(resources.getString(R.string.msv_no_gift_card), resources.getString(R.string.msv_empty_gift_card))
                            tv_redeem_a_gift_card.visibility = View.GONE

                        }

                    }

                    override fun onFail(code: Int, msg: String) {
                        msv.showError()
                        content_view.isRefreshing = false
                    }
                })

    }


    override fun onDestroy() {
        myGiftCardAdapter.removeAllCount()
        super.onDestroy()
    }

}