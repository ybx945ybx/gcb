package com.gocashback.module_discover.activity

import android.app.Dialog
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.HORIZONTAL
import com.alibaba.android.arouter.facade.annotation.Route
import com.gocashback.lib_common.ACTIVITY_WELFARE
import com.gocashback.lib_common.base.GcbBaseActivity
import com.gocashback.lib_common.event.LoginChangeEvent
import com.gocashback.lib_common.language.LOCALE_CHINESE
import com.gocashback.lib_common.language.getLanguage
import com.gocashback.lib_common.network.*
import com.gocashback.lib_common.network.api.UserApi
import com.gocashback.lib_common.network.model.user.ExclusiveBenefitsIfModel
import com.gocashback.lib_common.share.ShareUtils
import com.gocashback.lib_common.startInviteActivity
import com.gocashback.lib_common.startLoginActivity
import com.gocashback.lib_common.startMainActivity
import com.gocashback.lib_common.widget.WelfareGetDlg
import com.gocashback.module_discover.R
import com.gocashback.module_discover.adapter.WelfareTaskAdapter
import com.gocashback.module_discover.model.WelfareTaskItemModel
import kotlinx.android.synthetic.main.activity_welfare.*
import org.greenrobot.eventbus.Subscribe

/**
 * @Author 55HAITAO
 * @Date 2019-08-01 10:13
 */
@Route(path = ACTIVITY_WELFARE)
class WelfareActivity : GcbBaseActivity() {

    private lateinit var welfareTaskAdapter: WelfareTaskAdapter
    private var list: ArrayList<WelfareTaskItemModel> = arrayListOf(WelfareTaskItemModel().apply { type = 0 }, WelfareTaskItemModel().apply { type = 1 }, WelfareTaskItemModel().apply { type = 2 })

    private var registerEnable = false
    private var shopEnable = false
    private var inviteEnable = false

    private lateinit var shareTitle: String
    private lateinit var shareText: String
    private lateinit var shareUrl: String
    private lateinit var shareImg: String

    override fun setLayoutId(): Int {
        return R.layout.activity_welfare
    }

    override fun initVars() {
        registerEventBus()

        shareTitle = resources.getString(R.string.welfare_share_title)
        shareText = resources.getString(R.string.welfare_share_text)
        shareUrl = "https://www.gocashback.com"
        shareImg = "https://d1t4h9gelh7map.cloudfront.net/data/upload/channel/201904/5cb1afd2cf1d9.png"

    }

    override fun initViews() {
        getImmersionBar()?.fitsSystemWindows(true)?.statusBarColor(R.color.white)?.statusBarDarkFont(true)?.init()

        rycv_task.apply {
            layoutManager = LinearLayoutManager(this@WelfareActivity).apply { orientation = HORIZONTAL }
            welfareTaskAdapter = WelfareTaskAdapter(this@WelfareActivity, list)
            adapter = welfareTaskAdapter
        }

        tv_welfare_get.setOnClickListener {
            tv_welfare_get.isEnabled = false

            createService(UserApi::class.java)
                    .exclusiveBenefits(1)
                    .compose(ResponseTransformer.handleResult())
                    .compose(bindToLifecycle())
                    .subscribe(object : BaseObserver<ExclusiveBenefitsIfModel>(this) {
                        override fun onSuccess(t: ExclusiveBenefitsIfModel) {
                            WelfareGetDlg(this@WelfareActivity, object : WelfareGetDlg.OnShareListener {
                                override fun onShare(dlg: Dialog) {
                                    ShareUtils.showShareBoard(this@WelfareActivity, shareTitle, shareText, shareUrl, shareImg)

                                }
                            }).show()
                        }

                        override fun onFail(code: Int, msg: String) {
                            super.onFail(code, msg)
                            tv_welfare_get.isEnabled = true

                        }
                    })
        }

        tv_tips.text = if (getLanguage(this) == LOCALE_CHINESE) "1.完成全部任务可获的额外\$6新手任务奖励\n" +
                "\n" +
                "2.推荐一位好友注册GoCashBack，好友需要成功完成一笔线上返利购物\n" +
                "\n" +
                "3.上述任务的所有奖励需生效后额外奖励才可提现\n" +
                "\n" +
                "4.本次活动最终解释权归GoCashBack所有\n" +
                "\n" else "1.Complete all tasks to earn an extra \$6 bonus\n" +
                "\n" +
                "2.Refer a friend to join and use GoCashBack for at least one online order, the task is completed when the cash back has become available \n" +
                "\n" +
                "3.Extra bonus will be able to withdraw after all tasks has been completed\n" +
                "\n" +
                "4.GoCashBack reserves the right to the final interpretation of these terms and conditions\n" +
                "\n"
    }

    override fun initEvent() {
        welfareTaskAdapter.setOnItemClickListener { adapter, view, position ->
            if (!welfareTaskAdapter.data[position].isFinish)
                when (welfareTaskAdapter.data[position].type) {
                    0 -> startLoginActivity(this)
                    1 -> startMainActivity(this)
                    2 -> startInviteActivity(this)
                }
        }

    }

    override fun initData() {
        if (isLogin())
            createService(UserApi::class.java)
                    .exclusiveBenefits(0)
                    .compose(ResponseTransformer.handleResult())
                    .compose(bindToLifecycle())
                    .subscribe(object : BaseObserver<ExclusiveBenefitsIfModel>(this) {
                        override fun onSuccess(t: ExclusiveBenefitsIfModel) {
                            with(t) {
                                list.apply {
                                    get(0).isFinish = true
                                    get(1).isFinish = is_shop == 1
                                    get(2).isFinish = is_invite == 1
                                }
                                welfareTaskAdapter.setNewData(list)

                                registerEnable = true
                                shopEnable = is_shop == 1
                                inviteEnable = is_invite == 1

                                checkGetEnable(is_award)
                            }


                        }
                    })

    }

    private fun checkGetEnable(is_award: Int) {
        if (is_award == 1) {
            tv_welfare_get.isEnabled = false
        } else {
            tv_welfare_get.isEnabled = registerEnable && shopEnable && inviteEnable

        }

    }

    @Subscribe
    fun onLoginChange(loginChangeEvent: LoginChangeEvent) {
        initData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ShareUtils.onActivityResult(this, requestCode, resultCode, data)
    }
}