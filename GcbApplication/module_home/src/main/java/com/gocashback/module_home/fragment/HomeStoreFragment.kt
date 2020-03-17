package com.gocashback.module_home.fragment

import android.support.constraint.ConstraintLayout
import android.support.constraint.Group
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.gocashback.lib_common.*
import com.gocashback.lib_common.base.GcbBaseApplication
import com.gocashback.lib_common.base.GcbBaseFragment
import com.gocashback.lib_common.event.LoginChangeEvent
import com.gocashback.lib_common.imageload.loadImage
import com.gocashback.lib_common.language.LOCALE_CHINESE
import com.gocashback.lib_common.language.getLanguage
import com.gocashback.lib_common.model.JumpIfModel
import com.gocashback.lib_common.network.BaseObserver
import com.gocashback.lib_common.network.ResponseTransformer
import com.gocashback.lib_common.network.api.AppConfigApi
import com.gocashback.lib_common.network.api.IndexApi
import com.gocashback.lib_common.network.api.StoreApi
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.createService
import com.gocashback.lib_common.network.isLogin
import com.gocashback.lib_common.network.model.index.IndexAdvertIfModel
import com.gocashback.lib_common.network.model.index.IndexAdvertItemModel
import com.gocashback.lib_common.network.model.index.IndexChannelItemModel
import com.gocashback.lib_common.network.model.store.StoreDetailIfModel
import com.gocashback.lib_common.network.model.store.StoreItemModel
import com.gocashback.lib_common.network.model.store.StoreListIfModel
import com.gocashback.lib_common.network.model.user.CollectionIfModel
import com.gocashback.lib_common.network.model.user.CollectionItemModel
import com.gocashback.lib_common.utils.JsonUtils
import com.gocashback.lib_common.utils.jump
import com.gocashback.lib_common.widget.GcbImageView
import com.gocashback.lib_common.widget.GcbSubTitleView
import com.gocashback.module_home.R
import com.gocashback.module_home.adapter.HomeStoreAdapter
import com.gocashback.module_home.adapter.HomeStoreChannelAdapter
import com.gocashback.module_home.adapter.HomeStoreFavoriteAdapter
import com.gocashback.module_home.adapter.HomeStoreRecommendAdapter
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_home_store.*
import org.greenrobot.eventbus.Subscribe

/**
 * 首页商家tab页
 *
 * @Author 55HAITAO
 * @Date 2019/4/8 10:53 AM
 */
@Route(path = FRAGMENT_HOME_STORE)
class HomeStoreFragment : GcbBaseFragment() {
    // 头布局
    private lateinit var headView: View
    // banner
    private lateinit var bannerContainer: ConstraintLayout
    private lateinit var convenientBanner: ConvenientBanner<IndexAdvertItemModel>
    // 快速入口
    private lateinit var rycvChannel: RecyclerView
    private lateinit var homeStoreChannelAdapter: HomeStoreChannelAdapter
    // 超级返利
    private lateinit var supperContainer: ConstraintLayout
    private lateinit var supperStoreCover: GcbImageView
    private lateinit var supperStoreName: TextView
    private lateinit var supperStoreRebate: TextView
    // 独立日大促
    private lateinit var bigSaleContainer: ConstraintLayout
    private lateinit var bigSaleCover: GcbImageView
    // 推荐商家
    private lateinit var titleRecommend: GcbSubTitleView
    private lateinit var rycvRecommend: RecyclerView
    private lateinit var homeStoreRecommendAdapter: HomeStoreRecommendAdapter
    // 收藏商家
    private lateinit var titleFavorite: GcbSubTitleView
    private lateinit var rycvFavorite: RecyclerView
    private lateinit var homeStoreFavoriteAdapter: HomeStoreFavoriteAdapter
    // 热门商家
    private lateinit var homeStoreAdapter: HomeStoreAdapter

    override fun setLayoutId(): Int {
        return R.layout.fragment_home_store
    }

    override fun initVars() {
        registerEventBus()
    }

    override fun initView() {
        headView = LayoutInflater.from(mActivity).inflate(R.layout.layout_home_store_head, null)
        // banner
        bannerContainer = headView.findViewById(R.id.banner_container)
        convenientBanner = headView.findViewById(R.id.hot_store_banner)
        // 快速入口
        rycvChannel = headView.findViewById<RecyclerView>(R.id.rycv_channel).apply {
            layoutManager = GridLayoutManager(activity, 4)
            homeStoreChannelAdapter = HomeStoreChannelAdapter(listOf())
            homeStoreChannelAdapter.setOnItemClickListener { _, _, position ->
                //                show(activity, "跳转相应频道")
                when (homeStoreChannelAdapter.data[position].path) {
                    "stores" -> {
                    }
                    "deals" -> {
                    }
                    "travel" -> {
                        startWebviewActivity(mActivity, homeStoreChannelAdapter.data[position].url)
                    }
                    "guide" -> {
                    }
                    "taobao" -> {
                        startWebviewActivity(mActivity, homeStoreChannelAdapter.data[position].url)
                    }
                    "aliexpress" -> {
                    }
                    "usercenter/giftcards" -> {
                        startRedeemGiftCardActivity(mActivity)
                    }
                    "usercenter/referral" -> {
                        startInviteActivity(mActivity)
                    }
                }
            }
            adapter = homeStoreChannelAdapter
        }
        // 超级返利
        supperContainer = headView.findViewById(R.id.supper_container)
        supperStoreCover = headView.findViewById(R.id.iv_store_cover)
        supperStoreName = headView.findViewById(R.id.tv_store_name)
        supperStoreRebate = headView.findViewById(R.id.tv_store_cash_back)
        // 独立日大促
        bigSaleContainer = headView.findViewById(R.id.big_sale_container)
        bigSaleCover = headView.findViewById(R.id.iv_big_sale_cover)
        // 推荐商家
        titleRecommend = headView.findViewById(R.id.recommend_stores_sub_title)
        rycvRecommend = headView.findViewById<RecyclerView>(R.id.rycv_recommend_stores).apply {
            layoutManager = LinearLayoutManager(activity).apply { orientation = LinearLayoutManager.HORIZONTAL }
            homeStoreRecommendAdapter = HomeStoreRecommendAdapter(mActivity, listOf())
            homeStoreRecommendAdapter.setOnItemClickListener { _, _, position ->
                startStoreDetailActivity(activity, homeStoreRecommendAdapter.data[position].flag)
            }
            adapter = homeStoreRecommendAdapter
        }
        // 收藏商家
        titleFavorite = headView.findViewById(R.id.favorite_stores_sub_title)
        rycvFavorite = headView.findViewById<RecyclerView>(R.id.rycv_favorite_stores).apply {
            layoutManager = LinearLayoutManager(activity).apply { orientation = LinearLayoutManager.HORIZONTAL }
            homeStoreFavoriteAdapter = HomeStoreFavoriteAdapter(mActivity, listOf())
            homeStoreFavoriteAdapter.setOnItemClickListener { _, _, position ->
                startStoreDetailActivity(activity, homeStoreFavoriteAdapter.data[position].flag)
            }
            adapter = homeStoreFavoriteAdapter
        }
        // 添加头布局
        homeStoreAdapter = HomeStoreAdapter(mActivity, listOf())
        homeStoreAdapter.addHeaderView(headView)
        // 热门商家
        rycv_home_store.apply {
            layoutManager = LinearLayoutManager(mActivity)
            adapter = homeStoreAdapter
        }

    }

    override fun initEvent() {
        // 刷新
        content_view.setOnRefreshListener {
            page = 0
            getHomeStoreData()
        }
        // 加载更多
        homeStoreAdapter.setOnLoadMoreListener({ loadMoreData() }, rycv_home_store)

        homeStoreAdapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.left_discount -> startDealDetailActivity(mActivity, homeStoreAdapter.data[position].deal?.get(0)?.id
                        ?: "")
                R.id.right_discount -> startDealDetailActivity(mActivity, homeStoreAdapter.data[position].deal?.get(1)?.id
                        ?: "")
                R.id.iv_store_cover -> startStoreDetailActivity(mActivity, homeStoreAdapter.data[position].flag)
                R.id.tv_store_cash_back -> startStoreDetailActivity(mActivity, homeStoreAdapter.data[position].flag)
                R.id.tv_store_name -> startStoreDetailActivity(mActivity, homeStoreAdapter.data[position].flag)

            }
        }

        msv.setOnRetryClickListener {
            initData()

        }

    }

    /**
     * 加载更多（热门商家）
     */
    private fun loadMoreData() {
        page++
        getHotStores()
    }

    override fun initData() {
        msv.showLoading()
        page = 0
        getHomeStoreData()

    }

    /**
     * 获取首页数据
     */
    private fun getHomeStoreData() {

        // 广告位
//        getBanner()
        // 分类频道
        getChannel()
        // 超级返利
        getSupperCash()
        // 独立日大促
        getBigSale()
        // 推荐商家
        getRecommendStores()
        // 收藏商家
        if (isLogin()) {
            getFavoriteStores()
        } else {
            titleFavorite.visibility = View.GONE
            rycvFavorite.visibility = View.GONE
        }
        // 热门商家
        getHotStores()
    }

    /**
     * 获取Banner数据
     */
    private fun getBanner() {
        createService(IndexApi::class.java).getIndexAdvert(if (LOCALE_CHINESE == getLanguage(GcbBaseApplication.application)) 50 else 49)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<IndexAdvertIfModel>(null) {
                    override fun onSuccess(t: IndexAdvertIfModel) {
                        setBannerView(t.ad)
                    }

                    override fun onFail(code: Int, msg: String) {
                        setBannerView(null)

                    }
                })
    }

    /**
     * 获取快速入口数据
     */
    private fun getChannel() {
        createService(IndexApi::class.java).getIndexChannel()
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<List<IndexChannelItemModel>>(null) {
                    override fun onSuccess(t: List<IndexChannelItemModel>) {
                        setChannelView(t)
                    }

                    override fun onFail(code: Int, msg: String) {
                        setChannelView(null)

                    }
                })
    }

    /**
     * 获取超级返利数据
     */
    private fun getSupperCash() {
        createService(IndexApi::class.java).getIndexAdvert(if (LOCALE_CHINESE == getLanguage(GcbBaseApplication.application)) 56 else 55)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<IndexAdvertIfModel>(null) {
                    override fun onSuccess(t: IndexAdvertIfModel) {
                        setSupperView(t.ad)
                    }

                    override fun onFail(code: Int, msg: String) {
                        setSupperView(null)

                    }
                })
    }

    /**
     * 获取独立日大促数据
     */
    private fun getBigSale() {
        createService(IndexApi::class.java).getIndexAdvert(if (LOCALE_CHINESE == getLanguage(GcbBaseApplication.application)) 68 else 69)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<IndexAdvertIfModel>(null) {
                    override fun onSuccess(t: IndexAdvertIfModel) {
                        setBigSaleView(t.ad)
                    }

                    override fun onFail(code: Int, msg: String) {
                        setBigSaleView(null)

                    }
                })
    }


    /**
     * 获取推荐商家数据
     */
    private fun getRecommendStores() {
        createService(StoreApi::class.java).getStoreList(2, 0, 6)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<StoreListIfModel>(null) {
                    override fun onSuccess(t: StoreListIfModel) {
                        setRecommendView(t.store)
                    }

                    override fun onFail(code: Int, msg: String) {
                        setRecommendView(null)

                    }
                })
    }

    /**
     * 获取收藏商家
     */
    private fun getFavoriteStores() {
        createService(UserApi::class.java)
                .collection("store", 0, perPage)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<CollectionIfModel>(mActivity) {
                    override fun onSuccess(t: CollectionIfModel) {
                        setFavoriteView(t.data)
                    }

                    override fun onFail(code: Int, msg: String) {
                        setFavoriteView(null)
                    }
                })

    }

    /**
     * 获取热门商家数据
     */
    private fun getHotStores() {
        createService(StoreApi::class.java).getStoreList(1, 0, 12)
                .compose(ResponseTransformer.handleResult())
                .compose(bindToLifecycle())
                .subscribe(object : BaseObserver<StoreListIfModel>(null) {
                    override fun onSuccess(t: StoreListIfModel) {
                        t.store?.let {
                            if (page == 0) {
                                getBanner()
                                msv.showContent()
                                content_view.isRefreshing = false
                                homeStoreAdapter.setNewData(it)
                            } else {
                                homeStoreAdapter.addData(it)
                            }
                        }

                        if (page + 1 < t.page_count) homeStoreAdapter.loadMoreComplete() else homeStoreAdapter.loadMoreEnd(true)

                    }

                    override fun onFail(code: Int, msg: String) {
                        content_view.isRefreshing = false
                        if (page == 0) {
                            msv.showError()
                        }
                    }

                })
    }

    private fun setBannerView(ad: List<IndexAdvertItemModel>?) {
        if (ad.isNullOrEmpty()) {
            bannerContainer.visibility = View.GONE
        } else {
            bannerContainer.visibility = View.VISIBLE
            convenientBanner.setPages(object : CBViewHolderCreator {
                override fun createHolder(itemView: View?): Holder<*> {
                    return NetImageHolderView(itemView)
                }

                override fun getLayoutId(): Int {
                    return R.layout.item_hot_store_banner
                }
            }, ad)
                    .setPageIndicator(IntArray(2).apply {
                        set(0, R.drawable.ic_page_indicator)
                        set(1, R.drawable.ic_page_indicator_focused)
                    })
                    .setOnItemClickListener { position ->
                        jump(mActivity, JumpIfModel().apply {
                            this.click_type = ad[position].click_type
                            this.url = ad[position].url
                            this.gotag = ad[position].gotag
                            this.gotobuy_url = ad[position].gotobuy_url
                            this.store_id = ad[position].store_id
                        })
                    }
                    .startTurning(3000)

        }
    }

    private fun setChannelView(t: List<IndexChannelItemModel>?) {
        if (t.isNullOrEmpty()) {
            rycvChannel.visibility = View.GONE
        } else {
            rycvChannel.visibility = View.VISIBLE
            homeStoreChannelAdapter.setNewData(t)
        }

    }

    private fun setSupperView(ad: List<IndexAdvertItemModel>?) {
        if (ad.isNullOrEmpty() || ad[0].store == null) {
            // 没有数控的话拉取默认groupon商家数据
            createService(StoreApi::class.java)
                    .getStoreDetail("groupon")
                    .compose(ResponseTransformer.handleResult())
                    .compose(bindToLifecycle())
                    .subscribe(object : BaseObserver<StoreDetailIfModel>(mActivity) {
                        override fun onSuccess(t: StoreDetailIfModel) {
                            supperContainer.visibility = View.VISIBLE
                            supperStoreName.text = t.name
                            supperStoreRebate.text = t.rebate
                            loadImage(originUrl = t.store_logo, targetView = supperStoreCover)
                            supperContainer.setOnClickListener {
                                startStoreDetailActivity(mActivity, "groupon")
                            }

                        }

                        override fun onFail(code: Int, msg: String) {
                            supperContainer.visibility = View.GONE

                        }
                    })
        } else {
            with(ad[0].store) {
                supperContainer.visibility = View.VISIBLE
                supperStoreName.text = this!!.name
                supperStoreRebate.text = this.rebate
                loadImage(originUrl = this.store_logo, targetView = supperStoreCover)
                supperContainer.setOnClickListener {
                    startStoreDetailActivity(mActivity, flag)

//                    jump(mActivity, JumpIfModel().apply {
//                        this.click_type = ad[0].click_type
//                        this.url = ad[0].url
//                        this.gotag = ad[0].gotag
//                        this.gotobuy_url = ad[0].gotobuy_url
//                        this.store_id = ad[0].store_id
//                    })
                }
            }

        }
    }

    private fun setBigSaleView(ad: List<IndexAdvertItemModel>?) {
        if (ad.isNullOrEmpty()) {
            bigSaleContainer.visibility = View.GONE
        } else {
            bigSaleContainer.visibility = View.VISIBLE
            loadImage(originUrl = ad[0].img_url, targetView = bigSaleCover)
            bigSaleContainer.setOnClickListener {
                jump(mActivity, JumpIfModel().apply {
                    this.click_type = ad[0].click_type
                    this.url = ad[0].url
                    this.gotag = ad[0].gotag
                    this.gotobuy_url = ad[0].gotobuy_url
                    this.store_id = ad[0].store_id
                })
            }

        }
    }

    private fun setRecommendView(store: List<StoreItemModel>?) {
        if (store.isNullOrEmpty()) {
            titleRecommend.visibility = View.GONE
            rycvRecommend.visibility = View.GONE
        } else {
            titleRecommend.visibility = View.VISIBLE
            rycvRecommend.visibility = View.VISIBLE
            homeStoreRecommendAdapter.setNewData(store)
        }

    }


    private fun setFavoriteView(collection: List<CollectionItemModel>?) {
        if (collection.isNullOrEmpty()) {
            titleFavorite.visibility = View.GONE
            rycvFavorite.visibility = View.GONE
        } else {
            titleFavorite.visibility = View.VISIBLE
            rycvFavorite.visibility = View.VISIBLE
            homeStoreFavoriteAdapter.setNewData(collection)
        }
    }

    inner class NetImageHolderView(itemView: View?) : Holder<IndexAdvertItemModel>(itemView) {
        private lateinit var ivBanner: GcbImageView
        private lateinit var group: Group
        private lateinit var ivStoreLogo: GcbImageView
        private lateinit var tvTitle1: TextView
        private lateinit var tvTitle2: TextView
        private lateinit var tvDiscount: TextView
        private lateinit var tvCouponCode: TextView
        private lateinit var llytCouponCode: LinearLayout

        override fun updateUI(data: IndexAdvertItemModel) {
            with(data) {
                loadImage(originUrl = img_url, targetView = ivBanner)

                if (Intersection_shape == 2) {
                    group.visibility = View.INVISIBLE
                } else {
                    group.visibility = View.VISIBLE
                    loadImage(originUrl = store?.store_logo ?: "", targetView = ivStoreLogo)
                    tvTitle1.text = banner_title
                    tvTitle2.text = banner_title_two
                    tvDiscount.text = store?.rebate
                    tvCouponCode.text = discount_code
                    llytCouponCode.visibility = if (TextUtils.isEmpty(discount_code)) View.INVISIBLE else View.VISIBLE
                }

            }
        }

        override fun initView(itemView: View) {
            ivBanner = itemView.findViewById(R.id.iv_banner)
            group = itemView.findViewById(R.id.group_left)
            ivStoreLogo = itemView.findViewById(R.id.iv_store_logo)
            tvTitle1 = itemView.findViewById(R.id.tv_title_1)
            tvTitle2 = itemView.findViewById(R.id.tv_title_2)
            tvDiscount = itemView.findViewById(R.id.tv_discount)
            tvCouponCode = itemView.findViewById(R.id.tv_coupon_code)
            llytCouponCode = itemView.findViewById(R.id.llyt_coupon_code)
        }
    }

    @Subscribe
    fun onLoginChange(loginChangeEvent: LoginChangeEvent) {
        page = 0
        getHomeStoreData()
    }

}
