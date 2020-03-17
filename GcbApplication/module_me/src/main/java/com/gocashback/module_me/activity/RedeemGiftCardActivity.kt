package com.gocashback.module_me.activity

import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_REDEEM_GIFTCARD
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.GiftCardApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.model.giftCard.GiftCardIfModel
import com.gocashback.lib_common.startGiftCardDetailActivity
import com.gocashback.module_me.R
import com.gocashback.module_me.adapter.GiftCardAdapter
import kotlinx.android.synthetic.main.activity_redeem_gift_card.*

/**
 * @Author 55HAITAO
 * @Date 2019-06-15 15:22
 */
@Route(path = ACTIVITY_REDEEM_GIFTCARD)
class RedeemGiftCardActivity : GcbBaseActivity() {

    private lateinit var giftCardAdapter: GiftCardAdapter
    private var keyWord = "0"

    override fun setLayoutId(): Int {
        return R.layout.activity_redeem_gift_card
    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        rycv_gift_card.apply {
            layoutManager = GridLayoutManager(this@RedeemGiftCardActivity, 2)
            giftCardAdapter = GiftCardAdapter(listOf())
            adapter = giftCardAdapter
        }

    }

    override fun initEvent() {
        //
        gcbSearchView.addOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!TextUtils.isEmpty(gcbSearchView.getText())) {
                        hideSoftInput()
                        page = 0
                        getGiftCard()
                    }
                    return true
                }
                return false

            }
        })
        gcbSearchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s)) {
                    keyWord = s.toString()
                } else {
                    // 清空搜索词后列表为全部数据
                    page = 0
                    keyWord = "0"
                    getGiftCard()
                }
            }
        })
        // 刷新
        content_view.setOnRefreshListener {
            page = 0
            getGiftCard()
        }
        // 列表item点击
        giftCardAdapter.setOnItemClickListener { _, _, position ->
            startGiftCardDetailActivity(this, giftCardAdapter.data[position].id)
        }
        // 加载更多
        giftCardAdapter.setOnLoadMoreListener({
            page++
            getGiftCard()
        }, rycv_gift_card)

    }

    override fun initData() {
        msv.showLoading()
        page = 0
        getGiftCard()
    }

    /**
     * 获取返利列表数据
     */
    private fun getGiftCard() {
        if (TextUtils.isEmpty(keyWord)) return
        createService(GiftCardApi::class.java)
                .giftCard(keyWord, page, perPage)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<GiftCardIfModel>(this) {
                    override fun onSuccess(t: GiftCardIfModel) {
                        msv.showContent()
                        content_view.isRefreshing = false
                        t.data?.let {
                            if (page == 0) {
                                // 列表数据
                                giftCardAdapter.setNewData(it)
                                // 显示共搜索到几条数据
                                if (keyWord == "0") {
                                    llyt_result_count.visibility = View.GONE
                                } else {
                                    llyt_result_count.visibility = if (t.total > 0) View.VISIBLE else View.GONE
                                    tv_result.text = t.total.toString()
                                }
                            } else {
                                giftCardAdapter.addData(it)
                            }

                            if (page + 1 < t.page_count) giftCardAdapter.loadMoreComplete() else giftCardAdapter.loadMoreEnd(true)

                        }

                        if (giftCardAdapter.data.isNullOrEmpty()) {
                            msv.showEmpty(resources.getString(R.string.msv_no_result))
                        }

                    }

                    override fun onFail(code: Int, msg: String) {
                        msv.showError()
                        content_view.isRefreshing = false
                    }
                })

    }
}